package com.cams.taskify.service;

import com.cams.taskify.DTO.Employee.CreateEmployeeDTO;
import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Employee.PatchEmployeeDTO;
import com.cams.taskify.DTO.Task.TaskDTO;
import com.cams.taskify.entity.Employee;
import com.cams.taskify.entity.Task;
import com.cams.taskify.exception.ResourceNotFoundException;
import com.cams.taskify.repository.EmployeeRepository;
import com.cams.taskify.repository.TaskRepository;
import com.cams.taskify.response.PaginatedResponse;

import com.cams.taskify.response.TaskListResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    public PaginatedResponse<List<EmployeeDTO>> getEmployees(Pageable pageable) {
        Page<Employee> employeesPage = employeeRepository.findAll(pageable);

        return PaginatedResponse.<List<EmployeeDTO>>builder()
                .data(employeesPage.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).toList())
                .count(employeesPage.getNumberOfElements())
                .page(employeesPage.getNumber())
                .totalRecords(employeesPage.getTotalPages())
                .totalPages(employeesPage.getTotalPages())
                .build();
    }

    public EmployeeDTO getEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public EmployeeDTO createEmployee(CreateEmployeeDTO createEmployeeDTO) {
        Employee employee = modelMapper.map(createEmployeeDTO, Employee.class);
        return modelMapper.map(employeeRepository.save(employee), EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployee(long id, PatchEmployeeDTO patchEmployeeDTO) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        // Patch ignores null updates
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(patchEmployeeDTO, employee);

        Employee saved = employeeRepository.save(employee);
        return modelMapper.map(saved, EmployeeDTO.class);
    }

    public PaginatedResponse<TaskListResponse> getTasksForEmployee(long id, Pageable pageable) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        Page<Task> taskPage = taskRepository.findByAssignedTo(employee.getId(), pageable);

        List<TaskDTO> tasks = taskPage.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList();
        List<EmployeeDTO> employees = List.of(modelMapper.map(employee, EmployeeDTO.class));

        return PaginatedResponse.<TaskListResponse>builder()
                .data(new TaskListResponse(tasks, employees))
                .count(taskPage.getNumberOfElements())
                .page(taskPage.getNumber())
                .totalRecords(taskPage.getTotalPages())
                .totalPages(taskPage.getTotalPages())
                .build();
    }
}
