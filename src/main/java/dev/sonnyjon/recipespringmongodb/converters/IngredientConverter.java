package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;

/**
 * Created by Sonny on 7/8/2022.
 */
public class IngredientConverter implements DualConverter<Ingredient, IngredientDto>
{
    private final UnitOfMeasureConverter uomConverter = new UnitOfMeasureConverter();

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
