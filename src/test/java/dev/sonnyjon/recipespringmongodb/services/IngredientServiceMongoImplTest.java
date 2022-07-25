package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.IngredientConverter;
import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import dev.sonnyjon.recipespringmongodb.repositories.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 7/20/2022.
 */
@ExtendWith(SpringExtension.class)
class IngredientServiceMongoImplTest
{
    public static final String RECIPE_ID = "RECIPE-1";
    public static final String INGRED1_ID = "INGRED1-ID";
    public static final String INGRED2_ID = "INGRED2-ID";
    public static final String INGRED1_DESC = "INGRED1-DESC";
    public static final String INGRED2_DESC = "INGRED2-DESC";
    public static final BigDecimal AMOUNT = new BigDecimal(2);
    public static final String UOM_ID = "UOM-1";

    @Mock
    RecipeRepository recipeRepository;

    IngredientService ingredientService;
    IngredientConverter converter;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceMongoImpl( recipeRepository );
        converter = new IngredientConverter();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    void findInRecipe_shouldReturnDto_whenFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Optional<Recipe> optional = Optional.of( testRecipe );
        Ingredient testIngredient = getTestIngredient( INGRED1_ID, INGRED1_DESC );

        when(recipeRepository.findById(anyString())).thenReturn( optional );

        // when
        IngredientDto expected = converter.convertEntity( testIngredient );
        IngredientDto actual = ingredientService.findInRecipe( testRecipe.getId(), expected.getId() );

        // then
        assertEquals( expected.getDescription(), actual.getDescription() );
        assertEquals( expected.getAmount(), actual.getAmount() );
        assertEquals( expected.getUom().getId(), actual.getUom().getId() );

        verify(recipeRepository, times(1)).findById(any());
    }

    @Test
    void findInRecipe_shouldThrowException_whenRecipeNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED1_ID, INGRED1_DESC );
        IngredientDto expected = converter.convertEntity( testIngredient );

        when(recipeRepository.findByIngredientId(anyString())).thenThrow( NotFoundException.class );

        // when
        Executable executable = () -> ingredientService.findInRecipe( testRecipe.getId(), expected.getId() );

        // then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void saveIngredient_shouldAdd_NewIngredient()
    {
        // given
        Recipe testRecipe = getTestRecipeWithNoIngredient();
        Recipe expectedRecipe = getTestRecipeWithOneIngredient();
        Optional<Recipe> recipeOptional = Optional.of( testRecipe );

        Ingredient expectedIngredient = getTestIngredient( INGRED1_ID, INGRED1_DESC );
        IngredientDto testIngredient = converter.convertEntity( expectedIngredient );

        when(recipeRepository.findById(anyString())).thenReturn( recipeOptional );
        when(recipeRepository.save(any())).thenReturn( expectedRecipe );

        // when
        IngredientDto actualDto = ingredientService.saveIngredient( testRecipe.getId(), testIngredient );

        // then
        assertNotNull( actualDto.getId() );
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void saveIngredient_shouldUpdate_ExistingIngredient()
    {
        // given
        Ingredient expectedIngredient = getTestIngredient( INGRED1_ID, "CHANGED" );
        Recipe testRecipe = getTestRecipeWithOneIngredient();
        Optional<Recipe> optional = Optional.of( testRecipe );

        Recipe expectedRecipe = getTestRecipeWithOneIngredient();
        expectedRecipe.getIngredients().clear();
        expectedRecipe.addIngredient( expectedIngredient );

        List<Recipe> recipes = new ArrayList<>();
        recipes.add( testRecipe );

        IngredientDto testIngredient = converter.convertEntity( expectedIngredient );

        when(recipeRepository.findById(anyString())).thenReturn( optional );
        when(recipeRepository.save(any())).thenReturn( expectedRecipe );

        // when
        IngredientDto actualDto = ingredientService.saveIngredient( testRecipe.getId(), testIngredient );

        // then
        assertEquals( expectedIngredient.getId(), actualDto.getId() );
        assertEquals( "CHANGED", actualDto.getDescription() );
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void saveIngredient_shouldThrowException_whenRecipeNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient expected = getTestIngredient( INGRED1_ID, INGRED1_DESC );
        IngredientDto testIngredient = converter.convertEntity( expected );

        when(recipeRepository.findById(anyString())).thenThrow( NotFoundException.class );

        // when
        Executable executable = () -> ingredientService.saveIngredient( testRecipe.getId(), testIngredient );

        // then
        assertThrows( NotFoundException.class, executable );
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    void removeIngredient_shouldRemoveIngredient_fromRecipe()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Recipe expectedRecipe = getTestRecipeWithOneIngredient();
        Optional<Recipe> optionalRecipe = Optional.of( testRecipe );

        Ingredient testIngredient = getTestIngredient( INGRED2_ID, INGRED2_DESC );

        when(recipeRepository.findById(anyString())).thenReturn( optionalRecipe );
        when(recipeRepository.save(any())).thenReturn( expectedRecipe );

        // when
        ingredientService.removeIngredient( testRecipe.getId(), testIngredient.getId() );

        // then
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void removeIngredient_shouldThrowException_whenRecipeNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED1_ID, INGRED1_DESC );

        when(recipeRepository.findByIngredientId(anyString())).thenThrow( NotFoundException.class );

        // when
        Executable executable = () -> ingredientService.removeIngredient( testRecipe.getId(), testIngredient.getId() );

        // then
        assertThrows( NotFoundException.class, executable );
    }

    //==================================================================================================================
    private Recipe getTestRecipeWithOneIngredient()
    {
        Recipe recipe = new Recipe();
        recipe.setId( RECIPE_ID );
        recipe.addIngredient(getTestIngredient( INGRED1_ID, INGRED1_DESC ));

        return recipe;
    }

    private Recipe getTestRecipeWithTwoIngredients()
    {
        Recipe recipe = getTestRecipeWithOneIngredient();
        recipe.addIngredient(getTestIngredient( INGRED2_ID, INGRED2_DESC ));

        return recipe;
    }

    private Recipe getTestRecipeWithNoIngredient()
    {
        Recipe recipe = new Recipe();
        recipe.setId( RECIPE_ID );

        return recipe;
    }

    private Ingredient getTestIngredient(String ingredientId, String description)
    {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId( UOM_ID );

        Ingredient ingredient = new Ingredient();
        ingredient.setId( ingredientId );
        ingredient.setDescription( description );
        ingredient.setAmount( AMOUNT );
        ingredient.setUom( uom );

        return ingredient;
    }
}