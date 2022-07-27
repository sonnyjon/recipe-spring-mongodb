package dev.sonnyjon.recipespringmongodb.repositories;

import dev.sonnyjon.recipespringmongodb.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Sonny on 7/17/2022.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CategoryRepositoryIT
{
    @Autowired
    CategoryRepository repository;

    @Test
    void findByDescription()
    {
        // given
        final String EXPECTED_DESC = "Italian";

        // when
        Optional<Category> optional = repository.findByDescription( EXPECTED_DESC );
        Category category = optional.orElse(new Category());
        String ACTUAL_DESC = category.getDescription();

        // then
        assertEquals( EXPECTED_DESC, ACTUAL_DESC );
    }
}