package dev.sonnyjon.recipespringmongodb.controllers;

import dev.sonnyjon.recipespringmongodb.dto.RecipeDto;
import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.services.ImageService;
import dev.sonnyjon.recipespringmongodb.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Sonny on 7/14/2022.
 */
@ExtendWith(MockitoExtension.class)
class ImageControllerTest
{
    @Mock
    ImageService imageService;
    @Mock
    RecipeService recipeService;

    ImageController controller;
    MockMvc mockMvc;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    public void showForm_shouldReturnFormUri_whenRecipeFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/image", RECIPE_ID);
        final String EXPECTED_RETURN = "recipe/imageuploadform";

        // given
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId( RECIPE_ID );

        when(recipeService.findDtoById(anyString())).thenReturn(recipeDto);

        // when
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(forwardedUrl( EXPECTED_RETURN ));

        // then
        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void showForm_shouldThrowException_whenRecipeNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/image", RECIPE_ID);

        // given
        when(recipeService.findDtoById(anyString())).thenThrow(NotFoundException.class);

        // when
        mockMvc.perform(get( TEST_URI ))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        // then
        verify(recipeService, times(1)).findDtoById(anyString());
    }

    @Test
    public void handleImage_shouldRedirectToShow_whenRecipeFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String EXPECTED_URI = String.format("/recipe/%s/show", RECIPE_ID);

        // given
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        // when
        mockMvc.perform(multipart("/recipe/{id}/image", RECIPE_ID).file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", EXPECTED_URI));

        // then
        verify(imageService, times(1)).saveImageFile(anyString(), any());
    }

    @Test
    public void handleImage_shouldThrowException_whenRecipeNotFound() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";

        // given
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        doThrow(NotFoundException.class).when(imageService).saveImageFile(anyString(), any());

        // when
        mockMvc.perform(multipart("/recipe/{id}/image", RECIPE_ID).file(multipartFile))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

        // then
        verify(imageService, times(1)).saveImageFile(anyString(), any());
    }

    @Test
    public void renderImage_shouldStream_ImageBytes() throws Exception
    {
        final String RECIPE_ID = "RECIPE-1";
        final String TEST_URI = String.format("/recipe/%s/recipeimage", RECIPE_ID);

        // given
        String sub = "fake image text";
        Byte[] bytesBoxed = new Byte[sub.getBytes().length];

        int i = 0;
        for (byte primByte : sub.getBytes())
        {
            bytesBoxed[i++] = primByte;
        }

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId( RECIPE_ID );
        recipeDto.setImage(bytesBoxed);

        when(recipeService.findDtoById(anyString())).thenReturn(recipeDto);

        // when
        MockHttpServletResponse response = mockMvc.perform(get( TEST_URI ))
                                                    .andExpect(status().isOk())
                                                    .andReturn().getResponse();

        byte[] reponseBytes = response.getContentAsByteArray();
        String contentType = response.getContentType();

        // then
        assertEquals(sub.getBytes().length, reponseBytes.length);
        assertEquals("image/jpeg", contentType);
    }
}