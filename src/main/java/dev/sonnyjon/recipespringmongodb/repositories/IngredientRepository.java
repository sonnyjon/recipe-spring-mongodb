package dev.sonnyjon.recipespringmongodb.repositories;

import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Sonny on 7/22/2022.
 */
public interface IngredientRepository extends MongoRepository<Ingredient, String>
{
}
