package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.model.Recipe;

import java.util.Set;

/**
 * Created by Sonny on 7/9/2022.
 */
public interface RecipeService
{
    Set<Recipe> getRecipes();

    Recipe findById(String id);

    RecipeDto findDtoById(String id);

    RecipeDto saveRecipe(RecipeDto dto);

    void deleteById(String idToDelete);
}
