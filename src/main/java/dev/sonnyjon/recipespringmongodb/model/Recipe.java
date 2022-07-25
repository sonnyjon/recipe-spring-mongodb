package dev.sonnyjon.recipespringmongodb.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sonny on 7/7/2022.
 */
@Getter
@Setter
@Document("recipe")
public class Recipe
{
    @Id
    private String id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Byte[] image;
    private Difficulty difficulty;
    private Notes notes;
    private Set<Ingredient> ingredients = new HashSet<>();
    @DBRef
    private Set<Category> categories = new HashSet<>();

    /**
     * Adds the ingredient parameter to this Recipe's ingredient collection.
     *
     * @param ingredient Recipe ingredient.
     * @return this Recipe with the newly added ingredient.
     */
    public Recipe addIngredient(Ingredient ingredient)
    {
        this.ingredients.add(ingredient);
        return this;
    }
}
