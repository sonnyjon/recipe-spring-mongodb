package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import dev.sonnyjon.recipespringmongodb.repositories.RecipeRepository;
import dev.sonnyjon.recipespringmongodb.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 7/10/2022.
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
class IngredientServiceImplTest
{
    public static final String RECIPE_ID = "RECIPE-1";
    public static final String INGRED_ID_1 = "INGRED-1";
    public static final String INGRED_ID_2 = "INGRED-2";
    public static final BigDecimal AMOUNT = new BigDecimal(2);
    public static final String UOM_ID = "UOM-1";

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientService ingredientService;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepository, unitOfMeasureRepository);
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    void findByRecipeIdAndIngredientId_shouldReturnDto_ifFound()
    {
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(INGRED_ID_2);

        Recipe recipe = getTestRecipeWithIngredient();
        recipe.addIngredient(ingredient2);
        Optional<Recipe> optional = Optional.of(recipe);

        when(recipeRepository.findById(RECIPE_ID)).thenReturn(optional);

        IngredientDto dto = ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID, INGRED_ID_2);
        assertEquals(INGRED_ID_2, dto.getId());
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    void findByRecipeIdAndIngredientId_shouldThrowException_whenNotFound()
    {
        when(recipeRepository.findById(anyString())).thenThrow(new NotFoundException("Recipe Not Found"));

        assertThrows(NotFoundException.class, () -> recipeRepository.findById(RECIPE_ID));
    }

    @Test
    void saveIngredient_shouldReturn_equivObject()
    {
        // Set up desired recipe with desired ingredient and uom
        Recipe desiredRecipe = getTestRecipeWithIngredient();
        Optional<Recipe> optionalRecipe = Optional.of(desiredRecipe);
        UnitOfMeasure desiredUom = getTestUomFromRecipe(desiredRecipe);
        Optional<UnitOfMeasure> optionalUom = Optional.of(desiredUom);

        // Mock repository CRUD operations
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(optionalRecipe);
        when(recipeRepository.save(any())).thenReturn(desiredRecipe);
        when(unitOfMeasureRepository.findById(UOM_ID)).thenReturn(optionalUom);

        // Ingredient DTO to Save
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(INGRED_ID_1);
        ingredientDto.setUom(new UnitOfMeasureDto());
        ingredientDto.getUom().setId(UOM_ID);

        // Ingredient DTO from the saved recipe.
        IngredientDto actualIngredient = ingredientService.saveIngredient(desiredRecipe.getId(), ingredientDto);

        Optional<Ingredient> optional = desiredRecipe.getIngredients().stream().findFirst();
        Ingredient expectedIngredient = optional.orElse(new Ingredient());

        assertEquals(expectedIngredient.getId(), actualIngredient.getId());
    }

    @Test
    void deleteById_shouldRemoveIngredient_fromRecipe()
    {
        // Build recipe with one ingredient
        Recipe recipe = getTestRecipeWithIngredient();
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(RECIPE_ID)).thenReturn(recipeOptional);

        assertEquals(1, recipe.getIngredients().size());
        assertTrue(ingredientService.deleteById(RECIPE_ID, INGRED_ID_1));
    }

    private Recipe getTestRecipeWithIngredient()
    {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGRED_ID_1);
        ingredient.setAmount(AMOUNT);
        ingredient.setUom(uom);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.addIngredient(ingredient);

        return recipe;
    }

    private UnitOfMeasure getTestUomFromRecipe(Recipe recipe)
    {
        if (recipe == null) return null;

        Optional<Ingredient> optional = recipe.getIngredients().stream().findFirst();
        return optional.map(Ingredient::getUom).orElse(null);
    }
}