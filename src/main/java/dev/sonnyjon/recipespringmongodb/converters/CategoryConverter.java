package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.CategoryDto;
import dev.sonnyjon.recipespringmongodb.model.Category;
import lombok.Synchronized;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sonny on 7/8/2022.
 */
public class CategoryConverter implements DualConverter<Category, CategoryDto>
{
    @Synchronized
    public Set<CategoryDto> convertEntities(Set<Category> entities)
    {
        if (entities == null) return null;

        final Set<CategoryDto> dtos = new HashSet<>();
        entities.forEach((Category entity) -> dtos.add(convertEntity(entity)));

        return dtos;
    }

    @Synchronized
    public Set<Category> convertDtos(Set<CategoryDto> dtos)
    {
        if (dtos == null) return null;

        final Set<Category> entities = new HashSet<>();
        dtos.forEach((CategoryDto dto) -> entities.add(convertDto(dto)));

        return entities;
    }

    @Synchronized
    @Override
    public CategoryDto convertEntity(Category entity)
    {
        if (entity == null) return null;

        final CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    @Synchronized
    @Override
    public Category convertDto(CategoryDto dto)
    {
        if (dto == null) return null;

        final Category entity = new Category();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
