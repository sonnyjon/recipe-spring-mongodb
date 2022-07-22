package dev.sonnyjon.recipespringmongodb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Created by Sonny on 7/7/2022.
 */
@Getter
@Setter
@NoArgsConstructor
@Document("ingredient")
public class Ingredient
{
    @Id
    private String id;

    private String description;
    private BigDecimal amount;
    @DBRef
    private UnitOfMeasure uom;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom)
    {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }
}
