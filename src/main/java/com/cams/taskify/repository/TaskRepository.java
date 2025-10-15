package com.cams.taskify.repository;

import com.cams.taskify.constants.TaskStatus;
import com.cams.taskify.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAssignedTo(long empId, Pageable pageable);

    Page<Task> findByAssignedToAndStatus(long empId, TaskStatus status, Pageable pageable);

    List<Task> findByAssignedTo(long empId);
}
