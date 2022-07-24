package dev.sonnyjon.recipespringmongodb.controllers;

import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.services.RecipeService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Sonny on 7/15/2022.
 */
@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest
{
    @Mock
    RecipeService recipeService;

    RecipeController controller;
    MockMvc mockMvc;
    AutoCloseable mocks;

    @BeforeEach
    public void setUp() throws Exception
    {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                .setControllerAdvice(new ControllerExceptionHandler())
                                .build();
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    public void showById_shouldReturnShowUri_whenRecipeFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/show", RECIPE_ID);
        final String EXPECTED_RETURN = "recipe/show";

        // given
        RecipeDto recipe = new RecipeDto();
        recipe.setId( RECIPE_ID );

        when(recipeService.findDtoById(anyString())).thenReturn(recipe);

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));

        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void showById_shouldThrowException_whenRecipeNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/show", RECIPE_ID);

        // given
        when(recipeService.findDtoById(anyString())).thenThrow(NotFoundException.class);

        // when
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(forwardedUrl( "404error" ));

        // then
        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void newRecipe_shouldReturnFormUri() throws Exception
    {
        final String TEST_URI = "/recipe/new";
        final String EXPECTED_RETURN = "recipe/recipeform";

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));
    }

    @Test
    public void updateRecipe_shouldReturnFormUri_whenRecipeFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/update", RECIPE_ID);
        final String EXPECTED_RETURN = "recipe/recipeform";

        // given
        RecipeDto recipe = new RecipeDto();
        recipe.setId( RECIPE_ID );

        when(recipeService.findDtoById(anyString())).thenReturn(recipe);

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));

        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void updateRecipe_shouldThrowException_whenRecipeNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/update", RECIPE_ID);

        // given
        when(recipeService.findDtoById(anyString())).thenThrow(NotFoundException.class);

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(forwardedUrl( "404error" ));

        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void saveOrUpdate_shouldReturnShowUri_afterSave() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = "/recipe";
        final String EXPECTED_RETURN = String.format("/recipe/%s/show", RECIPE_ID);

        // given
        RecipeDto expectedRecipe = new RecipeDto();
        expectedRecipe.setId( RECIPE_ID );

        when(recipeService.saveRecipe(any())).thenReturn(expectedRecipe);

        // when, then
        mockMvc.perform(post( TEST_URI )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "My new recipe")
                        .param("descriptions", "This is how you do it")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl( EXPECTED_RETURN ));

        // then
    }

    // TODO Form validation tests

//    @Test
//    public void testPostNewRecipeFormValidationFail() throws Exception
//    {
//        RecipeCommand command = new RecipeCommand();
//        command.setId("2");
//
//        when(recipeService.saveRecipeCommand(any())).thenReturn(command);
//
//        mockMvc.perform(post("/recipe")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("id", "")
//                        .param("cookTime", "3000")
//
//                )
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(view().name("recipe/recipeform"));
//    }

    @Test
    public void deleteById_shouldRedirectToRoot_afterDelete() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/delete", RECIPE_ID);
        final String EXPECTED_RETURN = "/";

        // given
        doNothing().when(recipeService).deleteById(anyString());

        // when, then
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl( EXPECTED_RETURN ));

        verify(recipeService, times(1)).deleteById(anyString());
    }
}