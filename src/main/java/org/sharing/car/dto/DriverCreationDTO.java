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

package org.sharing.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sharing.car.domainvalue.GeoCoordinate;
import org.sharing.car.domainvalue.OnlineStatus;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class DriverCreationDTO {

    @NotBlank(message = "First Name must not be empty")
    private String firstName;

    @NotBlank(message = "Last Name must not be empty")
    private String lastName;

    @Email(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;

    @Min(value = 15, message = "Age must be greater than or equal to  15")
    @Max(value = 120, message = "Age must be less than 120")
    private int age;

    @Valid
    private GeoCoordinate coordinate;

    private DriverCreationDTO() {
    }

    public static DriverDTO makeDriverDTO(DriverCreationDTO driverCreationDTO) {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setFirstName(driverCreationDTO.getFirstName());
        driverDTO.setLastName(driverCreationDTO.getLastName());
        driverDTO.setEmail(driverCreationDTO.getEmail());
        driverDTO.setPassword(driverCreationDTO.getPassword());
        GeoCoordinate coordinate = driverCreationDTO.getCoordinate();
        if (coordinate != null) {
            driverDTO.setCoordinate(coordinate);
        }
        if (driverCreationDTO.getAge() != 0) {
            driverDTO.setAge(driverCreationDTO.getAge());
        }
        driverDTO.setStatus(OnlineStatus.ONLINE.name());
        return driverDTO;
    }
}
