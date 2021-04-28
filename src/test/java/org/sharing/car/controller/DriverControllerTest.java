package org.sharing.car.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.sharing.car.domainvalue.OnlineStatus;
import org.sharing.car.dto.DriverCreationDTO;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.entity.Driver;
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
        DriverDTO driverDTO = new DriverDTO(null, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, "ONLINE", ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")), ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        when(driverService.createDriver(driverDTO)).thenAnswer(invocation -> invocation.getArgument(0));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonMapper.mapToJson(driverDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(driverDTO, JsonMapper.mapFromJson(result.getResponse().getContentAsString(), DriverDTO.class));

    }
}
