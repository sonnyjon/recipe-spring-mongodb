package dev.sonnyjon.recipespringmongodb.dto;

import dev.sonnyjon.recipespringmongodb.model.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
    private Set<IngredientDto> ingredients = new HashSet<>();
    private Byte[] image;
    private Difficulty difficulty;
    private NotesDto notes;
    private Set<CategoryDto> categories = new HashSet<>();
}
