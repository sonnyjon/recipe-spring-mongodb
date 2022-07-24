package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.RecipeConverter;
import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Sonny on 7/12/2022.
 */
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService
{
    private final RecipeRepository recipeRepository;
    private final RecipeConverter converter = new RecipeConverter();

    public RecipeServiceImpl(RecipeRepository recipeRepository)
    {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes()
    {
        log.debug("I'm in the service");

        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(String id)
    {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (recipeOptional.isEmpty()) throw new NotFoundException("Recipe Not Found. For ID value: " + id );

        return recipeOptional.get();
    }

    @Override
    public RecipeDto findDtoById(String id)
    {
        return converter.convertEntity(findById(id));
    }

    @Override
    @Transactional
    public RecipeDto saveRecipe(RecipeDto dto)
    {
        Recipe detachedRecipe = converter.convertDto(dto);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());

        return converter.convertEntity(savedRecipe);
    }

    @Override
    @Transactional
    public void deleteById(String idToDelete)
    {
        recipeRepository.deleteById(idToDelete);
    }
}