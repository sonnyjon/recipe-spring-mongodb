package dev.sonnyjon.recipespringmongodb.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by Sonny on 7/24/2022.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class IngredientServiceIT
{
//    @Autowired
//    IngredientService ingredientService;
//    @Autowired
//    RecipeService recipeService;
//    Recipe testRecipe;
//    Ingredient testIngredient;
//
//    @BeforeEach
//    void setUp()
//    {
//        testRecipe = recipeService.getRecipes()
//                                    .stream()
//                                    .findFirst()
//                                    .orElseThrow(NotFoundException::new);
//
//        testIngredient = testRecipe.getIngredients()
//                                    .stream()
//                                    .findFirst()
//                                    .orElseThrow(NotFoundException::new);
//    }
//
//    @Test
//    void findByRecipe_shouldReturnDto_whenFound()
//    {
//        // when
//        IngredientDto actual = ingredientService.findInRecipe( testRecipe.getId(), testIngredient.getId() );
//
//        // then
//        assertNotNull( testRecipe );
//        assertNotNull( testRecipe.getIngredients() );
//    }
}