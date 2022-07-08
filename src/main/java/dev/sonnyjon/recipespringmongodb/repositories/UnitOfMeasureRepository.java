package dev.sonnyjon.recipespringmongodb.repositories;

import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by Sonny on 7/7/2022.
 */
public interface UnitOfMeasureRepository extends MongoRepository<UnitOfMeasure, String>
{
    Optional<UnitOfMeasure> findByDescription(String description);
}
