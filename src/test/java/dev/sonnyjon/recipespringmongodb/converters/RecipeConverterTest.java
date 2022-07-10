package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.CategoryDto;
import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.dto.NotesDto;
import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by Sonny on 7/9/2022.
 */
class RecipeConverterTest
{
    // Recipe
    public static final String RECIPE_ID = "RECIPE-1";
    public static final String RECIPE_DESC = "RECIPE_DESC";
    public static final Integer PREP_TIME = 7;
    public static final Integer COOK_TIME = 5;
    public static final Integer SERVINGS = 3;
    public static final String SOURCE = "SOURCE";
    public static final String URL = "URL";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    // Ingredient
    public static final String INGRED_ID_1 = "INGRED-1";
    public static final String INGRED_ID_2 = "INGRED-2";
    // Notes
    public static final String NOTES_ID = "NOTES-1";
    // Category
    public static final String CAT_ID_1 = "CAT-1";
    public static final String CAT_ID_2 = "CAT-2";

    RecipeConverter converter;

    @BeforeEach
    void setUp()
    {
        converter = new RecipeConverter();
    }

    @Test
    void convertEntity_shouldReturn_equivDto()
    {
        Recipe entity = new Recipe();
        entity.setId(RECIPE_ID);
        entity.setDescription(RECIPE_DESC);
        entity.setPrepTime(PREP_TIME);
        entity.setCookTime(COOK_TIME);
        entity.setServings(SERVINGS);
        entity.setSource(SOURCE);
        entity.setUrl(URL);
        entity.setDirections(DIRECTIONS);
        entity.addIngredient(getTestIngredientEntity(INGRED_ID_1));
        entity.addIngredient(getTestIngredientEntity(INGRED_ID_2));
        entity.setDifficulty(DIFFICULTY);
        entity.setNotes(getTestNotesEntity());
        entity.getCategories().add(getTestCategoryEntity(CAT_ID_1));
        entity.getCategories().add(getTestCategoryEntity(CAT_ID_2));

        RecipeDto dto = converter.convertEntity(entity);

        assertEquals(RECIPE_ID, dto.getId());
        assertEquals(RECIPE_DESC, dto.getDescription());
        assertEquals(PREP_TIME, dto.getPrepTime());
        assertEquals(COOK_TIME, dto.getCookTime());
        assertEquals(SERVINGS, dto.getServings());
        assertEquals(SOURCE, dto.getSource());
        assertEquals(URL, dto.getUrl());
        assertEquals(DIRECTIONS, dto.getDirections());
        assertEquals(2, dto.getIngredients().size());
        assertEquals(DIFFICULTY, dto.getDifficulty());
        assertEquals(NOTES_ID, dto.getNotes().getId());
        assertEquals(2, dto.getCategories().size());
    }

    @Test
    void convertDto_shouldReturn_equivEntity()
    {
        RecipeDto dto = new RecipeDto();
        dto.setId(RECIPE_ID);
        dto.setDescription(RECIPE_DESC);
        dto.setPrepTime(PREP_TIME);
        dto.setCookTime(COOK_TIME);
        dto.setServings(SERVINGS);
        dto.setSource(SOURCE);
        dto.setUrl(URL);
        dto.setDirections(DIRECTIONS);
        dto.getIngredients().add(getTestIngredientDto(INGRED_ID_1));
        dto.getIngredients().add(getTestIngredientDto(INGRED_ID_2));
        dto.setDifficulty(DIFFICULTY);
        dto.setNotes(getTestNotesDto());
        dto.getCategories().add(getTestCategoryDto(CAT_ID_1));
        dto.getCategories().add(getTestCategoryDto(CAT_ID_2));

        Recipe entity = converter.convertDto(dto);

        assertEquals(RECIPE_ID, entity.getId());
        assertEquals(RECIPE_DESC, entity.getDescription());
        assertEquals(PREP_TIME, entity.getPrepTime());
        assertEquals(COOK_TIME, entity.getCookTime());
        assertEquals(SERVINGS, entity.getServings());
        assertEquals(SOURCE, entity.getSource());
        assertEquals(URL, entity.getUrl());
        assertEquals(DIRECTIONS, entity.getDirections());
        assertEquals(2, entity.getIngredients().size());
        assertEquals(DIFFICULTY, entity.getDifficulty());
        assertEquals(NOTES_ID, entity.getNotes().getId());
        assertEquals(2, entity.getCategories().size());
    }

    @Test
    void nullEntity_shouldReturn_null()
    {
        RecipeDto dto = converter.convertEntity(null);
        assertNull(dto);
    }

    @Test
    void nullDto_shouldReturn_null()
    {
        Recipe entity = converter.convertDto(null);
        assertNull(entity);
    }

    private Ingredient getTestIngredientEntity(String id)
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);

        return ingredient;
    }

    private IngredientDto getTestIngredientDto(String id)
    {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(id);

        return ingredientDto;
    }

    private Notes getTestNotesEntity()
    {
        Notes notes = new Notes();
        notes.setId(NOTES_ID);

        return notes;
    }

    private NotesDto getTestNotesDto()
    {
        NotesDto notesDto = new NotesDto();
        notesDto.setId(NOTES_ID);

        return notesDto;
    }

    private Category getTestCategoryEntity(String id)
    {
        Category category = new Category();
        category.setId(id);

        return category;
    }

    private CategoryDto getTestCategoryDto(String id)
    {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(id);

        return categoryDto;
    }
}