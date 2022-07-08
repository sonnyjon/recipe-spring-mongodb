package dev.sonnyjon.recipespringmongodb.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sonny on 7/7/2022.
 */
@Getter
@Setter
@Document("unitOfMeasure")
public class UnitOfMeasure
{
    @Id
    private String id;

    private String description;
}
