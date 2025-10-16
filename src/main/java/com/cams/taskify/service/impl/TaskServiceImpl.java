package com.cams.taskify.service.impl;

import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Task.CreateTaskDTO;
import com.cams.taskify.DTO.Task.PatchTaskDTO;
import com.cams.taskify.DTO.Task.TaskDTO;
import com.cams.taskify.DTO.Task.TaskDetailsDTO;
import com.cams.taskify.entity.Employee;
import com.cams.taskify.entity.Task;
import com.cams.taskify.exception.ResourceNotFoundException;
import com.cams.taskify.repository.EmployeeRepository;
import com.cams.taskify.repository.TaskRepository;
import com.cams.taskify.response.PaginatedResponse;
import com.cams.taskify.response.TaskListResponse;
import com.cams.taskify.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public PaginatedResponse<TaskListResponse> getTasks(Pageable pageable) {
        Page<Task> tasksPage = taskRepository.findAll(pageable);

        return PaginatedResponse.<TaskListResponse>builder()
                .data(getTasksWithEmployees(tasksPage))
                .count(tasksPage.getNumberOfElements())
                .page(tasksPage.getNumber())
                .totalRecords(tasksPage.getTotalPages())
                .totalPages(tasksPage.getTotalPages())
                .build();
    }

    private TaskListResponse getTasksWithEmployees(Page<Task> tasksPage) {

        // Convert to TaskDTOs
        List<TaskDTO> taskDTOs = tasksPage.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList();

        // Collect unique employee IDs
        Set<Long> employeeIds = tasksPage.stream()
                .map(Task::getAssignedTo)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Fetch employees and map to DTOs
        List<EmployeeDTO> employeeDTOs = employeeRepository.findAllById(employeeIds).stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class)).toList();

        return new TaskListResponse(taskDTOs,  employeeDTOs);
    }

    public TaskDetailsDTO getTask(long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", id));
        TaskDetailsDTO taskDetailsDTO = modelMapper.map(task, TaskDetailsDTO.class);
        if(task.getAssignedTo() != null) {
            // Having employee as optional to ignore missing employee of already assigned tasks
            Optional<Employee> employee = employeeRepository.findById(task.getAssignedTo());
            if(employee.isPresent()) {
                taskDetailsDTO.setAssignedTo(modelMapper.map(employee, EmployeeDTO.class));
            }
        }

        return taskDetailsDTO;
    }

    public TaskDetailsDTO createTask(CreateTaskDTO createTaskDTO) {
        Employee employee = null;
        if(createTaskDTO.getAssignedTo() != null) {
            employee = employeeRepository.findById(createTaskDTO.getAssignedTo()).orElseThrow(() -> new ResourceNotFoundException("Employee", createTaskDTO.getAssignedTo()));
        }
        Task newTask = taskRepository.save(modelMapper.map(createTaskDTO, Task.class));
        TaskDetailsDTO taskDetailsDTO = modelMapper.map(newTask, TaskDetailsDTO.class);
        if(employee != null) {
            taskDetailsDTO.setAssignedTo(modelMapper.map(employee, EmployeeDTO.class));
        }
        return taskDetailsDTO;
    }

    public TaskDetailsDTO updateTask(long id, PatchTaskDTO patchTaskDTO) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", id));

        Optional<Employee> employee = Optional.empty();
        if(patchTaskDTO.getAssignedTo() != null) {
            // Having employee as optional to ignore missing employee of already assigned tasks
            employee = employeeRepository.findById(patchTaskDTO.getAssignedTo());
            if(employee.isEmpty()) {
                throw new ResourceNotFoundException("Employee", patchTaskDTO.getAssignedTo());
            }
        }

        // Patch ignores null updates
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.map(patchTaskDTO, task);
        Task saved = taskRepository.save(task);

        if(saved.getAssignedTo() != null) {
            employee = employeeRepository.findById(task.getAssignedTo());
        }

        TaskDetailsDTO taskDetailsDTO = modelMapper.map(saved, TaskDetailsDTO.class);
        if(employee.isPresent()) {
            taskDetailsDTO.setAssignedTo(modelMapper.map(employee, EmployeeDTO.class));
        }
        return taskDetailsDTO;
    }
}
