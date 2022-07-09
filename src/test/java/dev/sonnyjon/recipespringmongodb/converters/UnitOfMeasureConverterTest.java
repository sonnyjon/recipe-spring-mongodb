package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by Sonny on 7/8/2022.
 */
@ActiveProfiles(profiles = {"test"})
class UnitOfMeasureConverterTest
{
    public static final String UOM_ID = "UOM-1";
    public static final String DESCRIPTION = "UOM_DESC";

    UnitOfMeasureConverter converter;

    @BeforeEach
    void setUp()
    {
        converter = new UnitOfMeasureConverter();
    }

    @Test
    void convertEntity_shouldReturn_equivDto()
    {
        UnitOfMeasure entity = new UnitOfMeasure();
        entity.setId(UOM_ID);
        entity.setDescription(DESCRIPTION);

        UnitOfMeasureDto dto = converter.convertEntity(entity);

        assertEquals(UOM_ID, dto.getId());
        assertEquals(DESCRIPTION, dto.getDescription());
    }

    @Test
    void convertDto_shouldReturn_equivEntity()
    {
        UnitOfMeasureDto dto = new UnitOfMeasureDto();
        dto.setId(UOM_ID);
        dto.setDescription(DESCRIPTION);

        UnitOfMeasure entity = converter.convertDto(dto);

        assertEquals(UOM_ID, entity.getId());
        assertEquals(DESCRIPTION, entity.getDescription());
    }

    @Test
    void nullEntity_shouldReturn_null()
    {
        UnitOfMeasureDto dto = converter.convertEntity(null);
        assertNull(dto);
    }

    @Test
    void nullDto_shouldReturn_null()
    {
        UnitOfMeasure entity = converter.convertDto(null);
        assertNull(entity);
    }
}