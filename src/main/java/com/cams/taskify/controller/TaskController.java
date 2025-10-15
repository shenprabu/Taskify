package com.cams.taskify.controller;

import com.cams.taskify.DTO.Task.CreateTaskDTO;
import com.cams.taskify.DTO.Task.PatchTaskDTO;
import com.cams.taskify.DTO.Task.TaskDetailsDTO;
import com.cams.taskify.response.PaginatedResponse;
import com.cams.taskify.response.TaskListResponse;
import com.cams.taskify.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.cams.taskify.constants.RouteConstants.TASKS_API_BASE_URL;
import static com.cams.taskify.constants.RouteConstants.TASKS_API_ID_URL;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping(TASKS_API_BASE_URL)
    public PaginatedResponse<TaskListResponse>  getTasks(Pageable pageable) {
        return taskService.getTasks(pageable);
    }

    @GetMapping(TASKS_API_ID_URL)
    public TaskDetailsDTO getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    @PostMapping(TASKS_API_BASE_URL)
    public TaskDetailsDTO createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        return taskService.createTask(createTaskDTO);
    }

    @PatchMapping(TASKS_API_ID_URL)
    public TaskDetailsDTO updateTask(@PathVariable long id, @Valid @RequestBody PatchTaskDTO patchTaskDTO) {
        return taskService.updateTask(id, patchTaskDTO);
    }
}
