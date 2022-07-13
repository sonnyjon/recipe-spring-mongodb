package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.UnitOfMeasureConverter;
import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Sonny on 7/12/2022.
 */
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService
{
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureConverter converter = new UnitOfMeasureConverter();

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository)
    {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Set<UnitOfMeasureDto> listAllUoms()
    {
        return StreamSupport
                .stream(unitOfMeasureRepository.findAll().spliterator(), false)
                .map(converter::convertEntity)
                .collect(Collectors.toSet());
    }
}
