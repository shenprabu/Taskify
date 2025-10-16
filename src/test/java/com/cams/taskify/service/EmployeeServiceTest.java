package com.cams.taskify.service;

import com.cams.taskify.DTO.Employee.CreateEmployeeDTO;
import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Employee.PatchEmployeeDTO;
import com.cams.taskify.entity.Employee;
import com.cams.taskify.exception.ResourceNotFoundException;
import com.cams.taskify.repository.EmployeeRepository;
import com.cams.taskify.repository.TaskRepository;
import com.cams.taskify.response.PaginatedResponse;
import com.cams.taskify.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private TaskRepository taskRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeServiceImpl(employeeRepository, new ModelMapper(), taskRepository);
    }

    @Test
    void testGetEmployees() {
        Pageable pageable = PageRequest.of(0, 2);

        Employee emp1 = new Employee();
        emp1.setId(1L);
        emp1.setName("John");
        emp1.setEmail("john@cams.com");

        Employee emp2 = new Employee();
        emp2.setId(2L);
        emp2.setName("Jane");
        emp2.setEmail("jane@cams.com");

        List<Employee> employees = List.of(emp1, emp2);
        Page<Employee> employeePage = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        PaginatedResponse<List<EmployeeDTO>> result = employeeService.getEmployees(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalRecords());
        assertEquals(2, result.getCount());
        assertEquals(0, result.getPage());
        assertEquals("John", result.getData().get(0).getName());
        assertEquals("Jane", result.getData().get(1).getName());
        verify(employeeRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John");
        employee.setEmail("john@cams.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void testCreateEmployee() {
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setName("Jane");
        createEmployeeDTO.setEmail("jane@cams.com");

        Employee newEmployee = new Employee();
        newEmployee.setId(1L);
        newEmployee.setName("Jane");
        newEmployee.setEmail("jane@cams.com");

        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        EmployeeDTO created = employeeService.createEmployee(createEmployeeDTO);

        assertEquals("Jane", created.getName());
    }

    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Jane");
        employee.setEmail("jane@cams.com");

        PatchEmployeeDTO patchEmployeeDTO = new PatchEmployeeDTO();
        patchEmployeeDTO.setName("John");
        patchEmployeeDTO.setEmail("john@cams.com");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setName("John");
        updatedEmployee.setEmail("john@cams.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(updatedEmployee);

        EmployeeDTO updated = employeeService.updateEmployee(1L, patchEmployeeDTO);
        assertEquals("John", updated.getName());

    }
}
