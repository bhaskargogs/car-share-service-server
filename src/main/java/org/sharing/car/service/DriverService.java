package org.sharing.car.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.entity.Driver;
import org.sharing.car.exception.InvalidConstraintsException;
import org.sharing.car.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository repository;
    private final ModelMapper mapper;

    @Transactional
    public DriverDTO createDriver(DriverDTO driver) throws InvalidConstraintsException {
        Driver newDriver;
        try {
            newDriver = repository.save(mapper.map(driver, Driver.class));
        } catch (InvalidConstraintsException ex) {
            log.error("InvalidDriverException: createDriver() Failed to create Driver " + driver, ex);
            throw new InvalidConstraintsException(ex.getMessage());
        }
        return mapper.map(newDriver, DriverDTO.class);
    }
}
