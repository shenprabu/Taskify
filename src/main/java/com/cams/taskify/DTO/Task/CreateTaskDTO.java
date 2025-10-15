package com.cams.taskify.DTO.Task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {
    @NotBlank
    private String title;

    private String description;
    private String status;
    private Long assignedTo;
}
