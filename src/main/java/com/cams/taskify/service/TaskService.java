package com.cams.taskify.service;

import com.cams.taskify.DTO.Task.CreateTaskDTO;
import com.cams.taskify.DTO.Task.PatchTaskDTO;
import com.cams.taskify.DTO.Task.TaskDetailsDTO;
import com.cams.taskify.response.PaginatedResponse;
import com.cams.taskify.response.TaskListResponse;

import org.springframework.data.domain.Pageable;

public interface TaskService {

    PaginatedResponse<TaskListResponse> getTasks(Pageable pageable);

    TaskDetailsDTO getTask(long id);

    TaskDetailsDTO createTask(CreateTaskDTO createTaskDTO);

    TaskDetailsDTO updateTask(long id, PatchTaskDTO patchTaskDTO);

}
