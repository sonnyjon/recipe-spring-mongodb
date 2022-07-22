package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.IngredientConverter;
import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by Sonny on 7/20/2022.
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
class IngredientServiceMongoImplTest
{
    public static final String RECIPE_ID = "RECIPE-1";
    public static final String INGRED1_ID = "INGRED1-ID";
    public static final String INGRED2_ID = "INGRED2-ID";
    public static final BigDecimal AMOUNT = new BigDecimal(2);
    public static final String UOM_ID = "UOM-1";

    @Mock
    MongoTemplate mongoTemplate;

    IngredientService ingredientService;
    IngredientConverter converter;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceMongoImpl( mongoTemplate );
        converter = new IngredientConverter();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    void findByRecipe_shouldReturnDto_whenFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED1_ID );

        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenReturn( testRecipe );
        when(mongoTemplate.findOne(any(), eq(Ingredient.class))).thenReturn( testIngredient );

        // when
        IngredientDto expected = converter.convertEntity( testIngredient );
        IngredientDto actual = ingredientService.findByRecipe( testRecipe.getId(), expected.getId() );

        // then
        assertEquals( expected.getDescription(), actual.getDescription() );
        assertEquals( expected.getAmount(), actual.getAmount() );
        assertEquals( expected.getUom().getId(), actual.getUom().getId() );
        verify(mongoTemplate, times(1)).findOne(any(), eq(Recipe.class));
        verify(mongoTemplate, times(1)).findOne(any(), eq(Ingredient.class));
    }

    @Test
    void findByRecipe_shouldThrowException_whenRecipeNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED1_ID );
        IngredientDto expected = converter.convertEntity( testIngredient );

        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenThrow( NotFoundException.class );
        when(mongoTemplate.findOne(any(), eq(Ingredient.class))).thenReturn( testIngredient );

        // when
        Executable executable = () -> ingredientService.findByRecipe( testRecipe.getId(), expected.getId() );

        // then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void findByRecipe_shouldThrowException_whenIngredientNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED1_ID );
        IngredientDto expected = converter.convertEntity( testIngredient );

        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenReturn( testRecipe );
        when(mongoTemplate.findOne(any(), eq(Ingredient.class))).thenThrow( NotFoundException.class );

        // when
        Executable executable = () -> ingredientService.findByRecipe( testRecipe.getId(), expected.getId() );

        // then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void saveIngredient_shouldAdd_NewIngredient()
    {
        // given
        Recipe testRecipe = getTestRecipeWithOneIngredient();
        Ingredient expectedIngredient = getTestIngredient( INGRED2_ID );
        IngredientDto testIngredient = converter.convertEntity( expectedIngredient );

        testIngredient.setId( null );

        when(mongoTemplate.save(any(), anyString())).thenReturn( expectedIngredient );
        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenReturn( testRecipe, null );

        // when
        IngredientDto actualDto = ingredientService.saveIngredient( testRecipe.getId(), testIngredient );

        // then
        assertNotNull( actualDto.getId() );
        verify(mongoTemplate, times(1)).findOne(any(), eq(Recipe.class));
    }

    @Test
    void saveIngredient_shouldUpdate_ExistingIngredient()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient expectedIngredient = getTestIngredient( INGRED1_ID );
        IngredientDto testIngredient = converter.convertEntity( expectedIngredient );

        when(mongoTemplate.save(any(), anyString())).thenReturn( expectedIngredient );
        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenReturn( testRecipe );

        // when
        IngredientDto actualDto = ingredientService.saveIngredient( testRecipe.getId(), testIngredient );

        // then
        assertEquals( expectedIngredient.getId(), actualDto.getId() );
        verify(mongoTemplate, times(1)).findOne(any(), eq(Recipe.class));
    }

    @Test
    void saveIngredient_shouldThrowException_whenRecipeNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient expected = getTestIngredient( INGRED1_ID );
        IngredientDto testIngredient = converter.convertEntity( expected );

        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenThrow( NotFoundException.class );

        // when
        Executable executable = () -> ingredientService.saveIngredient( testRecipe.getId(), testIngredient );

        // then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void removeIngredient_shouldRemoveIngredient_fromRecipe()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED2_ID );

        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenReturn( testRecipe );
        when(mongoTemplate.findOne(any(), eq(Ingredient.class))).thenReturn( testIngredient );
        when(mongoTemplate.save(any(), anyString())).thenReturn( null );
        when(mongoTemplate.remove(any(), anyString())).thenReturn( null );

        assertEquals(2, testRecipe.getIngredients().size());

        // when
        ingredientService.removeIngredient( testRecipe.getId(), testIngredient.getId() );

        // then
        verify(mongoTemplate, times(1)).findOne(any(), eq(Recipe.class));
    }

    @Test
    void removeIngredient_shouldThrowException_whenRecipeNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED1_ID );

        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenThrow( NotFoundException.class );

        // when
        Executable executable = () -> ingredientService.removeIngredient( testRecipe.getId(), testIngredient.getId() );

        // then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void removeIngredient_shouldThrowException_whenIngredientNotFound()
    {
        // given
        Recipe testRecipe = getTestRecipeWithTwoIngredients();
        Ingredient testIngredient = getTestIngredient( INGRED1_ID );

        when(mongoTemplate.findOne(any(), eq(Recipe.class))).thenReturn( testRecipe );
        when(mongoTemplate.findOne(any(), eq(Ingredient.class))).thenThrow( NotFoundException.class );

        // when
        Executable executable = () -> ingredientService.removeIngredient( testRecipe.getId(), testIngredient.getId() );

        // then
        assertThrows(NotFoundException.class, executable);
    }

    //==================================================================================================================
    private Recipe getTestRecipeWithOneIngredient()
    {
        Recipe recipe = new Recipe();
        recipe.setId( RECIPE_ID );
        recipe.addIngredient(getTestIngredient( INGRED1_ID ));

        return recipe;
    }

    private Recipe getTestRecipeWithTwoIngredients()
    {
        Recipe recipe = getTestRecipeWithOneIngredient();
        recipe.addIngredient(getTestIngredient( INGRED2_ID ));

        return recipe;
    }

    private Ingredient getTestIngredient(String ingredientId )
    {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId( UOM_ID );

        Ingredient ingredient = new Ingredient();
        ingredient.setId( ingredientId );
        ingredient.setAmount( AMOUNT );
        ingredient.setUom( uom );

        return ingredient;
    }
}