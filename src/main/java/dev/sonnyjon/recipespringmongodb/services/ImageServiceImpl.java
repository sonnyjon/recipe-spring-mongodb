package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.exceptions.NotFoundException;
import dev.sonnyjon.recipespringmongodb.model.Recipe;
import dev.sonnyjon.recipespringmongodb.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Sonny on 7/9/2022.
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService
{
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository)
    {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(String recipeId, MultipartFile file)
    {
        try {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NotFoundException::new);
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;

            for (byte b : file.getBytes())
            {
                byteObjects[i++] = b;
            }

            recipe.setImage(byteObjects);
            recipeRepository.save(recipe);
        }
        catch (IOException e)
        {
            log.error("Error occurred", e);
            e.printStackTrace();
        }
    }
}
