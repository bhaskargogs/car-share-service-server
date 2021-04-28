package org.sharing.car.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sharing.car.entity.Driver;
import org.sharing.car.exception.InvalidConstraintsException;
import org.sharing.car.repository.DriverRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository repository;

    public Driver createDriver(Driver driver) throws InvalidConstraintsException {
        Driver newDriver;
        try {
            newDriver = repository.save(driver);
        } catch (InvalidConstraintsException ex) {
            log.error("InvalidDriverException: createDriver() Failed to create Driver " + driver, ex);
            throw new InvalidConstraintsException(ex.getMessage());
        }
        return newDriver;
    }
}
