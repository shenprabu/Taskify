package com.cams.taskify.service;

import com.cams.taskify.DTO.Employee.CreateEmployeeDTO;
import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Employee.PatchEmployeeDTO;
import com.cams.taskify.entity.Employee;
import com.cams.taskify.exception.ResourceNotFoundException;
import com.cams.taskify.repository.EmployeeRepository;
import com.cams.taskify.response.PaginatedResponse;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public PaginatedResponse<EmployeeDTO> getEmployees(Pageable pageable) {
        Page<Employee> employeesPage = employeeRepository.findAll(pageable);

        return PaginatedResponse.<EmployeeDTO>builder()
                .data(employeesPage.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList()))
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
}
