package org.sharing.car.controller;

import org.sharing.car.dto.DriverCreationDTO;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@Valid @RequestBody DriverCreationDTO driverCreationRequest) {
        DriverDTO driverDTO = DriverCreationDTO.makeDriverDTO(driverCreationRequest);
        return new ResponseEntity<>(driverService.createDriver(driverDTO), HttpStatus.CREATED);
    }
}
