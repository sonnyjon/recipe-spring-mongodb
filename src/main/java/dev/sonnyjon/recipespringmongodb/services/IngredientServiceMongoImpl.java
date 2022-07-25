package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.IngredientConverter;
import dev.sonnyjon.recipespringmongodb.converters.UnitOfMeasureConverter;
import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sonny on 7/19/2022.
 */
@Slf4j
@Service("ingredientService")
public class IngredientServiceMongoImpl implements IngredientService
{
    private final RecipeRepository recipeRepository;
    private final IngredientConverter converter = new IngredientConverter();
    private final UnitOfMeasureConverter uomConverter = new UnitOfMeasureConverter();

    public IngredientServiceMongoImpl(RecipeRepository recipeRepository)
    {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientDto findInRecipe(String recipeId, String ingredientId)
    {
        Recipe recipe = findRecipeForIngredient( ingredientId );
        return converter.convertEntity(findIngredient( recipe, ingredientId ));
    }

    @Override
    @Transactional
    public IngredientDto saveIngredient(String recipeId, IngredientDto dto)
    {
        Recipe recipe;
        Ingredient savedIngredient;

        try {
            recipe = findRecipeForIngredient( dto.getId() );
            savedIngredient = updateExisting( dto, recipe );
        }
        catch (NotFoundException e)
        {
            recipe = findRecipe( recipeId );
            savedIngredient = saveNew( dto, recipe );
        }

        return converter.convertEntity( savedIngredient );
    }

    @Override
    @Transactional
    public void removeIngredient(String recipeId, String ingredientId)
    {
        Recipe recipe;

        try {
            recipe = findRecipeForIngredient( ingredientId );
        }
        catch (NotFoundException e)
        {
            String msg = String.format("Ingredient [ID: %1$s] not found for Recipe [ID: %2$s].",
                                        ingredientId,
                                        recipeId);
            throw new NotFoundException( msg );
        }

        Ingredient ingredient = findIngredient( recipe, ingredientId );
        recipe.getIngredients().remove( ingredient );

        recipeRepository.save( recipe );
    }

    //==================================================================================================================
    private Recipe findRecipeForIngredient(String ingredientId)
    {
        Recipe recipe = recipeRepository.findByIngredientId( ingredientId ).get(0);

        if (recipe == null)
            throw new NotFoundException("Recipe not found for Ingredient ID=" + ingredientId);

        return recipe;
    }

    private Recipe findRecipe(String recipeId)
    {
        return recipeRepository.findById( recipeId )
                                .orElseThrow(
                                    () -> new NotFoundException("Recipe not found. ID: " + recipeId)
                                );
    }

    private Ingredient findIngredient(Recipe recipe, String ingredientId)
    {
        return recipe.getIngredients()
                        .stream()
                        .filter(
                            ingredient -> ingredient.getId().equals( ingredientId )
                        )
                        .findFirst()
                        .orElseThrow(
                            ()-> new NotFoundException("Ingredient not found. ID: " + ingredientId)
                        );
    }

    private Ingredient findIngredientByDescription(Recipe recipe, String ingredientDesc)
    {
        return recipe.getIngredients()
                        .stream()
                        .filter(
                            ingredient -> ingredient.getDescription().equals( ingredientDesc )
                        )
                        .findFirst()
                        .orElseThrow(
                            ()-> new NotFoundException("Ingredient not found. DESC: " + ingredientDesc)
                        );
    }

    private Ingredient saveNew(IngredientDto newIngredient, Recipe recipe)
    {
        Ingredient ingredient = converter.convertDto( newIngredient );
        recipe.addIngredient( ingredient );
        Recipe savedRecipe = recipeRepository.save( recipe );

        return findIngredientByDescription( savedRecipe, ingredient.getDescription() );
    }

    private Ingredient updateExisting(IngredientDto existing, Recipe recipe)
    {
        Ingredient ingredient = converter.convertDto( existing );
        Recipe savedRecipe = recipeRepository.save( recipe );

        return findIngredient( savedRecipe, ingredient.getId() );
    }
}
