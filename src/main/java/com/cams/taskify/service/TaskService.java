package com.cams.taskify.service;

import com.cams.taskify.DTO.Task.CreateTaskDTO;
import com.cams.taskify.DTO.Task.PatchTaskDTO;
import com.cams.taskify.DTO.Task.TaskDTO;
import com.cams.taskify.entity.Task;
import com.cams.taskify.exception.ResourceNotFoundException;
import com.cams.taskify.repository.TaskRepository;
import com.cams.taskify.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public PaginatedResponse<TaskDTO> getTasks(Pageable pageable) {
        Page<Task> tasksPage = taskRepository.findAll(pageable);

        return PaginatedResponse.<TaskDTO>builder()
                .data(tasksPage.stream().map(task -> modelMapper.map(task, TaskDTO.class)).collect(Collectors.toList()))
                .count(tasksPage.getNumberOfElements())
                .page(tasksPage.getNumber())
                .totalRecords(tasksPage.getTotalPages())
                .totalPages(tasksPage.getTotalPages())
                .build();
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
