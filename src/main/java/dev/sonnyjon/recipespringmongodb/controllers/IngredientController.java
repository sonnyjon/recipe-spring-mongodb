package dev.sonnyjon.recipespringmongodb.controllers;

import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.services.IngredientService;
import dev.sonnyjon.recipespringmongodb.services.RecipeService;
import dev.sonnyjon.recipespringmongodb.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by Sonny on 7/15/2022.
 */
@Slf4j
@Controller
public class IngredientController
{
    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(IngredientService ingredientService,
                                RecipeService recipeService,
                                UnitOfMeasureService unitOfMeasureService)
    {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model)
    {
        log.debug("Getting ingredient list for recipe id: " + recipeId);

        model.addAttribute("recipe", recipeService.findDtoById(recipeId));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model)
    {
        log.debug("Showing ingredients for ingredient id: " + id);

        model.addAttribute("ingredient", ingredientService.findInRecipe(recipeId, id));
        return "recipe/ingredient/show";
    }

    // TODO should this be newIngredient()?

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model)
    {
        // Check for valid recipeId. Throws NotFoundException if invalid.
        recipeService.findDtoById(recipeId);
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setUom(new UnitOfMeasureDto());

        model.addAttribute( "recipeId", recipeId );
        model.addAttribute( "ingredient", ingredientDto );
        model.addAttribute( "uomList",  unitOfMeasureService.listAllUoms() );

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model)
    {
        model.addAttribute("ingredient", ingredientService.findInRecipe(recipeId, id));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@PathVariable String recipeId, @ModelAttribute IngredientDto ingredient)
    {
        IngredientDto savedIngredient = ingredientService.saveIngredient(recipeId, ingredient);
        log.debug("saved ingredient id:" + savedIngredient.getId());

        return String.format("redirect:/recipe/%1$s/ingredient/%2$s/show", recipeId, savedIngredient.getId());
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id)
    {
        log.debug("deleting ingredient id:" + id);
        ingredientService.removeIngredient(recipeId, id);

        return String.format("redirect:/recipe/%s/ingredients", recipeId);
    }
}