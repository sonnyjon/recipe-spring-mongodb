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
    private Set<Ingredient> ingredients = new HashSet<>();
    private Byte[] image;
    private Difficulty difficulty;
    private Notes notes;

    @DBRef
    private Set<Category> categories = new HashSet<>();

    /**
     * Sets this Recipe's notes to the parameter notes and creates a bidirectional relationship by
     * setting the parameter's recipe to this Recipe, given the parameter is not null.
     *
     * @param notes Notes for this Recipe.
     */
    public void setNotes(Notes notes)
    {
        this.notes = notes;
        if (notes != null) notes.setRecipe(this);
    }

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
