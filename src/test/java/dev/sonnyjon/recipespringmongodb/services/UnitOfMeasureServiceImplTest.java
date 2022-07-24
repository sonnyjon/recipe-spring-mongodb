package dev.sonnyjon.recipespringmongodb.services;

import dev.sonnyjon.recipespringmongodb.converters.UnitOfMeasureConverter;
import dev.sonnyjon.recipespringmongodb.dto.UnitOfMeasureDto;
import dev.sonnyjon.recipespringmongodb.model.UnitOfMeasure;
import dev.sonnyjon.recipespringmongodb.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Sonny on 7/12/2022.
 */
@ExtendWith(SpringExtension.class)
class UnitOfMeasureServiceImplTest
{
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    UnitOfMeasureService service;
    UnitOfMeasureConverter converter;
    AutoCloseable mocks;

    @BeforeEach
    void setUp()
    {
        mocks = MockitoAnnotations.openMocks(this);
        service = new UnitOfMeasureServiceImpl(unitOfMeasureRepository);
        converter = new UnitOfMeasureConverter();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        mocks.close();
    }

    @Test
    void listAllUoms_should_ReturnAllUoms()
    {
        // given
        List<UnitOfMeasure> uoms = new ArrayList<>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId("UOM-1");

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId("UOM-2");

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setId("UOM-3");

        uoms.add(uom1);
        uoms.add(uom2);
        uoms.add(uom3);

        when(unitOfMeasureRepository.findAll()).thenReturn(uoms);

        // when
        Set<UnitOfMeasureDto> actual = service.listAllUoms();

        // then
        assertEquals(3, actual.size());
    }
}