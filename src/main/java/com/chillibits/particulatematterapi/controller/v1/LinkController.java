/*
 * Copyright © Marc Auberer 2019 - 2020. All rights reserved
 */

package com.chillibits.particulatematterapi.controller.v1;

import com.chillibits.particulatematterapi.model.dto.LinkDto;
import com.chillibits.particulatematterapi.model.dto.LinkInsertUpdateDto;
import com.chillibits.particulatematterapi.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Link endpoint
 *
 * Endpoint to create, update or delete relationships between users and sensors
 */
@RestController
@Api(value = "Link REST Endpoint", tags = "link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * Adds a link to the database
     * <p>Note: Requires at least application role A (usual application)</p>
     *
     * @param link Instance of LinkInsertUpdateDto with all required data values
     * @return Inserted record
     */
    @RequestMapping(method = RequestMethod.POST, path = "/link", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Adds a link to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Cannot assign link to a non-existent sensor."),
            @ApiResponse(code = 406, message = "Cannot assign link to a non-existent user."),
            @ApiResponse(code = 406, message = "Invalid link data.")
    })
    public LinkDto addLink(@RequestBody LinkInsertUpdateDto link) {
        return linkService.addLink(link);
    }

    /**
     * Updates a link
     * <p>Note: Requires at least application role A (usual application)</p>
     *
     * @param link Updated instance of LinkInsertUpdateDto with all required data values
     * @return Status code of the update process
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/link", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updates a link")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Cannot assign link to a non-existent user."),
            @ApiResponse(code = 406, message = "Invalid link data.")
    })
    public Integer updateLink(@RequestBody LinkInsertUpdateDto link) {
        return linkService.updateLink(link);
    }

    /**
     * Deletes a link from the database
     * <p>Note: Requires at least application role A (usual application)</p>
     *
     * @param id Id of the link record, that has to be deleted
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/link/{id}")
    @ApiOperation(value = "Deletes a link from the database")
    public void deleteLink(@PathVariable int id) {
        linkService.deleteLinkById(id);
    }
}