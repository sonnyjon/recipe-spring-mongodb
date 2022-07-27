package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Sonny on 7/17/2022.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class ImageServiceIT
{
    public static final String RECIPE_ID = "TEST-RECIPE";
    public static final String RECIPE_DESC = "TEST-RECIPE-DESC";

    @Autowired
    RecipeService recipeService;
    @Autowired
    ImageService imageService;
    @Mock
    MockMultipartFile mockFile;

    @BeforeEach
    void setUp()
    {
        mockFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                "Spring Framework Guru".getBytes());
    }

    @Test
    void saveImageFile_shouldSaveImage_toDB() throws Exception
    {
        // given
        final byte[] expectedBytes = mockFile.getBytes();
        final Recipe expectedRecipe = getTestRecipe();

        // when
        imageService.saveImageFile(RECIPE_ID, mockFile);
        Recipe actualRecipe = recipeService.findById( RECIPE_ID );

        // then
        assertEquals(expectedRecipe.getId(), actualRecipe.getId());
        assertEquals(expectedBytes.length, actualRecipe.getImage().length);
    }

    private Recipe getTestRecipe()
    {
        RecipeDto recipe = new RecipeDto();
        recipe.setId( RECIPE_ID );
        recipe.setDescription( RECIPE_DESC );
        recipeService.saveRecipe(recipe);

        return recipeService.findById( RECIPE_ID );
    }
}