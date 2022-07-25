package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import lombok.Synchronized;

/**
 * Created by Sonny on 7/8/2022.
 */
public class RecipeConverter implements DualConverter<Recipe, RecipeDto>
{
    private final CategoryConverter catConverter = new CategoryConverter();
    private final IngredientConverter ingredConverter = new IngredientConverter();
    private final NotesConverter notesConverter = new NotesConverter();

    @Synchronized
    @Override
    public RecipeDto convertEntity(Recipe entity)
    {
        if (entity == null) return null;

        final RecipeDto dto = new RecipeDto();
        dto.setId( entity.getId() );
        dto.setDescription( entity.getDescription() );
        dto.setPrepTime( entity.getPrepTime() );
        dto.setCookTime( entity.getCookTime() );
        dto.setServings( entity.getServings() );
        dto.setSource( entity.getSource() );
        dto.setUrl( entity.getUrl() );
        dto.setDirections( entity.getDirections() );
        dto.setIngredients(ingredConverter.convertEntities( entity.getIngredients() ));
        dto.setImage( entity.getImage() );
        dto.setDifficulty( entity.getDifficulty() );
        dto.setNotes(notesConverter.convertEntity(entity.getNotes() ));
        dto.setCategories(catConverter.convertEntities( entity.getCategories() ));

        return dto;
    }

    @Synchronized
    @Override
    public Recipe convertDto(RecipeDto dto)
    {
        if (dto == null) return null;

        final Recipe entity = new Recipe();
        entity.setId( dto.getId() );
        entity.setDescription( dto.getDescription() );
        entity.setPrepTime( dto.getPrepTime() );
        entity.setCookTime( dto.getCookTime() );
        entity.setServings( dto.getServings() );
        entity.setSource( dto.getSource() );
        entity.setUrl( dto.getUrl() );
        entity.setDirections( dto.getDirections() );
        entity.setIngredients(ingredConverter.convertDtos( dto.getIngredients() ));
        entity.setImage( dto.getImage() );
        entity.setDifficulty( dto.getDifficulty() );
        entity.setNotes(notesConverter.convertDto( dto.getNotes() ));
        entity.setCategories(catConverter.convertDtos( dto.getCategories() ));

        return entity;
    }
}
