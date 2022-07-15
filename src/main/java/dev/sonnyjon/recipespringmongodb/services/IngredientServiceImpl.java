package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.IngredientConverter;
import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import dev.sonnyjon.recipespringmongodb.repositories.RecipeRepository;
import dev.sonnyjon.recipespringmongodb.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Sonny on 7/9/2022.
 */
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService
{
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientConverter converter = new IngredientConverter();

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository)
    {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientDto findByRecipeIdAndIngredientId(String recipeId, String ingredientId)
    {
        Recipe recipe = findRecipe(recipeId);
        Ingredient ingredient = findIngredient(recipe, ingredientId);
        return converter.convertEntity(ingredient);
    }

    @Override
    public IngredientDto saveIngredient(String recipeId, IngredientDto dto)
    {
        Recipe recipe = findRecipe(recipeId);

        if (recipe != null)
        {
            Ingredient ingredient = findIngredient(recipe, dto.getId());

            if (ingredient != null) // update existing Ingredient
            {
                ingredient.setDescription(dto.getDescription());
                ingredient.setAmount(dto.getAmount());
                ingredient.setUom(findUnitOfMeasure(dto.getUom().getId()));
            }
            else // add new Ingredient
            {
                ingredient = converter.convertDto(dto);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);
            Ingredient savedIngredient = findIngredient(savedRecipe, dto.getId());

            if (savedIngredient == null) savedIngredient = findIngredientByDescription(savedRecipe, dto);

            return converter.convertEntity(savedIngredient);
        }
        else
        {
            log.error("Recipe not found for id: " + dto.getId());
            throw new NoSuchElementException("Recipe ID: " + dto.getId());
        }
    }

    @Override
    public boolean deleteById(String recipeId, String idToDelete)
    {
        boolean deleted = false;

        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);
        Recipe recipe = findRecipe(recipeId);

        if (recipe != null)
        {
            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(
                        ingredient -> ingredient.getId().equals(idToDelete)
                    )
                    .findFirst();

            if (ingredientOptional.isPresent())
            {
                log.debug("found Ingredient");
                Ingredient ingredientToDelete = ingredientOptional.get();
                deleted = recipe.getIngredients().remove(ingredientToDelete);
                recipeRepository.save(recipe);
            }
        }
        else log.debug("Recipe Id Not found. Id:" + recipeId);

        return deleted;
    }

    private UnitOfMeasure findUnitOfMeasure(String uomId)
    {
        return unitOfMeasureRepository.findById(uomId)
                                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"));
    }

    private Ingredient findIngredient(Recipe recipe, String ingredientId)
    {
        Optional<Ingredient> optional = recipe.getIngredients()
                                                .stream()
                                                .filter(
                                                        ingredient -> ingredient.getId().equals(ingredientId)
                                                )
                                                .findFirst();

        return optional.orElseThrow(() ->
                new NotFoundException("Ingredient not found for ID: " + ingredientId));
    }

    private Ingredient findIngredientByDescription(Recipe recipe, IngredientDto dto)
    {
        Optional<Ingredient> optional = recipe.getIngredients()
                                                .stream()
                                                .filter(
                                                        ingredient -> ingredient.getDescription()
                                                                                .equals(dto.getDescription())
                                                )
                                                .filter(
                                                        ingredient -> ingredient.getAmount().equals(dto.getAmount())
                                                )
                                                .filter(
                                                        ingredient -> ingredient.getUom()
                                                                                .getId()
                                                                                .equals(dto.getUom().getId())
                                                )
                                                .findFirst();

        return optional.orElse(null);
    }

    private Recipe findRecipe(String recipeId)
    {
        Optional<Recipe> optional = recipeRepository.findById(recipeId);
        return optional.orElseThrow(() ->
                new NotFoundException("Recipe not found for ID: " + recipeId));
    }
}
