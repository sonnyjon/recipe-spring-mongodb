package dev.sonnyjon.recipespringmongodb.dto;

import dev.sonnyjon.recipespringmongodb.model.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonny on 7/8/2022.
 */
@Getter
@Setter
@NoArgsConstructor
public class RecipeDto
{
    private String id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private List<IngredientDto> ingredients = new ArrayList<>();
    private Byte[] image;
    private Difficulty difficulty;
    private NotesDto notes;
    private List<CategoryDto> categories = new ArrayList<>();
}
