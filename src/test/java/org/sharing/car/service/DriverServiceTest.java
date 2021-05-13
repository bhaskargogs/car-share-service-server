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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sharing.car.domainvalue.GeoCoordinate;
import org.sharing.car.domainvalue.OnlineStatus;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.dto.MapStructMapper;
import org.sharing.car.entity.Driver;
import org.sharing.car.exception.DriverNotFoundException;
import org.sharing.car.exception.InvalidConstraintsException;
import org.sharing.car.repository.DriverRepository;
import org.springframework.data.domain.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @Mock
    private MapStructMapper mapper;

    List<DriverDTO> drivers = new ArrayList<>();
    List<Driver> driverEntities = new ArrayList<>();
    DriverDTO driver1;
    Driver driverEntity1;
    DriverDTO driver2;
    Driver driverEntity2;

    @BeforeEach
    public void setUp() {
        driver1 = new DriverDTO(1L, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), "ONLINE", ZonedDateTime.now(), ZonedDateTime.now());
        driverEntity1 = new Driver("Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), OnlineStatus.ONLINE, ZonedDateTime.now(), ZonedDateTime.now());
        driver2 = new DriverDTO(2L, "John", "Maier", "jmaeir@t-online.com", "abc234", 26, new GeoCoordinate(64.25, 100.25), "ONLINE", ZonedDateTime.now(), ZonedDateTime.now());
        driverEntity2 = new Driver("John", "Maier", "jmaeir@t-online.com", "abc234", 26, new GeoCoordinate(64.25, 100.25), OnlineStatus.ONLINE, ZonedDateTime.now(), ZonedDateTime.now());
        driverEntities.add(driverEntity1);
        driverEntities.add(driverEntity2);
        drivers.add(driver1);
        drivers.add(driver2);
    }

    @Test
    public void testCreateNullDriver() {
        when(driverRepository.save(null)).thenThrow(InvalidConstraintsException.class);
        assertThrows(InvalidConstraintsException.class, () -> driverService.createDriver(null));
    }

    @Test
    public void testCreateValidDriver() {
        when(mapper.makeDriver(driver1)).thenReturn(driverEntity1);
        when(mapper.makeDriverDTO(driverEntity1)).thenReturn(driver1);
        when(driverRepository.save(driverEntity1)).thenAnswer(invocation -> invocation.getArgument(0));
        assertEquals(driver1, driverService.createDriver(driver1));
    }

    @Test
    public void testFindInvalidDriver() {
        when(driverRepository.findById(2L)).thenThrow(DriverNotFoundException.class);
        assertThrows(DriverNotFoundException.class, () -> driverService.findDriverById(2L));
    }

    @Test
    public void testFindAllDrivers() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());
        Page<Driver> driverEntitiesPage = new PageImpl<>(driverEntities);
        lenient().when(mapper.makeDriverDTO(driverEntity1)).thenReturn(driver1);
        lenient().when(mapper.makeDriverDTO(driverEntity2)).thenReturn(driver2);
        when(driverRepository.findAll(pageable)).thenReturn(driverEntitiesPage);
        assertEquals(drivers, driverService.findAllDrivers(0, 5, "asc", "id"));
    }

    @Test
    public void testFindValidDriver() {
        when(mapper.makeDriverDTO(driverEntity1)).thenReturn(driver1);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverEntity1));
        assertEquals(driver1, driverService.findDriverById(1L));
    }

    @Test
    public void testUpdateInvalidDriver() {
        when(driverRepository.findById(2L)).thenThrow(DriverNotFoundException.class);
        assertThrows(DriverNotFoundException.class, () -> driverService.updateDriver(driver1, 2L));
    }

    @Test
    public void testUpdateValidDriver() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverEntity1));
        when(driverRepository.save(driverEntity1)).thenReturn(driverEntity1);
        when(mapper.makeDriver(driver1)).thenReturn(driverEntity1);
        when(mapper.makeDriverDTO(driverEntity1)).thenReturn(driver1);
        assertEquals(driver1, driverService.updateDriver(driver1, 1L));
    }

    @Test
    public void testDeleteInvalidDriver() {
        when(driverRepository.findById(2L)).thenThrow(DriverNotFoundException.class);
        assertThrows(DriverNotFoundException.class, () -> driverService.deleteDriver(2L));
    }

    @Test
    public void testDeleteValidDriver() {
        DriverDTO driverDTO = new DriverDTO(1L, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), "ONLINE", ZonedDateTime.now(), ZonedDateTime.now());
        Driver driverEntity = new Driver("Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, new GeoCoordinate(35.32, 87.23), OnlineStatus.ONLINE, ZonedDateTime.now(), ZonedDateTime.now());
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverEntity));
        driverService.deleteDriver(1L);
        verify(driverRepository, times(1)).deleteById(1L);
    }
}
