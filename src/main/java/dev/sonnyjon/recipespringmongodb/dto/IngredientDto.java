package dev.sonnyjon.recipespringmongodb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Sonny on 7/8/2022.
 */
@Getter
@Setter
@NoArgsConstructor
public class IngredientDto
{
    private String id;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasureDto uom;
}
