package com.cams.taskify.repository;

import com.cams.taskify.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAssignedTo(long empId, Pageable pageable);
}
