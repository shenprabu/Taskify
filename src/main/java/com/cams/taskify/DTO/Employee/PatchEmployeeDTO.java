package com.cams.taskify.DTO.Employee;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchEmployeeDTO {
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private String department;
}
