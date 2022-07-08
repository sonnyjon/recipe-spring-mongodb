package dev.sonnyjon.recipespringmongodb.repository;

import dev.sonnyjon.recipespringmongodb.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Sonny on 7/7/2022.
 */
public interface RecipeRepository extends MongoRepository<Recipe, String>
{
}
