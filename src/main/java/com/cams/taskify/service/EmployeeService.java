package com.cams.taskify.service;

import com.cams.taskify.DTO.Employee.CreateEmployeeDTO;
import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Employee.PatchEmployeeDTO;
import com.cams.taskify.constants.TaskStatus;
import com.cams.taskify.response.PaginatedResponse;
import com.cams.taskify.response.TaskListResponse;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    PaginatedResponse<List<EmployeeDTO>> getEmployees(Pageable pageable);

    EmployeeDTO getEmployeeById(long id);

    EmployeeDTO createEmployee(CreateEmployeeDTO createEmployeeDTO);

    EmployeeDTO updateEmployee(long id, PatchEmployeeDTO patchEmployeeDTO);

    PaginatedResponse<TaskListResponse> getTasksForEmployee(long empId, TaskStatus status, Pageable pageable);

    Map<String, Object> getEmployeeTaskStats(long empId);

}
