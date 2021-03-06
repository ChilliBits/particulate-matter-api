/*
 * Copyright © Marc Auberer 2019-2021. All rights reserved
 */

package com.chillibits.particulatematterapi.controller.v1;

import com.chillibits.particulatematterapi.exception.ErrorCode;
import com.chillibits.particulatematterapi.exception.exception.RankingDataException;
import com.chillibits.particulatematterapi.model.dto.RankingItemCityCompressedDto;
import com.chillibits.particulatematterapi.model.dto.RankingItemCityDto;
import com.chillibits.particulatematterapi.model.dto.RankingItemCountryCompressedDto;
import com.chillibits.particulatematterapi.model.dto.RankingItemCountryDto;
import com.chillibits.particulatematterapi.repository.SensorRepository;
import com.chillibits.particulatematterapi.service.RankingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("logging")
@DisplayName("Ranking Controller")
public class RankingControllerTests {

    @Autowired
    private RankingController rankingController;
    @MockBean
    private SensorRepository sensorRepository;

    private final List<RankingItemCityDto> testDataCity = getTestDataCity();
    private final List<RankingItemCountryDto> testDataCountry = getTestDataCountry();
    private final List<RankingItemCityCompressedDto> assertDataCity = getAssertDataCity();
    private final List<RankingItemCountryCompressedDto> assertDataCountry = getAssertDataCountry();

    @TestConfiguration
    static class RankingControllerImplTestContextConfiguration {

        @Bean
        public RankingController rankingController() {
            return new RankingController();
        }

        @Bean
        public RankingService rankingService() {
            return new RankingService();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Before
    public void init() {
        // Setup fake method calls
        when(sensorRepository.getRankingByCity(anyInt())).thenReturn(testDataCity);
        when(sensorRepository.getRankingByCountry(anyInt())).thenReturn(testDataCountry);
    }

    // ----------------------------------------------- Get city ranking ------------------------------------------------

    @Test
    public void getCityRankingSuccessfully() throws RankingDataException {
        List<RankingItemCityDto> result = rankingController.getRankingByCity(5);
        assertThat(result).containsExactlyInAnyOrder(testDataCity.toArray(RankingItemCityDto[]::new));
    }

    @Test
    public void getCityRankingInvalidItemsNumber() {
        // Try with invalid input
        Exception exception = assertThrows(RankingDataException.class, () ->
                rankingController.getRankingByCity(-1)
        );

        String expectedMessage = new RankingDataException(ErrorCode.INVALID_ITEMS_NUMBER).getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void getCityRankingCompressedSuccessfully() throws RankingDataException {
        List<RankingItemCityCompressedDto> result = rankingController.getRankingByCityCompressed(5);
        assertThat(result).containsExactlyInAnyOrder(assertDataCity.toArray(RankingItemCityCompressedDto[]::new));
    }

    @Test
    public void getCityRankingCompressedInvalidItemsNumber() {
        // Try with invalid input
        Exception exception = assertThrows(RankingDataException.class, () ->
                rankingController.getRankingByCityCompressed(-1)
        );

        String expectedMessage = new RankingDataException(ErrorCode.INVALID_ITEMS_NUMBER).getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    // --------------------------------------------- Get country ranking -----------------------------------------------

    @Test
    public void getCountryRankingSuccessfully() throws RankingDataException {
        List<RankingItemCountryDto> result = rankingController.getRankingByCountry(5);
        assertThat(result).containsExactlyInAnyOrder(testDataCountry.toArray(RankingItemCountryDto[]::new));
    }

    @Test
    public void getCountryRankingInvalidItemsNumber() {
        // Try with invalid input
        Exception exception = assertThrows(RankingDataException.class, () ->
                rankingController.getRankingByCountry(-1)
        );

        String expectedMessage = new RankingDataException(ErrorCode.INVALID_ITEMS_NUMBER).getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void getCountryRankingCompressedSuccessfully() throws RankingDataException {
        List<RankingItemCountryCompressedDto> result = rankingController.getRankingByCountryCompressed(5);
        assertThat(result).containsExactlyInAnyOrder(assertDataCountry.toArray(RankingItemCountryCompressedDto[]::new));
    }

    @Test
    public void getCountryRankingCompressedInvalidItemsNumber() {
        // Try with invalid input
        Exception exception = assertThrows(RankingDataException.class, () ->
                rankingController.getRankingByCountryCompressed(-1)
        );

        String expectedMessage = new RankingDataException(ErrorCode.INVALID_ITEMS_NUMBER).getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    // -------------------------------------------------- Test data ----------------------------------------------------

    private List<RankingItemCityDto> getTestDataCity() {
        // Create ranking item objects
        RankingItemCityDto r1 = new RankingItemCityDto("Russia", "Moskva", 55);
        RankingItemCityDto r2 = new RankingItemCityDto("Germany", "Berlin", 42);
        RankingItemCityDto r3 = new RankingItemCityDto("Germany", "Hamburg", 22);
        RankingItemCityDto r4 = new RankingItemCityDto("Germany", "München", 22);
        RankingItemCityDto r5 = new RankingItemCityDto("Italy", "Parma", 22);

        // Add them to test data
        return Arrays.asList(r1, r2, r3, r4, r5);
    }

    private List<RankingItemCountryDto> getTestDataCountry() {
        // Create ranking item objects
        RankingItemCountryDto r1 = new RankingItemCountryDto("Germany", 722);
        RankingItemCountryDto r2 = new RankingItemCountryDto("Poland", 203);
        RankingItemCountryDto r3 = new RankingItemCountryDto("Italy", 149);
        RankingItemCountryDto r4 = new RankingItemCountryDto("Russia", 124);
        RankingItemCountryDto r5 = new RankingItemCountryDto("Netherlands", 84);

        // Add them to test data
        return Arrays.asList(r1, r2, r3, r4, r5);
    }

    private List<RankingItemCityCompressedDto> getAssertDataCity() {
        // Create ranking item objects
        RankingItemCityCompressedDto r1 = new RankingItemCityCompressedDto("Russia", "Moskva", 55);
        RankingItemCityCompressedDto r2 = new RankingItemCityCompressedDto("Germany", "Berlin", 42);
        RankingItemCityCompressedDto r3 = new RankingItemCityCompressedDto("Germany", "Hamburg", 22);
        RankingItemCityCompressedDto r4 = new RankingItemCityCompressedDto("Germany", "München", 22);
        RankingItemCityCompressedDto r5 = new RankingItemCityCompressedDto("Italy", "Parma", 22);

        // Add them to test data
        return Arrays.asList(r1, r2, r3, r4, r5);
    }

    private List<RankingItemCountryCompressedDto> getAssertDataCountry() {
        // Create ranking item objects
        RankingItemCountryCompressedDto r1 = new RankingItemCountryCompressedDto("Germany", 722);
        RankingItemCountryCompressedDto r2 = new RankingItemCountryCompressedDto("Poland", 203);
        RankingItemCountryCompressedDto r3 = new RankingItemCountryCompressedDto("Italy", 149);
        RankingItemCountryCompressedDto r4 = new RankingItemCountryCompressedDto("Russia", 124);
        RankingItemCountryCompressedDto r5 = new RankingItemCountryCompressedDto("Netherlands", 84);

        // Add them to test data
        return Arrays.asList(r1, r2, r3, r4, r5);
    }
}