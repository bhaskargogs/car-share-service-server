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

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sharing.car.domainvalue.GeoCoordinate;
import org.sharing.car.domainvalue.OnlineStatus;
import org.sharing.car.dto.DriverCreationDTO;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.dto.DriverUpdationDTO;
import org.sharing.car.dto.MapStructMapper;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DriverController.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @MockBean
    private MapStructMapper mapper;

    DriverCreationDTO creationDTO;
    DriverUpdationDTO updationDTO;
    List<DriverDTO> drivers = new ArrayList<>();
    DriverDTO driver1;
    DriverDTO driver2;

    @BeforeEach
    public void setUp() {
        creationDTO = new DriverCreationDTO("Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23));
        updationDTO = new DriverUpdationDTO(1L, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), OnlineStatus.ONLINE);
        driver1 = new DriverDTO(1L, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), "ONLINE", ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        driver2 = new DriverDTO(2L, "John", "Maier", "jmaeir@t-online.com", "abc234", 26, new GeoCoordinate(64.25, 100.25), "ONLINE", ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        drivers.add(driver1);
        drivers.add(driver2);
    }

    @Test
    public void testCreateInvalidDriver_Returns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateValidDriver_Returns201() throws Exception {
        when(mapper.makeDriverDTO(any(DriverCreationDTO.class))).thenReturn(driver1);
        when(driverService.createDriver(any(DriverDTO.class))).thenReturn(driver1);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonMapper.serialize(creationDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        DriverDTO expected = (DriverDTO) JsonMapper.deserialize(DriverDTO.class, result.getResponse().getContentAsString());
        assertEquals(expected, driver1);
    }

    @Test
    public void testFindInvalidDriver_Returns404() throws Exception {
        when(driverService.findDriverById(5L)).thenThrow(DriverNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindValidDriver_Returns200() throws Exception {
        when(driverService.findDriverById(1L)).thenReturn(driver1);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/drivers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        DriverDTO expected = (DriverDTO) JsonMapper.deserialize(DriverDTO.class, result.getResponse().getContentAsString());
        assertEquals(expected, driver1);
    }

    @Test
    public void testUpdateInvalidDriver_Returns404() throws Exception {
        when(mapper.makeDriverDTO(any(DriverUpdationDTO.class))).thenReturn(driver1);
        when(driverService.updateDriver(any(DriverDTO.class), eq(1L))).thenThrow(DriverNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/drivers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonMapper.serialize(updationDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateValidDriver_Returns200() throws Exception {
        when(mapper.makeDriverDTO(any(DriverUpdationDTO.class))).thenReturn(driver1);
        when(driverService.updateDriver(any(DriverDTO.class), eq(1L))).thenReturn(driver1);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/drivers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonMapper.serialize(updationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        DriverDTO expected = (DriverDTO) JsonMapper.deserialize(DriverDTO.class, result.getResponse().getContentAsString());
        assertEquals(expected, driver1);
    }

    @Test
    public void testDeleteInvalidDriver_Returns404() throws Exception {
        doThrow(DriverNotFoundException.class).when(driverService).deleteDriver(5L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/drivers/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteValidDriver_Return200() throws Exception {
        doNothing().when(driverService).deleteDriver(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/drivers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(driverService, times(1)).deleteDriver(1L);
    }

    @Test
    public void testFindAllDrivers_Return200AndDrivers() throws Exception {
        when(driverService.findAllDrivers(0, 20, "asc", "id")).thenReturn(drivers);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/drivers")
                .param("pageNo", "0")
                .param("pageSize", "20")
                .param("direction", "asc")
                .param("field", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<DriverDTO> expectedDrivers = JsonMapper.mapListFromJson(result.getResponse().getContentAsString(), new TypeReference<List<DriverDTO>>() {});
        assertEquals(expectedDrivers, drivers);
    }
}
