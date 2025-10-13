package com.cams.taskify.DTO.Employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class PatchEmployeeDTO {
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private String department;
}
