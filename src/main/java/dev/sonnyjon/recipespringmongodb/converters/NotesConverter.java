package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.NotesDto;
import dev.sonnyjon.recipespringmongodb.model.Notes;
import lombok.Synchronized;

/**
 * Created by Sonny on 7/8/2022.
 */
public class NotesConverter implements DualConverter<Notes, NotesDto>
{
    @Synchronized
    @Override
    public NotesDto convertEntity(Notes entity)
    {
        if (entity == null) return null;

        final NotesDto dto = new NotesDto();
        dto.setId(entity.getId());
        dto.setRecipeNotes(entity.getRecipeNotes());

        return dto;
    }

    @Synchronized
    @Override
    public Notes convertDto(NotesDto dto)
    {
        if (dto == null) return null;

        final Notes entity = new Notes();
        entity.setId(dto.getId());
        entity.setRecipeNotes(dto.getRecipeNotes());

        return entity;
    }
}
