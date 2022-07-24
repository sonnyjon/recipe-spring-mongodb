package dev.sonnyjon.recipespringmongodb.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Sonny on 7/7/2022.
 */
class RecipeTest
{
    static final String RECIPE_ID = "RECIPE-1";
    static final String NOTES_ID = "NOTES-1";
    static final String INGRED_DESC = "INGRED-DESC";

    Recipe recipe;

    @BeforeEach
    void setUp()
    {
        recipe = new Recipe();
        recipe.setId( RECIPE_ID );
    }

    @Test
    void addIngredient_returnRecipe_shouldContainIngredient()
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription( INGRED_DESC );
        Recipe recipeAfter = recipe.addIngredient(ingredient);

        assertTrue(recipeAfter.getIngredients().contains(ingredient));
    }
}