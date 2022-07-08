package dev.sonnyjon.recipespringmongodb.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Created by Sonny on 7/7/2022.
 */
@Getter
@Setter
@Document("category")
public class Category
{
    @Id
    private String id;

    private String description;
    private Set<Recipe> recipes;
}
