/*
 * Copyright © Marc Auberer 2019-2021. All rights reserved
 */

package com.chillibits.particulatematterapi.controller.v1;

import com.chillibits.particulatematterapi.exception.ErrorCode;
import com.chillibits.particulatematterapi.exception.exception.ClientDataException;
import com.chillibits.particulatematterapi.model.db.main.Client;
import com.chillibits.particulatematterapi.model.dto.ClientDto;
import com.chillibits.particulatematterapi.model.dto.ClientInsertUpdateDto;
import com.chillibits.particulatematterapi.repository.ClientRepository;
import com.chillibits.particulatematterapi.service.ClientService;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("logging")
@DisplayName("Client Controller")
public class ClientControllerTests {

    @Autowired
    private ClientController clientController;
    @MockBean
    private ClientRepository clientRepository;

    private final List<Client> testData = getTestData();
    private final List<ClientInsertUpdateDto> insertUpdateTestData = getTestInsertUpdateData();
    private final List<ClientDto> assertData = getAssertData();

    @TestConfiguration
    static class ClientControllerImplTestContextConfiguration {

        @Bean
        public ClientController clientController() {
            return new ClientController();
        }

        @Bean
        public ClientService clientService() {
            return new ClientService();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Before
    public void init() {
        // Setup fake method calls
        when(clientRepository.findAll()).thenReturn(testData);
        when(clientRepository.findByName(testData.get(0).getName())).thenReturn(Optional.of(testData.get(0)));
        when(clientRepository.save(any(Client.class))).then(returnsFirstArg());
        when(clientRepository.updateClient(any(Client.class))).thenReturn(1);
        doNothing().when(clientRepository).deleteById(anyInt());
    }

    // ------------------------------------------------- Get client ----------------------------------------------------

    @Test
    @DisplayName("Test for getting all clients successfully")
    public void testGetAllClients() {
        List<ClientDto> result = clientController.getAllClients();
        assertThat(result).containsExactlyInAnyOrder(assertData.toArray(ClientDto[]::new));
    }

    @Test
    @DisplayName("Test for getting a client by its name successfully")
    public void testGetSingleClientByName() throws ClientDataException {
        ClientDto result = clientController.getClientInfoByName(testData.get(0).getName());
        assertEquals(assertData.get(0), result);
    }

    @Test
    @DisplayName("Test for getting a client by its name, triggering a ClientNotExisting exception")
    public void testGetSingleClientByNameClientNotExistingException() {
        // Try with invalid input
        Exception exception = assertThrows(ClientDataException.class, () ->
                clientController.getClientInfoByName("test")
        );

        String expectedMessage = new ClientDataException(ErrorCode.CLIENT_NOT_EXISTING).getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    // ------------------------------------------------- Add client ----------------------------------------------------

    @Test
    @DisplayName("Test for adding a client successfully")
    public void testAddClient() throws ClientDataException {
        ClientDto result = clientController.addClient(insertUpdateTestData.get(1));
        assertEquals(assertData.get(1), result);
    }

    @Test
    @DisplayName("Test for adding a client, triggering a InvalidClientData exception")
    public void testAddClientInvalidClientDataException() {
        // Try with invalid input
        Exception exception = assertThrows(ClientDataException.class, () ->
                clientController.addClient(insertUpdateTestData.get(4))
        );

        String expectedMessage = new ClientDataException(ErrorCode.INVALID_CLIENT_DATA).getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    // ------------------------------------------------ Update client --------------------------------------------------

    @Test
    @DisplayName("Test for updating a client successfully")
    public void testUpdateClient() throws ClientDataException {
        int result = clientController.updateClient(insertUpdateTestData.get(1));
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test for updating a client, triggering a InvalidClientData exception")
    public void testUpdateClientInvalidClientDataException() {
        // Try with invalid input
        Exception exception = assertThrows(ClientDataException.class, () ->
                clientController.updateClient(insertUpdateTestData.get(4))
        );

        String expectedMessage = new ClientDataException(ErrorCode.INVALID_CLIENT_DATA).getMessage();
        assertEquals(expectedMessage, exception.getMessage());
    }

    // ------------------------------------------------ Delete client --------------------------------------------------

    @Test
    @DisplayName("Test for deleting a client successfully")
    public void testDeleteClient() throws ClientDataException {
        assertDoesNotThrow(() -> clientController.deleteClient(testData.get(2).getId()));
    }

    // -------------------------------------------------- Test data ----------------------------------------------------

    private List<Client> getTestData() {
        // Create client objects
        Client c1 = new Client(0, "admin", "Particulate Matter Admin", "not set", Client.TYPE_DESKTOP_APPLICATION, Client.ROLE_APPLICATION_ADMIN, Client.STATUS_ONLINE, true, 100, "1.0.0", 100, "1.0.0", "ChilliBits", "Only for administrators");
        Client c2 = new Client(1, "pmapp", "Particulate Matter App", "not set", Client.TYPE_ANDROID_APP, Client.ROLE_APPLICATION_CHILLIBITS, Client.STATUS_ONLINE, true, 400, "4.0.0", 400, "4.0.0", "ChilliBits", "");
        Client c3 = new Client(2, "pmapp-web", "Particulate Matter App Web", "not set", Client.TYPE_WEBSITE, Client.ROLE_APPLICATION_CHILLIBITS, Client.STATUS_ONLINE, true, 123, "1.2.3", 123, "1.2.3", "ChilliBits", "");
        Client c4 = new Client(3, "pm-pred", "Prediction Service", "not set", Client.TYPE_NONE, Client.ROLE_APPLICATION_CHILLIBITS, Client.STATUS_OFFLINE, false, 100, "1.0.0", 100, "1.0.0", "ChilliBits", "In development");
        Client c5 = new Client(4, "awesome-app", "My awesome application", "12345", Client.TYPE_DESKTOP_APPLICATION, Client.ROLE_APPLICATION, Client.STATUS_SUPPORT_ENDED, false, 1230, "1.23.0", 1256, "1.25.6", "", "");
        // Add them to test data
        return Arrays.asList(c1, c2, c3, c4, c5);
    }

    private List<ClientInsertUpdateDto> getTestInsertUpdateData() {
        // Create client objects
        ClientInsertUpdateDto c1 = new ClientInsertUpdateDto(0, "admin", "Particulate Matter Admin", "not set", Client.TYPE_DESKTOP_APPLICATION, Client.ROLE_APPLICATION_ADMIN, Client.STATUS_ONLINE, true, 100, "1.0.0", 100, "1.0.0", "ChilliBits", "Only for administrators");
        ClientInsertUpdateDto c2 = new ClientInsertUpdateDto(1, "pmapp", "Particulate Matter App", "not set", Client.TYPE_ANDROID_APP, Client.ROLE_APPLICATION_CHILLIBITS, Client.STATUS_ONLINE, true, 400, "4.0.0", 400, "4.0.0", "ChilliBits", "");
        ClientInsertUpdateDto c3 = new ClientInsertUpdateDto(2, "pmapp-web", "Particulate Matter App Web", "not set", Client.TYPE_WEBSITE, Client.ROLE_APPLICATION_CHILLIBITS, Client.STATUS_ONLINE, true, 123, "1.2.3", 123, "1.2.3", "ChilliBits", "");
        ClientInsertUpdateDto c4 = new ClientInsertUpdateDto(3, "pm-pred", "Prediction Service", "not set", Client.TYPE_NONE, Client.ROLE_APPLICATION_CHILLIBITS, Client.STATUS_OFFLINE, false, 100, "1.0.0", 100, "1.0.0", "ChilliBits", "In development");
        ClientInsertUpdateDto c5 = new ClientInsertUpdateDto(4, "awesome-app", "My awesome application", "12345", Client.TYPE_DESKTOP_APPLICATION, Client.ROLE_APPLICATION, Client.STATUS_SUPPORT_ENDED, false, 1230, "1.23.0", 1256, "1.25.6", "", "");
        // Add them to test data
        return Arrays.asList(c1, c2, c3, c4, c5);
    }

    private List<ClientDto> getAssertData() {
        // Create client dto objects
        ClientDto cd1 = new ClientDto("admin", "Particulate Matter Admin", Client.TYPE_DESKTOP_APPLICATION, Client.STATUS_ONLINE, 100, "1.0.0", 100, "1.0.0", "ChilliBits", "Only for administrators");
        ClientDto cd2 = new ClientDto("pmapp", "Particulate Matter App", Client.TYPE_ANDROID_APP, Client.STATUS_ONLINE, 400, "4.0.0", 400, "4.0.0", "ChilliBits", "");
        ClientDto cd3 = new ClientDto("pmapp-web", "Particulate Matter App Web", Client.TYPE_WEBSITE, Client.STATUS_ONLINE, 123, "1.2.3", 123, "1.2.3", "ChilliBits", "");
        ClientDto cd4 = new ClientDto("pm-pred", "Prediction Service", Client.TYPE_NONE, Client.STATUS_OFFLINE, 100, "1.0.0", 100, "1.0.0", "ChilliBits", "In development");
        ClientDto cd5 = new ClientDto("awesome-app", "My awesome application", Client.TYPE_DESKTOP_APPLICATION, Client.STATUS_SUPPORT_ENDED, 1230, "1.23.0", 1256, "1.25.6", "", "");
        // Add them to test data
        return Arrays.asList(cd1, cd2, cd3, cd4, cd5);
    }
}