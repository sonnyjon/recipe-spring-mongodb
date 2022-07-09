package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.CategoryDto;
import dev.sonnyjon.recipespringmongodb.model.Category;
import lombok.Synchronized;

/**
 * Created by Sonny on 7/8/2022.
 */
public class CategoryConverter implements BiConverter<Category, CategoryDto>
{
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
