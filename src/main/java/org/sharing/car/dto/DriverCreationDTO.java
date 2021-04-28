package org.sharing.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sharing.car.domainvalue.OnlineStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

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

    private final OnlineStatus status = OnlineStatus.ONLINE;

    private final ZonedDateTime createdDate = ZonedDateTime.now();

    private final ZonedDateTime updatedDate = ZonedDateTime.now();

    private DriverCreationDTO() {
    }

    public static DriverDTO makeDriverDTO(DriverCreationDTO driverCreationDTO) {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setFirstName(driverCreationDTO.getFirstName());
        driverDTO.setLastName(driverCreationDTO.getLastName());
        driverDTO.setEmail(driverCreationDTO.getEmail());
        driverDTO.setPassword(driverCreationDTO.getPassword());
        if (driverCreationDTO.getAge() != 0) {
            driverDTO.setAge(driverCreationDTO.getAge());
        }
        driverDTO.setStatus(driverCreationDTO.getStatus().name());
        driverDTO.setCreatedDate(driverCreationDTO.getCreatedDate());
        driverDTO.setUpdatedDate(driverCreationDTO.getUpdatedDate());
        return driverDTO;
    }
}
