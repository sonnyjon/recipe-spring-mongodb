package dev.sonnyjon.recipespringmongodb.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Sonny on 7/9/2022.
 */
public interface ImageService
{
    void saveImageFile(String recipeId, MultipartFile file);
}
