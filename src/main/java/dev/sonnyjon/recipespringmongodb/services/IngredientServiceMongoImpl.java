package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.IngredientConverter;
import dev.sonnyjon.recipespringmongodb.converters.UnitOfMeasureConverter;
import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by Sonny on 7/19/2022.
 */
@Slf4j
@Service("ingredientService")
public class IngredientServiceMongoImpl implements IngredientService
{
    private final MongoTemplate mongoTemplate;
    private final IngredientConverter converter = new IngredientConverter();
    private final UnitOfMeasureConverter uomConverter = new UnitOfMeasureConverter();

    public IngredientServiceMongoImpl(MongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public IngredientDto findByRecipe(String recipeId, String ingredientId)
    {
        findRecipeForIngredient( recipeId, ingredientId );
        return converter.convertEntity(findIngredient( ingredientId ));
    }

    @Override
    public IngredientDto saveIngredient(String recipeId, IngredientDto dto)
    {
        Recipe recipe;
        Ingredient savedIngredient;

        try {
            findRecipeForIngredient( recipeId, dto.getId() );
            savedIngredient = updateExisting( dto );
        }
        catch (NotFoundException e)
        {
            recipe = findRecipe( recipeId );
            savedIngredient = saveNew( dto, recipe );
        }

        return converter.convertEntity( savedIngredient );
    }

    @Override
    public void removeIngredient(String recipeId, String ingredientId)
    {
        Recipe recipe;

        try {
            recipe = findRecipeForIngredient( recipeId, ingredientId );
        }
        catch (NotFoundException e)
        {
            String msg = String.format("Ingredient [ID: %1$s] not found for Recipe [ID: %2$s].",
                                        ingredientId,
                                        recipeId);
            throw new NotFoundException( msg );
        }

        Ingredient ingredient = findIngredient( ingredientId );
        recipe.getIngredients().remove( ingredient );

        mongoTemplate.save( recipe, "recipe" );
        mongoTemplate.remove( ingredient, "ingredient" );
    }

    //==================================================================================================================
    private Recipe findRecipeForIngredient(String recipeId, String ingredientId)
    {
        Recipe recipe = mongoTemplate.findOne(
            Query.query(Criteria.where("ingredient.id").is( ingredientId )),
            Recipe.class
        );

        if (recipe == null) throw new NotFoundException("Recipe not found. ID: " + recipeId);

        return recipe;
    }

    private Recipe findRecipe(String recipeId)
    {
        Recipe recipe = mongoTemplate.findOne(
                Query.query(Criteria.where("id").is( recipeId )),
                Recipe.class
        );

        if (recipe == null) throw new NotFoundException("Recipe not found. ID: " + recipeId);

        return recipe;
    }

    private Ingredient findIngredient(String ingredientId)
    {
        Ingredient ingredient = mongoTemplate.findOne(
            Query.query(Criteria.where("id").is( ingredientId )),
            Ingredient.class
        );

        if (ingredient == null) throw new NotFoundException("Ingredient not found. ID: " + ingredientId);

        return ingredient;
    }

    private boolean isInRecipe(String ingredientId, String recipeId)
    {
        Recipe foundRecipe = findRecipeForIngredient( recipeId, ingredientId );
        return foundRecipe.getId().equals( recipeId );
    }

    private Ingredient saveNew(IngredientDto newIngredient, Recipe recipe)
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription( newIngredient.getDescription() );
        ingredient.setAmount( newIngredient.getAmount() );
        ingredient.setUom(uomConverter.convertDto( newIngredient.getUom() ));

        Ingredient saved = mongoTemplate.save( ingredient, "ingredient" );

        recipe.addIngredient( saved );
        mongoTemplate.save( recipe, "recipe" );

        return saved;
    }

    private Ingredient updateExisting(IngredientDto existing)
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setId( existing.getId() );
        ingredient.setDescription( existing.getDescription() );
        ingredient.setAmount( existing.getAmount() );
        ingredient.setUom(uomConverter.convertDto( existing.getUom() ));

        return mongoTemplate.save( ingredient, "ingredient" );
    }
}
