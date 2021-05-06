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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.dto.MapStructMapper;
import org.sharing.car.entity.Driver;
import org.sharing.car.exception.DriverNotFoundException;
import org.sharing.car.exception.InvalidConstraintsException;
import org.sharing.car.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository repository;
    private final MapStructMapper mapper;

    @Transactional
    public DriverDTO createDriver(DriverDTO driverDTO) throws InvalidConstraintsException {
        Driver newDriver;
        try {
            newDriver = repository.save(mapper.makeDriver(driverDTO));
        } catch (InvalidConstraintsException ex) {
            log.error("InvalidDriverException: createDriver() Failed to create Driver " + driverDTO, ex);
            throw new InvalidConstraintsException(ex.getMessage());
        }
        return mapper.makeDriverDTO(newDriver);
    }

    @Transactional
    public DriverDTO findDriverById(Long id) throws DriverNotFoundException {
        Driver driver;
        driver = DriverService.findById(repository, id).orElseThrow(() -> new DriverNotFoundException("Driver not found with ID " + id));
        return mapper.makeDriverDTO(driver);
    }

    @Transactional
    public DriverDTO updateDriver(DriverDTO driverDTO, Long id) throws DriverNotFoundException {
        Driver updatedDriver;
        if (DriverService.findById(repository, id).isPresent()) {
            updatedDriver = repository.save(mapper.makeDriver(driverDTO));
        } else {
            throw new DriverNotFoundException("DriverNotFoundException: updateDriver() Driver not found with ID " + id);
        }
        return mapper.makeDriverDTO(updatedDriver);
    }

    private static Optional<Driver> findById(DriverRepository repository, Long id) {
        return repository.findById(id);
    }

    public void deleteDriver(Long id) throws DriverNotFoundException {
        if (DriverService.findById(repository, id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new DriverNotFoundException("DriverNotFoundException: deleteDriver() Driver not found with ID " + id);
        }
    }
}
