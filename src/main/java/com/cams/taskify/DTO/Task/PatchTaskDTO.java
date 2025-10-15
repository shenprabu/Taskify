package com.cams.taskify.DTO.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchTaskDTO {
    private String title;
    private String description;
    private String status;
    private Long assignedTo;
}
