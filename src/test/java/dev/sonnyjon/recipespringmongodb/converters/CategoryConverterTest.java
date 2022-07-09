package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.CategoryDto;
import dev.sonnyjon.recipespringmongodb.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Sonny on 7/8/2022.
 */
@ActiveProfiles(profiles = {"test"})
class CategoryConverterTest
{
    public static final String CAT_ID = "CAT-1";
    public static final String DESCRIPTION = "TEST_DESC";

    CategoryConverter converter;

    @BeforeEach
    void setUp()
    {
        converter = new CategoryConverter();
    }

    @Test
    void convertEntity_shouldReturn_equivDto()
    {
        Category entity = new Category();
        entity.setId(CAT_ID);
        entity.setDescription(DESCRIPTION);

        CategoryDto dto = converter.convertEntity(entity);

        assertEquals(CAT_ID, dto.getId());
        assertEquals(DESCRIPTION, dto.getDescription());
    }

    @Test
    void convertDto_shouldReturn_equivEntity()
    {
        CategoryDto dto = new CategoryDto();
        dto.setId(CAT_ID);
        dto.setDescription(DESCRIPTION);

        Category entity = converter.convertDto(dto);

        assertEquals(CAT_ID, entity.getId());
        assertEquals(DESCRIPTION, entity.getDescription());
    }

    @Test
    void nullEntity_shouldReturn_null()
    {
        CategoryDto dto = converter.convertEntity(null);
        assertNull(dto);
    }

    @Test
    void nullDto_shouldReturn_null()
    {
        Category entity = converter.convertDto(null);
        assertNull(entity);
    }
}