/*
 *    Copyright 2021 Bhaskar Gogoi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.sharing.car.controller;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sharing.car.domainvalue.GeoCoordinate;
import org.sharing.car.domainvalue.OnlineStatus;
import org.sharing.car.dto.DriverCreationDTO;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.exception.DriverNotFoundException;
import org.sharing.car.service.DriverService;
import org.sharing.car.util.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DriverController.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Test
    public void testCreateNullDriver_Returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateValidDriver_Returns201() throws Exception {
        DriverCreationDTO creationDTO = new DriverCreationDTO("Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23));
        DriverDTO driverDTO = new DriverDTO(null, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), OnlineStatus.OFFLINE.name(), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        when(driverService.createDriver(any(DriverDTO.class))).thenReturn(driverDTO);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonMapper.serialize(creationDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        DriverDTO expected = (DriverDTO) JsonMapper.deserialize(DriverDTO.class, result.getResponse().getContentAsString());
        assertEquals(expected, driverDTO);
    }

    @Test
    public void testFindDriverWithInvalidId_Returns404() throws Exception {
        when(driverService.findDriverById(5L)).thenThrow(DriverNotFoundException.class);
         mockMvc.perform(MockMvcRequestBuilders.get("/drivers/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindDriverWithValidId_Returns200() throws Exception {
        DriverDTO driverDTO = new DriverDTO(1L, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), OnlineStatus.OFFLINE.name(), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        when(driverService.findDriverById(1L)).thenReturn(driverDTO);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/drivers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        DriverDTO expected = (DriverDTO) JsonMapper.deserialize(DriverDTO.class, result.getResponse().getContentAsString());
        assertEquals(expected, driverDTO);
    }
}
