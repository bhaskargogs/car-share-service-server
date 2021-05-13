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
import org.modelmapper.ModelMapper;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.entity.Driver;
import org.sharing.car.exception.DriverNotFoundException;
import org.sharing.car.exception.InvalidConstraintsException;
import org.sharing.car.repository.DriverRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository repository;
    private final ModelMapper mapper;

    @Transactional
    public DriverDTO createDriver(DriverDTO driverDTO) throws InvalidConstraintsException {
        Driver newDriver;
        try {
            newDriver = repository.save(mapper.map(driverDTO, Driver.class));
        } catch (InvalidConstraintsException ex) {
            log.error("InvalidDriverException: createDriver() Failed to create Driver " + driverDTO, ex);
            throw new InvalidConstraintsException(ex.getMessage());
        }
        return mapper.map(newDriver, DriverDTO.class);
    }

    @Transactional
    public DriverDTO findDriverById(Long id) throws DriverNotFoundException {
        Driver driver;
        driver = DriverService.findById(repository, id).orElseThrow(() -> new DriverNotFoundException("Driver not found with ID " + id));
        return mapper.map(driver, DriverDTO.class);
    }

    @Transactional
    public DriverDTO updateDriver(DriverDTO driverDTO, Long id) throws DriverNotFoundException {
        Driver updatedDriver;
        if (DriverService.findById(repository, id).isPresent()) {
            updatedDriver = repository.save(mapper.map(driverDTO, Driver.class));
        } else {
            throw new DriverNotFoundException("DriverNotFoundException: updateDriver() Driver not found with ID " + id);
        }
        return mapper.map(updatedDriver, DriverDTO.class);
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

    public List<DriverDTO> findAllDrivers(int pageNo, int pageSize, String direction, String fieldName) {
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new InvalidConstraintsException("Invalid Direction value " + direction);
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, (direction.equalsIgnoreCase("asc")) ? Sort.by(fieldName).ascending() : Sort.by(fieldName).descending());
        Page<Driver> drivers = repository.findAll(paging);
        return (drivers.hasContent()) ?
                drivers.getContent().stream().map(driver -> mapper.map(driver, DriverDTO.class)).collect(Collectors.toList())
                : new ArrayList<>();
    }
}
