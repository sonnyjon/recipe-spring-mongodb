package dev.sonnyjon.recipespringmongodb.repositories;

import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
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
class UnitOfMeasureRepositoryIT
{
    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Test
    void findByDescription_shouldReturn_UomWithGivenDescription()
    {
        // given
        final String EXPECTED_DESC = "Teaspoon";

        // when
        Optional<UnitOfMeasure> optional = unitOfMeasureRepository.findByDescription( EXPECTED_DESC );
        UnitOfMeasure uom = optional.orElse(new UnitOfMeasure());
        String ACTUAL_DESC = uom.getDescription();

        // then
        assertEquals( EXPECTED_DESC, ACTUAL_DESC );
    }
}