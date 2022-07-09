package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import lombok.Synchronized;

/**
 * Created by Sonny on 7/8/2022.
 */
public class UnitOfMeasureConverter implements DualConverter<UnitOfMeasure, UnitOfMeasureDto>
{
    @Synchronized
    @Override
    public UnitOfMeasureDto convertEntity(UnitOfMeasure entity)
    {
        if (entity == null) return null;

        final UnitOfMeasureDto dto = new UnitOfMeasureDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    @Synchronized
    @Override
    public UnitOfMeasure convertDto(UnitOfMeasureDto dto)
    {
        if (dto == null) return null;

        final UnitOfMeasure entity = new UnitOfMeasure();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
