package dev.sonnyjon.recipespringmongodb.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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

    @Override
    public int hashCode()
    {
        return Objects.hash( id, description );
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        else if (obj == null || getClass() != obj.getClass()) return false;

        UnitOfMeasure uom = (UnitOfMeasure) obj;
        return Objects.equals( id, uom.id )
                && Objects.equals( description, uom.description );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "UnitOfMeasure {" );
        sb.append( "id='" ).append( id ).append( '\'' );
        sb.append( ", description='" ).append( description ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
