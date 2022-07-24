package dev.sonnyjon.recipespringmongodb.controllers;

import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sonny on 7/14/2022.
 */
@ExtendWith(MockitoExtension.class)
class IndexControllerTest
{
    @Mock
    RecipeService recipeService;

    IndexController controller;
    MockMvc mockMvc;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new IndexController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    void indexPage_shouldReturnIndexUri() throws Exception
    {
        final String RECIPE1_ID = "RECIPE-1";
        final String RECIPE2_ID = "RECIPE-2";
        final String expectedUri = "index";

        //given
        Recipe recipe1 = new Recipe();
        recipe1.setId(RECIPE1_ID);

        Recipe recipe2 = new Recipe();
        recipe2.setId(RECIPE2_ID);

        Set<Recipe> expectedRecipes = new HashSet<>();
        expectedRecipes.add(recipe1);
        expectedRecipes.add(recipe2);

        when(recipeService.getRecipes()).thenReturn(expectedRecipes);

        //when
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(expectedUri));

        //then
        verify(recipeService, times(1)).getRecipes();
    }
}