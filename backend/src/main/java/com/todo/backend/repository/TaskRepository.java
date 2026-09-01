package com.todo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todo.backend.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
