package dev.sonnyjon.recipespringmongodb.converters;

/**
 * Created by Sonny on 7/8/2022.
 */
public interface BiConverter<T, V>
{
    V convertEntity(T entity);

    T convertDto(V dto);
}
