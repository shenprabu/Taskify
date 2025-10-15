package com.cams.taskify.DTO.Task;

import com.cams.taskify.DTO.Employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// This is to show assignedTo information as EmployeeDTO
public class TaskDetailsDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private EmployeeDTO assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
