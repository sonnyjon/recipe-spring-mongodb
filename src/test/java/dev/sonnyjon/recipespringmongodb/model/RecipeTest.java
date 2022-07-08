package dev.sonnyjon.recipespringmongodb.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Sonny on 7/7/2022.
 */
@ActiveProfiles(profiles = {"test"})
class RecipeTest
{
    static final String RECIPE_ID = "RECIPE-1";
    static final String NOTES_ID = "NOTES-1";
    static final String INGRED_ID = "INGRED-1";

    Recipe recipe;

    @BeforeEach
    void setUp()
    {
        recipe = new Recipe();
        recipe.setId(RECIPE_ID);
    }

    @Test
    void setNonNullNotes_notesRecipe_shouldBeThisRecipe()
    {
        Notes notes = new Notes();
        notes.setId(NOTES_ID);
        recipe.setNotes(notes);

        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(RECIPE_ID, notes.getRecipe().getId());
    }

    @Test
    void addIngredient_returnRecipe_shouldContainIngredient()
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGRED_ID);
        Recipe recipeAfter = recipe.addIngredient(ingredient);

        assertTrue(recipeAfter.getIngredients().contains(ingredient));
    }
}