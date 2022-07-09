package dev.sonnyjon.recipespringmongodb.converters;

import dev.sonnyjon.recipespringmongodb.dto.IngredientDto;
import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.model.Ingredient;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by Sonny on 7/8/2022.
 */
class IngredientConverterTest
{
    public static final String INGRED_ID = "INGRED-1";
    public static final String INGRED_DESC = "INGRED_DESC";
    public static final BigDecimal AMOUNT = new BigDecimal(2);
    public static final String UOM_ID = "UOM-1";
    public static final String UOM_DESC = "UOM_DESC";

    IngredientConverter converter;

    @BeforeEach
    void setUp()
    {
        converter = new IngredientConverter();
    }

    @Test
    void convertEntity_shouldReturn_equivDto()
    {
        UnitOfMeasure uomEntity = new UnitOfMeasure();
        uomEntity.setId(UOM_ID);
        uomEntity.setDescription(UOM_DESC);

        Ingredient entity = new Ingredient();
        entity.setId(INGRED_ID);
        entity.setDescription(INGRED_DESC);
        entity.setAmount(AMOUNT);
        entity.setUom(uomEntity);

        IngredientDto dto = converter.convertEntity(entity);

        assertEquals(INGRED_ID, dto.getId());
        assertEquals(INGRED_DESC, dto.getDescription());
        assertEquals(AMOUNT, dto.getAmount());
        assertEquals(UOM_ID, dto.getUom().getId());
        assertEquals(UOM_DESC, dto.getUom().getDescription());
    }

    @Test
    void convertDto_shouldReturn_equivEntity()
    {
        UnitOfMeasureDto uomDto = new UnitOfMeasureDto();
        uomDto.setId(UOM_ID);
        uomDto.setDescription(UOM_DESC);

        IngredientDto dto = new IngredientDto();
        dto.setId(INGRED_ID);
        dto.setDescription(INGRED_DESC);
        dto.setAmount(AMOUNT);
        dto.setUom(uomDto);

        Ingredient entity = converter.convertDto(dto);

        assertEquals(INGRED_ID, entity.getId());
        assertEquals(INGRED_DESC, entity.getDescription());
        assertEquals(AMOUNT, entity.getAmount());
        assertEquals(UOM_ID, entity.getUom().getId());
        assertEquals(UOM_DESC, entity.getUom().getDescription());
    }

    @Test
    void nullEntity_shouldReturn_null()
    {
        IngredientDto dto = converter.convertEntity(null);
        assertNull(dto);
    }

    @Test
    void nullDto_shouldReturn_null()
    {
        Ingredient entity = converter.convertDto(null);
        assertNull(entity);
    }
}