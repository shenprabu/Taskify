package com.cams.taskify.service;

import com.cams.taskify.DTO.Employee.EmployeeDTO;
import com.cams.taskify.DTO.Task.CreateTaskDTO;
import com.cams.taskify.DTO.Task.PatchTaskDTO;
import com.cams.taskify.DTO.Task.TaskDTO;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

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

    public TaskListResponse getTasksWithEmployees(Page<Task> tasksPage) {

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

    public TaskDTO getTask(long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", id));
        return modelMapper.map(task, TaskDTO.class);
    }

    public TaskDTO createTask(CreateTaskDTO createTaskDTO) {
        Task task = modelMapper.map(createTaskDTO, Task.class);
        return modelMapper.map(taskRepository.save(task), TaskDTO.class);
    }

    public TaskDTO updateTask(long id, PatchTaskDTO patchTaskDTO) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", id));

        // Patch ignores null updates
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(patchTaskDTO, task);

        Task saved = taskRepository.save(task);
        return modelMapper.map(saved, TaskDTO.class);
    }
}
