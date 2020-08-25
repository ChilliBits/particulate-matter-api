/*
 * Copyright © Marc Auberer 2019 - 2020. All rights reserved
 */

package com.chillibits.particulatematterapi.controller.v1;

import com.chillibits.particulatematterapi.exception.ErrorCodeUtils;
import com.chillibits.particulatematterapi.exception.exception.LogAccessException;
import com.chillibits.particulatematterapi.model.db.data.LogItem;
import com.chillibits.particulatematterapi.shared.ConstantUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@Api(value = "Log REST Endpoint", tags = "log")
@ApiIgnore
public class LogController {

    @Autowired
    private MongoTemplate template;

    @RequestMapping(method = RequestMethod.GET, path = "/log", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the logs for the specified time span", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<LogItem> getAllLogs(
            @RequestParam(defaultValue = "0") long from,
            @RequestParam(defaultValue = "0") long to
    ) throws LogAccessException {
        validateRequest(from, to);
        long toTimestamp = to == 0 ? System.currentTimeMillis() : to;
        long fromTimestamp = from == 0 ? toTimestamp - ConstantUtils.DEFAULT_DATA_TIME_SPAN : from;

        return template.find(Query.query(Criteria.where("timestamp").gte(fromTimestamp).lte(toTimestamp)), LogItem.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/log/target/{target}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the logs for the specified time span, filtered by target", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<LogItem> getLogsByTarget(
            @PathVariable String target,
            @RequestParam(defaultValue = "0") long from,
            @RequestParam(defaultValue = "0") long to
    ) throws LogAccessException {
        validateRequest(from, to);
        long toTimestamp = to == 0 ? System.currentTimeMillis() : to;
        long fromTimestamp = from == 0 ? toTimestamp - ConstantUtils.DEFAULT_DATA_TIME_SPAN : from;

        return template.find(Query.query(Criteria.where("timestamp").gte(fromTimestamp).lte(toTimestamp).and("target").regex(target)), LogItem.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/log/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the logs for the specified time span, filtered by user", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<LogItem> getLogsByUser(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") long from,
            @RequestParam(defaultValue = "0") long to
    ) throws LogAccessException {
        validateRequest(from, to);
        long toTimestamp = to == 0 ? System.currentTimeMillis() : to;
        long fromTimestamp = from == 0 ? toTimestamp - ConstantUtils.DEFAULT_DATA_TIME_SPAN : from;

        return template.find(Query.query(Criteria.where("timestamp").gte(fromTimestamp).lte(toTimestamp).and("userId").is(userId)), LogItem.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/log/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the logs for the specified time span, filtered by client", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<LogItem> getLogsByClient(
            @PathVariable int clientId,
            @RequestParam(defaultValue = "0") long from,
            @RequestParam(defaultValue = "0") long to
    ) throws LogAccessException {
        validateRequest(from, to);
        long toTimestamp = to == 0 ? System.currentTimeMillis() : to;
        long fromTimestamp = from == 0 ? toTimestamp - ConstantUtils.DEFAULT_DATA_TIME_SPAN : from;

        return template.find(Query.query(Criteria.where("timestamp").gte(fromTimestamp).lte(toTimestamp).and("clientId").is(clientId)), LogItem.class);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/log/action/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the logs for the specified time span, filtered by action", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid time range. Please provide an unix timestamp: from >= 0 and to >=0")
    })
    public List<LogItem> getLogsByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "0") long from,
            @RequestParam(defaultValue = "0") long to
    ) throws LogAccessException {
        validateRequest(from, to);
        long toTimestamp = to == 0 ? System.currentTimeMillis() : to;
        long fromTimestamp = from == 0 ? toTimestamp - ConstantUtils.DEFAULT_DATA_TIME_SPAN : from;

        return template.find(Query.query(Criteria.where("timestamp").gte(fromTimestamp).lte(toTimestamp).and("action").regex(action)), LogItem.class);
    }

    // ---------------------------------------------- Utility functions ------------------------------------------------

    private void validateRequest(@RequestParam(defaultValue = "0") long from, @RequestParam(defaultValue = "0") long to) throws LogAccessException {
        if (from < 0 || to < 0 || from > to) throw new LogAccessException(ErrorCodeUtils.INVALID_TIME_RANGE_LOG);
    }
}