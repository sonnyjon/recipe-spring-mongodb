package dev.sonnyjon.recipespringmongodb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Sonny on 7/7/2022.
 */
@Getter
@Setter
@NoArgsConstructor
public class Ingredient
{
    private String id = UUID.randomUUID().toString();
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

    @Override
    public int hashCode()
    {
        return Objects.hash( id, description, amount, uom );
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        else if (obj == null || getClass() != obj.getClass()) return false;

        Ingredient ing = (Ingredient) obj;
        return Objects.equals( id, ing.id )
                && Objects.equals( description, ing.description )
                && Objects.equals( amount, ing.amount )
                && Objects.equals( uom, ing.uom );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "Ingredient {" );
        sb.append( "id='" ).append( id ).append( '\'' );
        sb.append( ", description='" ).append( description ).append( '\'' );
        sb.append( ", amount='" ).append( amount ).append( '\'' );
        sb.append( ", uom='" ).append( uom ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
