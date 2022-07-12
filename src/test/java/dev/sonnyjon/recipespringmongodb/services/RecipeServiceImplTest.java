package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.RecipeConverter;
import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.repositories.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 7/12/2022.
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
class RecipeServiceImplTest
{
    @Mock
    RecipeRepository recipeRepository;

    RecipeConverter converter;
    RecipeService recipeService;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);
        converter = new RecipeConverter();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    void getRecipes_shouldReturn_allRecipes()
    {
        // given
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.setId("RECIPE-1");
        recipes.add(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setId("RECIPE-2");
        recipes.add(recipe2);

        Recipe recipe3 = new Recipe();
        recipe3.setId("RECIPE-3");
        recipes.add(recipe3);

        Set<Recipe> expectedRecipes = new HashSet<>(recipes);

        when(recipeRepository.findAll()).thenReturn(recipes);

        // when
        Set<Recipe> actualRecipes = recipeService.getRecipes();

        // then
        assertEquals(expectedRecipes, actualRecipes);
    }

    @Test
    void findById_shouldReturnRecipe_whenFound()
    {
        // given
        Recipe expected = new Recipe();
        expected.setId("RECIPE-1");
        Optional<Recipe> optional = Optional.of(expected);

        when(recipeRepository.findById(anyString())).thenReturn(optional);

        // when
        Recipe actual = recipeService.findById("RECIPE-1");

        // then
        assertEquals(expected, actual);
    }

    @Test
    void findById_shouldThrowException_whenNotFound()
    {
        // given
        when(recipeRepository.findById(anyString())).thenThrow(NotFoundException.class);

        // when
        Executable executable = () -> recipeService.findById("RECIPE-1");

        // then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void findDtoById_shouldReturnDTO_whenFound()
    {
        // given
        Recipe recipe = new Recipe();
        recipe.setId("RECIPE-1");
        Optional<Recipe> optional = Optional.of(recipe);
        RecipeDto expectedDto = converter.convertEntity(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(optional);

        // when
        RecipeDto actualDto = recipeService.findDtoById("RECIPE-1");

        // then
        assertEquals(expectedDto.getId(), actualDto.getId());
    }

    @Test
    void findDtoById_shouldThrowException_whenNotFound()
    {
        // given
        when(recipeRepository.findById(anyString())).thenThrow(NotFoundException.class);

        // when
        Executable executable = () -> recipeService.findDtoById("RECIPE-1");

        // then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void saveRecipe_shouldReturn_equivDTO_Object()
    {
        // given
        Recipe recipe = new Recipe();
        recipe.setId("RECIPE-1");
        recipe.setDescription("RECIPE_DESC");
        Optional<Recipe> optional = Optional.of(recipe);
        RecipeDto expectedDto = converter.convertEntity(recipe);

        when(recipeRepository.save(any())).thenReturn(recipe);

        // when
        RecipeDto actualDto = recipeService.saveRecipe(expectedDto);

        // then
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getDescription(), actualDto.getDescription());
    }

    @Test
    void deleteById()
    {
        // given
        doNothing().when(recipeRepository).deleteById(anyString());

        // when
        recipeService.deleteById("RECIPE-1");

        // then
        verify(recipeRepository, times(1)).deleteById(anyString());
    }
}