package com.todo.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.todo.backend.model.Task;
import com.todo.backend.repository.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Transactional
    public Task markTaskAsComplete(Task task) {
        return changeCompletedStatus(task, true);
    }

    @Transactional
    public Task markTaskAsComplete(Long id) {
        Task taskToUpdate = taskRepository.findById(id).orElse(null);

        if(taskToUpdate == null) {
            throw new IllegalArgumentException("Task with id " + id + " not found");
        } 

        return markTaskAsComplete(taskToUpdate);
    }

    @Transactional
    public Task markTaskAsIncomplete(Task task) {
        return changeCompletedStatus(task, false);
    }

    @Transactional
    public Task markTaskAsIncomplete(Long id) {
        Task taskToUpdate = taskRepository.findById(id).orElse(null);

        if(taskToUpdate == null) {
            throw new IllegalArgumentException("Task with id " + id + " not found");
        } 

        return markTaskAsComplete(taskToUpdate);
    }

    @Transactional
    public Task changeCompletedStatus(Task task, boolean status) {
        task.setCompleted(status);
        return taskRepository.save(task);
    }

    @Transactional
    public Task changeDescription(Task task, String description) {
        task.setDescription(description);
        return taskRepository.save(task);
    }

    @Transactional
    public Task changeDescription(Long id, String description) {
        Task taskToUpdate = taskRepository.findById(id).orElse(null);

        if(taskToUpdate == null) {
            throw new IllegalArgumentException("Task with id " + id + " not found");
        } 

        return changeDescription(taskToUpdate, description);
    }

    @Transactional
    public Task createTask(Task task) {
        /*
        if(taskRepository.findByTitle(task.getTitle())) {
            throw new IllegalArgumentException("Title already taken");
        }
        */
       return taskRepository.save(task);
    }

    @Transactional
    public Task deleteTaskById(Long id) {
        Task taskToDelete = taskRepository.findById(id).orElse(null);

        if(taskToDelete == null) {
            throw new IllegalArgumentException("Task with id " + id + " not found");
        } else {
            taskRepository.deleteById(id);
            return taskToDelete;
        }
    }
}
