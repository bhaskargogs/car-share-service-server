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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sharing.car.dto.DriverCreationDTO;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.service.DriverService;
import org.sharing.car.util.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DriverController.class)
@Slf4j
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
        DriverCreationDTO creationDTO = new DriverCreationDTO("Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43);
        when(driverService.createDriver(any(DriverDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JsonMapper.mapToJson(creationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Allan"))
                .andExpect(jsonPath("$.lastName").value("Hufflepuff"))
                .andExpect(jsonPath("$.email").value("ahufflepuff@hotmail.com"))
                .andExpect(jsonPath("$.age").value(43))
                .andExpect(jsonPath("$.firstName").value("Allan"))
                .andExpect(jsonPath("$.status").value("ONLINE"));
    }
}
