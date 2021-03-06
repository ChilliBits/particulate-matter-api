/*
 * Copyright © Marc Auberer 2019-2021. All rights reserved
 */

package com.chillibits.particulatematterapi.controller.v1;

import com.chillibits.particulatematterapi.exception.ErrorCode;
import com.chillibits.particulatematterapi.exception.exception.DataAccessException;
import com.chillibits.particulatematterapi.model.dto.DataRecordCompressedDto;
import com.chillibits.particulatematterapi.model.dto.DataRecordDto;
import com.chillibits.particulatematterapi.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Data endpoint
 *
 * Main endpoint for the read accesses of the measurement data (write access via the push endpoint).
 */
@RestController
@Slf4j
@Api(value = "Data REST Endpoint", tags = "data")
public class DataController {

    @Autowired
    private DataService dataService;

    // ------------------------------------------- Data for single sensor ----------------------------------------------

    /**
     * Returns data records for a specific sensor
     *
     * @param chipId Chip-ID of the requested sensor
     * @param from Begin of the requested time range (unix timestamp in milliseconds)
     * @param to End of the requested time range (unix timestamp in milliseconds)
     * @return List of data records as List of DataRecordDto
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/{chipId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns data records for a specific sensor")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<DataRecordDto> getDataRecords(
        @PathVariable long chipId,
        @RequestParam(defaultValue = "0") long from,
        @RequestParam(defaultValue = "0") long to
    ) {
        return dataService.getDataRecords(chipId, from, to);
    }

    /**
     * Returns data records for a specific sensor in a compressed form
     *
     * @param chipId Chip-ID of the requested sensor
     * @param from Begin of the requested time range (unix timestamp in milliseconds)
     * @param to End of the requested time range (unix timestamp in milliseconds)
     * @return List of data records as List of DataRecordCompressedDto
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/{chipId}", params = "compressed")
    @ApiOperation(value = "Returns data records for a specific sensor in a compressed form")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<DataRecordCompressedDto> getDataRecordsCompressed(
        @PathVariable long chipId,
        @RequestParam(defaultValue = "0") long from,
        @RequestParam(defaultValue = "0") long to
    ) {
        return dataService.getDataRecordsCompressed(chipId, from, to);
    }

    /**
     * Returns the latest data record for a specific sensor
     *
     * @param chipId Chip-ID of the requested sensor
     * @return Data record as DataRecordDto, which has the average values in it
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/{chipId}/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the latest data record for a specific sensor")
    public DataRecordDto getLatestDataRecord(@PathVariable long chipId) {
        return dataService.getLatestDataRecord(chipId);
    }

    /**
     * Returns all data records for a specific sensor (should only be used for archiving purposes)
     *
     * @param chipId Chip-ID of the requested sensor
     * @return List of data records as List of DataRecordCompressedDto
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/{chipId}/all", produces = MediaType.APPLICATION_JSON_VALUE, params = "compressed")
    @ApiOperation(value = "Returns all data records for a specific sensor", hidden = true)
    public List<DataRecordCompressedDto> getAllDataRecordsCompressed(@PathVariable long chipId) {
        return dataService.getAllDataRecordsCompressed(chipId);
    }

    // ----------------------------------------------- Data for multiple sensors ---------------------------------------

    /**
     * Returns a record with the averages of the latest values of the specified sensors
     *
     * @param chipIds Array of all requested Chip-Ids
     * @return Data record as DataRecordDto, which has the average values in it
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/average", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns a record with the averages of the latest values of the specified sensors")
    public DataRecordDto getDataAverageMultipleSensors(@RequestParam Long[] chipIds) {
        return dataService.getDataAverageMultipleSensors(Arrays.asList(chipIds));
    }

    // --------------------------------------------------- Data for country --------------------------------------------

    /**
     * Returns all data records from sensors in a specific country
     *
     * @param country Name of the requested country
     * @param from Begin of the requested time range (unix timestamp in milliseconds)
     * @param to End of the requested time range (unix timestamp in milliseconds)
     * @return List of data records as List of DataRecordCompressedDto
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/country/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all data records from sensors in a specific country")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<DataRecordCompressedDto> getDataCountry(
        @PathVariable String country,
        @RequestParam(defaultValue = "0") long from,
        @RequestParam(defaultValue = "0") long to
    ) {
        return dataService.getDataCountry(country, from, to);
    }

    /**
     * Returns the average of all latest data records from all sensors within a specific country
     *
     * @param country Name of the requested country
     * @return Data record as DataRecordDto, which has the average values in it
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/country/{country}/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the average of all latest data records from all sensors within a specific country")
    public DataRecordDto getDataCountryLatest(
        @PathVariable String country
    ) {
        return dataService.getDataCountryLatest(country);
    }

    // ---------------------------------------------------- Data for city ----------------------------------------------

    /**
     * Returns all data records from sensors in a specific city
     *
     * @param country Name of the requested country
     * @param city Name of the requested city
     * @param from Begin of the requested time range (unix timestamp in milliseconds)
     * @param to End of the requested time range (unix timestamp in milliseconds)
     * @return List of data records as List of DataRecordCompressedDto
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/city/{country}/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all data records from sensors in a specific city")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<DataRecordCompressedDto> getDataCity(
        @PathVariable String country,
        @PathVariable String city,
        @RequestParam(defaultValue = "0") long from,
        @RequestParam(defaultValue = "0") long to
    ) {
        return dataService.getDataCity(country, city, from, to);
    }

    /**
     * Returns the average of all latest data records from all sensors within a specific city
     *
     * @param country Name of the requested country
     * @param city Name of the requested city
     * @return Data record as DataRecordDto, which has the average values in it
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/city/{country}/{city}/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the average of all latest data records from all sensors within a specific city")
    public DataRecordDto getDataCityLatest(
        @PathVariable String country,
        @PathVariable String city
    ) {
        return dataService.getDataCityLatest(country, city);
    }

    // --------------------------------------------- Chart data functions ----------------------------------------------

    /**
     * Returns the average of all latest data records from all sensors within a specific city
     *
     * @param chipId Chip-ID of the requested sensor
     * @param from Begin of the requested time range (unix timestamp in milliseconds)
     * @param to End of the requested time range (unix timestamp in milliseconds)
     * @param fieldIndex Index of the field which is needed (e.g. 0 for PM10, 1 for PM2.5, etc.)
     * @param mergeCount How many records will be merged to one (for performance purposes)
     * @return Json string, which can be processed by the chart endpoint
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/chart")
    @ApiOperation(value = "Returns chart ready data", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0"),
            @ApiResponse(code = 406, message = "Invalid merge count. Must be >= 1"),
            @ApiResponse(code = 406, message = "Invalid field index. Please provide a number >= 0. Also make sure, it's not too high.")
    })
    public String getChartData(
        @RequestParam long chipId,
        @RequestParam(defaultValue = "0") long from,
        @RequestParam(defaultValue = "0") long to,
        @RequestParam(defaultValue = "0") int fieldIndex,
        @RequestParam(defaultValue = "1") int mergeCount
    ) {
        long startTimestamp = System.currentTimeMillis();
        List<DataRecordDto> records = dataService.getChartData(chipId, from, to, fieldIndex, mergeCount);
        return chartDataToJson(fieldIndex, startTimestamp, records, 1);
    }

    /**
     *
     * @param country Name of the requested country
     * @param from Begin of the requested time range (unix timestamp in milliseconds)
     * @param to End of the requested time range (unix timestamp in milliseconds)
     * @param fieldIndex Index of the field which is needed (e.g. 0 for PM10, 1 for PM2.5, etc.)
     * @param granularity Granularity in minutes (similar to mergeCount)
     * @return Json string, which can be processed by the chart endpoint
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/chart", params = "country")
    @ApiOperation(value = "Returns chart ready data for a specific country", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0"),
            @ApiResponse(code = 406, message = "Invalid period. Please provide a period >= 1"),
            @ApiResponse(code = 406, message = "Invalid field index. Please provide a number >= 0. Also make sure, it's not too high.")
    })
    public String getChartDataCountry(
            @RequestParam String country,
            @RequestParam(defaultValue = "0") long from,
            @RequestParam(defaultValue = "0") long to,
            @RequestParam(defaultValue = "0") int fieldIndex,
            @RequestParam(defaultValue = "60") int granularity  // in minutes
    ) {
        long startTimestamp = System.currentTimeMillis();
        ImmutablePair<List<DataRecordDto>, Integer> result = dataService.getChartDataCountry(country, from, to, fieldIndex, granularity);
        return chartDataToJson(fieldIndex, startTimestamp, result.left, result.right);
    }

    /**
     *
     * @param country Name of the requested country
     * @param city Name of the requested city
     * @param from Begin of the requested time range (unix timestamp in milliseconds)
     * @param to End of the requested time range (unix timestamp in milliseconds)
     * @param fieldIndex Index of the field which is needed (e.g. 0 for PM10, 1 for PM2.5, etc.)
     * @param granularity Granularity in minutes (similar to mergeCount)
     * @return Json string, which can be processed by the chart endpoint
     */
    @RequestMapping(method = RequestMethod.GET, path = "/data/chart", params = {"country", "city"})
    @ApiOperation(value = "Returns chart ready data for a specific city", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0"),
            @ApiResponse(code = 406, message = "Invalid period. Please provide a period >= 1"),
            @ApiResponse(code = 406, message = "Invalid field index. Please provide a number >= 0. Also make sure, it's not too high.")
    })
    public String getChartDataCity(
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam(defaultValue = "0") long from,
            @RequestParam(defaultValue = "0") long to,
            @RequestParam(defaultValue = "0") int fieldIndex,
            @RequestParam(defaultValue = "60") int granularity  // in minutes
    ) {
        long startTimestamp = System.currentTimeMillis();
        ImmutablePair<List<DataRecordDto>, Integer> result = dataService.getChartDataCity(country, city, from, to, fieldIndex, granularity);
        return chartDataToJson(fieldIndex, startTimestamp, result.left, result.right);
    }

    // ---------------------------------------------- Encoding functions -----------------------------------------------

    private String chartDataToJson(int fieldIndex, long startTimestamp, List<DataRecordDto> records, int sensorCount) throws DataAccessException {
        JSONObject json = new JSONObject();
        // Handle possible errors
        if(!records.isEmpty()) {
            if(fieldIndex >= records.get(0).getSensorDataValues().length) throw new DataAccessException(ErrorCode.INVALID_FIELD_INDEX);
            // Bring the records into json format
            JSONArray jsonValues = new JSONArray();
            records.forEach(record -> {
                try {
                    JSONArray recordObject = new JSONArray();
                    recordObject.put(record.getTimestamp());
                    recordObject.put(record.getSensorDataValues()[fieldIndex].getValue());
                    jsonValues.put(recordObject);
                } catch (Exception ignored) {}
            });
            json.put("values", jsonValues);
            json.put("field", records.get(0).getSensorDataValues()[fieldIndex].getValueType());
        }
        long responseTime = System.currentTimeMillis() - startTimestamp;
        json.put("responseTime", responseTime);
        json.put("sensorCount", sensorCount);
        return json.toString();
    }
}