package dev.sonnyjon.recipespringmongodb.repositories;

import dev.sonnyjon.recipespringmongodb.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by Sonny on 7/7/2022.
 */
public interface RecipeRepository extends MongoRepository<Recipe, String>
{
    @Query("{'ingredients.ingredient.id': ?0}")
    List<Recipe> findByIngredientId(String ingredientId);
}
