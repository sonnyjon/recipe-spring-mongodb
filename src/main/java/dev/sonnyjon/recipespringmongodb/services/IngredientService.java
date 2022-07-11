package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;

/**
 * Created by Sonny on 7/9/2022.
 */
public interface IngredientService
{
    IngredientDto findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    IngredientDto saveIngredient(String recipeId, IngredientDto dto);
    boolean deleteById(String recipeId, String idToDelete);
}
