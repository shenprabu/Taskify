package com.cams.taskify.response;

import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Task.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskListResponse {
    private List<TaskDTO> tasks;
    private List<EmployeeDTO> employees;
}
