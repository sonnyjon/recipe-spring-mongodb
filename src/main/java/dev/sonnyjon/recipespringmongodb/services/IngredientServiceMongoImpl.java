package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.IngredientConverter;
import dev.sonnyjon.recipespringmongodb.converters.UnitOfMeasureConverter;
import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.repositories.IngredientRepository;
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
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientConverter converter = new IngredientConverter();
    private final UnitOfMeasureConverter uomConverter = new UnitOfMeasureConverter();

    public IngredientServiceMongoImpl(IngredientRepository ingredientRepository, RecipeRepository recipeRepository)
    {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientDto findByRecipe(String recipeId, String ingredientId)
    {
        findRecipeForIngredient( ingredientId );
        return converter.convertEntity(findIngredient( ingredientId ));
    }

    @Override
    @Transactional
    public IngredientDto saveIngredient(String recipeId, IngredientDto dto)
    {
        Recipe recipe;
        Ingredient savedIngredient;

        try {
            findRecipeForIngredient( dto.getId() );
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

        Ingredient ingredient = findIngredient( ingredientId );
        recipe.getIngredients().remove( ingredient );

        recipeRepository.save( recipe );
        ingredientRepository.delete( ingredient );
    }

    //==================================================================================================================
    private Recipe findRecipeForIngredient(String ingredientId)
    {
        Recipe recipe = recipeRepository.findByIngredientId( ingredientId );

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

    private Ingredient findIngredient(String ingredientId)
    {
        return ingredientRepository.findById( ingredientId )
                                    .orElseThrow(
                                        () -> new NotFoundException("Ingredient not found. ID: " + ingredientId)
                                    );
    }

    private Ingredient saveNew(IngredientDto newIngredient, Recipe recipe)
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription( newIngredient.getDescription() );
        ingredient.setAmount( newIngredient.getAmount() );
        ingredient.setUom(uomConverter.convertDto( newIngredient.getUom() ));

        Ingredient saved = ingredientRepository.save( ingredient );

        recipe.addIngredient( saved );
        recipeRepository.save( recipe );

        return saved;
    }

    private Ingredient updateExisting(IngredientDto existing)
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setId( existing.getId() );
        ingredient.setDescription( existing.getDescription() );
        ingredient.setAmount( existing.getAmount() );
        ingredient.setUom(uomConverter.convertDto( existing.getUom() ));

        return ingredientRepository.save( ingredient );
    }
}
