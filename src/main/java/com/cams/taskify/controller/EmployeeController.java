package com.cams.taskify.controller;

import com.cams.taskify.DTO.Employee.CreateEmployeeDTO;
import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Employee.PatchEmployeeDTO;
import com.cams.taskify.response.PaginatedResponse;
import com.cams.taskify.response.TaskListResponse;
import com.cams.taskify.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import static com.cams.taskify.constants.RouteConstants.*;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(EMPLOYEES_API_BASE_URL)
    public PaginatedResponse<List<EmployeeDTO>> getEmployees(Pageable pageable) {
        return employeeService.getEmployees(pageable);
    }

    @GetMapping(EMPLOYEES_API_ID_URL)
    public EmployeeDTO getEmployee(@PathVariable long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping(EMPLOYEES_API_BASE_URL)
    public EmployeeDTO createEmployee(@Valid @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        return employeeService.createEmployee(createEmployeeDTO);
    }

    @PatchMapping(EMPLOYEES_API_ID_URL)
    public EmployeeDTO updateEmployee(@PathVariable long id, @Valid @RequestBody PatchEmployeeDTO patchEmployeeDTO) {
        return employeeService.updateEmployee(id, patchEmployeeDTO);
    }

    @GetMapping(EMPLOYEES_API_TASKS_URL)
    public PaginatedResponse<TaskListResponse> getTasksForEmployee(@PathVariable long id, Pageable pageable) {
        return employeeService.getTasksForEmployee(id, pageable);
    }

}
