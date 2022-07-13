package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;

import java.util.Set;

/**
 * Created by Sonny on 7/9/2022.
 */
public interface UnitOfMeasureService
{
    Set<UnitOfMeasureDto> listAllUoms();
}
