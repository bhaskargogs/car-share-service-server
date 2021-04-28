package org.sharing.car.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sharing.car.entity.Driver;
import org.sharing.car.exception.InvalidConstraintsException;
import org.sharing.car.repository.DriverRepository;

import java.time.ZonedDateTime;

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

    @Test
    public void createNullDriver() {
        when(driverRepository.save(null)).thenThrow(InvalidConstraintsException.class);
        assertThrows(InvalidConstraintsException.class, () -> driverService.createDriver(null));
    }

    @Test
    public void createValidDriver() {
        Driver driver = new Driver(1l, "Allan", "Hufflepuff", "ahufflepuff@hotmail.com", "hotmail123", 43, ZonedDateTime.now(), ZonedDateTime.now());
        when(driverRepository.save(driver)).thenReturn(driver);
        assertEquals(driver, driverService.createDriver(driver));
    }
}
