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

import org.sharing.car.dto.DriverCreationDTO;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.dto.DriverUpdationDTO;
import org.sharing.car.dto.MapStructMapper;
import org.sharing.car.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private MapStructMapper mapper;

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@Valid @RequestBody DriverCreationDTO driverCreationRequest) {
        DriverDTO driverDTO = mapper.makeDriverDTO(driverCreationRequest);
        return new ResponseEntity<>(driverService.createDriver(driverDTO), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public DriverDTO findDriverById(@PathVariable Long id) {
        return driverService.findDriverById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<DriverDTO> updateDriver(@Valid @RequestBody DriverUpdationDTO driverUpdationDTO, @PathVariable Long id) {
        return new ResponseEntity<>(driverService.updateDriver(mapper.makeDriverDTO(driverUpdationDTO), id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public void deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> findAllDrivers(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                          @RequestParam(value = "direction", defaultValue = "asc") String direction,
                                                          @RequestParam(value = "field", defaultValue = "id") String field) {
        return new ResponseEntity<>(driverService.findAllDrivers(pageNo, pageSize, direction, field), HttpStatus.OK);
    }

}
