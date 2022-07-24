package dev.sonnyjon.recipespringmongodb.repositories;

import dev.sonnyjon.recipespringmongodb.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by Sonny on 7/7/2022.
 */
public interface RecipeRepository extends MongoRepository<Recipe, String>
{
    @Query("{'ingredient.id': ?0}")
    Recipe findByIngredientId(String id);
}
