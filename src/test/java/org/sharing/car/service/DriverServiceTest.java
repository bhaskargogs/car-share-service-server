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

package org.sharing.car.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.sharing.car.domainvalue.GeoCoordinate;
import org.sharing.car.domainvalue.OnlineStatus;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.entity.Driver;
import org.sharing.car.exception.DriverNotFoundException;
import org.sharing.car.exception.InvalidConstraintsException;
import org.sharing.car.repository.DriverRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @Mock
    private ModelMapper mapper;

    @Test
    public void testCreateNullDriver() {
        when(driverRepository.save(null)).thenThrow(InvalidConstraintsException.class);
        assertThrows(InvalidConstraintsException.class, () -> driverService.createDriver(null));
    }

    @Test
    public void testCreateValidDriver() {
        DriverDTO driverDTO = new DriverDTO(1L, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), "ONLINE", ZonedDateTime.now(), ZonedDateTime.now());
        Driver driverEntity = new Driver("Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), OnlineStatus.ONLINE, ZonedDateTime.now(), ZonedDateTime.now());
        when(mapper.map(driverDTO, Driver.class)).thenReturn(driverEntity);
        when(mapper.map(driverEntity, DriverDTO.class)).thenReturn(driverDTO);
        when(driverRepository.save(driverEntity)).thenAnswer(invocation -> invocation.getArgument(0));
        assertEquals(driverDTO, driverService.createDriver(driverDTO));
    }

    @Test
    public void testFindInvalidDriver() {
        when(driverRepository.findById(2L)).thenThrow(DriverNotFoundException.class);
        assertThrows(DriverNotFoundException.class, () -> driverService.findDriverById(2L));
    }

    @Test
    public void testFindValidDriver() {
        DriverDTO driverDTO = new DriverDTO(1L, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), "ONLINE", ZonedDateTime.now(), ZonedDateTime.now());
        Driver driverEntity = new Driver("Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), OnlineStatus.ONLINE, ZonedDateTime.now(), ZonedDateTime.now());
        when(mapper.map(driverEntity, DriverDTO.class)).thenReturn(driverDTO);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverEntity));
        assertEquals(driverDTO, driverService.findDriverById(1L));
    }
}
