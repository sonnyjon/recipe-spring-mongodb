package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sonny on 7/8/2022.
 */
public class IngredientConverter implements DualConverter<Ingredient, IngredientDto>
{
    private final UnitOfMeasureConverter uomConverter = new UnitOfMeasureConverter();


    @Synchronized
    public List<IngredientDto> convertEntities(Set<Ingredient> entities)
    {
        if (entities == null) return null;

        final List<IngredientDto> dtos = new ArrayList<>();
        entities.forEach((Ingredient entity) -> dtos.add(convertEntity(entity)));

        return dtos;
    }

    @Synchronized
    public Set<Ingredient> convertDtos(List<IngredientDto> dtos)
    {
        if (dtos == null) return null;

        final Set<Ingredient> entities = new HashSet<>();
        dtos.forEach((IngredientDto dto) -> entities.add(convertDto(dto)));

        return entities;
    }

    @Synchronized
    @Override
    public IngredientDto convertEntity(Ingredient entity)
    {
        if (entity == null) return null;

        final UnitOfMeasureDto uomDto = uomConverter.convertEntity(entity.getUom());
        final IngredientDto dto = new IngredientDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setAmount(entity.getAmount());
        dto.setUom(uomDto);

        return dto;
    }

    @Synchronized
    @Override
    public Ingredient convertDto(IngredientDto dto)
    {
        if (dto == null) return null;

        final UnitOfMeasure uomEntity = uomConverter.convertDto(dto.getUom());
        final Ingredient entity = new Ingredient();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setAmount(dto.getAmount());
        entity.setUom(uomEntity);

        return entity;
    }
}
