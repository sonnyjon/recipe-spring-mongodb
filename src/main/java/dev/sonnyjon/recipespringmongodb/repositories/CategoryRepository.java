package dev.sonnyjon.recipespringmongodb.repositories;

import dev.sonnyjon.recipespringmongodb.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by Sonny on 7/7/2022.
 */
public interface CategoryRepository extends MongoRepository<Category, String>
{
    Optional<Category> findByDescription(String description);
}
