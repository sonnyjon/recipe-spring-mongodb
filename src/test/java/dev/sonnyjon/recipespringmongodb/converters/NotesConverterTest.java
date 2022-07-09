package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.NotesDto;
import dev.sonnyjon.recipespringmongodb.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by Sonny on 7/8/2022.
 */
class NotesConverterTest
{
    public static final String NOTES_ID = "NOTES-1";
    public static final String RECIPE_NOTES = "RECIPE_NOTES";

    NotesConverter converter;

    @BeforeEach
    void setUp()
    {
        converter = new NotesConverter();
    }

    @Test
    void convertEntity_shouldReturn_equivDto()
    {
        Notes entity = new Notes();
        entity.setId(NOTES_ID);
        entity.setRecipeNotes(RECIPE_NOTES);

        NotesDto dto = converter.convertEntity(entity);

        assertEquals(NOTES_ID, dto.getId());
        assertEquals(RECIPE_NOTES, dto.getRecipeNotes());
    }

    @Test
    void convertDto_shouldReturn_equivEntity()
    {
        NotesDto dto = new NotesDto();
        dto.setId(NOTES_ID);
        dto.setRecipeNotes(RECIPE_NOTES);

        Notes entity = converter.convertDto(dto);

        assertEquals(NOTES_ID, entity.getId());
        assertEquals(RECIPE_NOTES, entity.getRecipeNotes());
    }

    @Test
    void nullEntity_shouldReturn_null()
    {
        NotesDto dto = converter.convertEntity(null);
        assertNull(dto);
    }

    @Test
    void nullDto_shouldReturn_null()
    {
        Notes entity = converter.convertDto(null);
        assertNull(entity);
    }
}