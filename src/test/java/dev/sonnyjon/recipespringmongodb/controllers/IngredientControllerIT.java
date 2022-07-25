package dev.sonnyjon.recipespringmongodb.controllers;

import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.services.IngredientService;
import dev.sonnyjon.recipespringmongodb.services.RecipeService;
import dev.sonnyjon.recipespringmongodb.services.UnitOfMeasureService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Sonny on 7/15/2022.
 */
@ExtendWith(MockitoExtension.class)
class IngredientControllerIT
{
    @Mock
    UnitOfMeasureService unitOfMeasureService;
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    IngredientController controller;

    MockMvc mockMvc;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new IngredientController(ingredientService, recipeService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    public void listIngredients_shouldReturnListUri_whenRecipeFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/ingredients", RECIPE_ID);
        final String EXPECTED_RETURN = "recipe/ingredient/list";

        // given
        final RecipeDto recipe = new RecipeDto();
        recipe.setId( RECIPE_ID );

        when(recipeService.findDtoById( RECIPE_ID )).thenReturn( recipe );

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));

        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void listIngredients_shouldThrowException_whenRecipeNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/ingredients", RECIPE_ID);

        // given
        final RecipeDto recipe = new RecipeDto();
        recipe.setId( RECIPE_ID );

        when(recipeService.findDtoById( RECIPE_ID )).thenThrow(NotFoundException.class);

        // when
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        // then
        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void showIngredient_shouldReturnShowUri_whenBothIdsFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String INGRED_ID = "INGRED-1";
        final String TEST_URI = String.format("/recipe/%1$s/ingredient/%2$s/show", RECIPE_ID, INGRED_ID);
        final String EXPECTED_RETURN = "recipe/ingredient/show";

        // given
        Recipe recipe = new Recipe();
        recipe.setId( RECIPE_ID );

        IngredientDto ingredient = new IngredientDto();
        ingredient.setId( INGRED_ID );

        when(ingredientService.findInRecipe( RECIPE_ID, INGRED_ID )).thenReturn( ingredient );

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));

        verify(ingredientService, times(1)).findInRecipe(anyString(), anyString());
    }

    @Test
    public void showIngredient_shouldThrowException_whenEitherIdNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String INGRED_ID = "INGRED-1";
        final String TEST_URI = String.format("/recipe/%1$s/ingredient/%2$s/show", RECIPE_ID, INGRED_ID);

        // given
        when(ingredientService.findInRecipe( RECIPE_ID, INGRED_ID )).thenThrow(NotFoundException.class);

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(ingredientService, times(1)).findInRecipe(anyString(), anyString());
    }

    @Test
    public void newIngredient_shouldReturnIngredientForm_whenRecipeFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/ingredient/new", RECIPE_ID);
        final String EXPECTED_RETURN = "recipe/ingredient/ingredientform";

        // given
        final RecipeDto testRecipe = new RecipeDto();
        testRecipe.setId( RECIPE_ID );

        when(recipeService.findDtoById( RECIPE_ID )).thenReturn( testRecipe );
        when(unitOfMeasureService.listAllUoms()).thenReturn( new HashSet<>() );

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));

        verify(recipeService, times(1)).findDtoById(anyString());
        verify(unitOfMeasureService, times(1)).listAllUoms();
    }

    @Test
    public void newIngredient_shouldThrowException_whenRecipeNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/ingredient/new", RECIPE_ID);

        // given
        final RecipeDto testRecipe = new RecipeDto();
        testRecipe.setId( RECIPE_ID );

        when(recipeService.findDtoById( RECIPE_ID )).thenThrow(NotFoundException.class);

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void updateIngredient_shouldReturnIngredientForm_whenBothIdsFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String INGRED_ID = "INGRED-1";
        final String TEST_URI = String.format("/recipe/%1$s/ingredient/%2$s/update", RECIPE_ID, INGRED_ID);
        final String EXPECTED_RETURN = "recipe/ingredient/ingredientform";

        // given
        IngredientDto ingredient = new IngredientDto();
        ingredient.setId( INGRED_ID );

        when(ingredientService.findInRecipe( RECIPE_ID, INGRED_ID )).thenReturn( ingredient );
        when(unitOfMeasureService.listAllUoms()).thenReturn( new HashSet<>() );

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));

        verify(ingredientService, times(1)).findInRecipe(anyString(), anyString());
        verify(unitOfMeasureService, times(1)).listAllUoms();
    }

    @Test
    public void updateIngredient_shouldThrowException_whenEitherIdNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String INGRED_ID = "INGRED-1";
        final String TEST_URI = String.format("/recipe/%1$s/ingredient/%2$s/update", RECIPE_ID, INGRED_ID);

        // given
        when(ingredientService.findInRecipe( RECIPE_ID, INGRED_ID )).thenThrow(NotFoundException.class);

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(
                    result -> assertTrue(result.getResolvedException() instanceof NotFoundException)
                );

        verify(ingredientService, times(1)).findInRecipe(anyString(), anyString());
    }

    // TODO Not working. Debug this once resources are in place
    @Test
    public void saveOrUpdate_shouldReturnShowUri_afterSave() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String INGRED_ID = "INGRED-1";
        final String TEST_URI = String.format("/recipe/%s/ingredient", RECIPE_ID);
        final String EXPECTED_RETURN = String.format("/recipe/%1$s/ingredient/%2$s/show", RECIPE_ID, INGRED_ID);

        // given
        IngredientDto expected = new IngredientDto();
        expected.setId( INGRED_ID );

        when(ingredientService.saveIngredient(anyString(), any())).thenReturn( expected );

        // when, then
        mockMvc.perform(post( TEST_URI )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "My new ingredient")
                        .param("amount", "3")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl( EXPECTED_RETURN ));

        verify(ingredientService, times(1)).saveIngredient(anyString(), any());
    }

    @Test
    public void saveOrUpdate_shouldThrowException_whenRecipeNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/ingredient", RECIPE_ID);

        // given
        when(ingredientService.saveIngredient(anyString(), any())).thenThrow( NotFoundException.class );

        // when, then
        mockMvc.perform(post( TEST_URI )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "My new ingredient")
                        .param("amount", "3")
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(ingredientService, times(1)).saveIngredient(anyString(), any());
    }

    @Test
    public void deleteIngredient_shouldReturnListUri_afterDelete() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String INGRED_ID = "INGRED-1";
        final String TEST_URI = String.format("/recipe/%1$s/ingredient/%2$s/delete", RECIPE_ID, INGRED_ID);
        final String EXPECTED_RETURN = String.format("/recipe/%s/ingredients", RECIPE_ID);

        // given
        doNothing().when(ingredientService).removeIngredient(anyString(), anyString());

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl( EXPECTED_RETURN ));

        verify(ingredientService, times(1)).removeIngredient(anyString(), anyString());

    }

    @Test
    public void deleteIngredient_shouldThrowException_whenEitherIdNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String INGRED_ID = "INGRED-1";
        final String TEST_URI = String.format("/recipe/%1$s/ingredient/%2$s/delete", RECIPE_ID, INGRED_ID);

        // given
        doThrow(NotFoundException.class).when(ingredientService).removeIngredient(anyString(), anyString());

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        verify(ingredientService, times(1)).removeIngredient(anyString(), anyString());
    }
}