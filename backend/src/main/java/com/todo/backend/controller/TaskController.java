package com.todo.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todo.backend.model.Task;
import com.todo.backend.service.TaskService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> all() {
        return taskService.findAll();
    }

    @GetMapping("/tasks/{id}")
    public Task one(@PathVariable Long id) {
        return taskService.findTaskById(id);
    }

    @PostMapping("/tasks")
    public Task newTask(@RequestBody Task newTask) {
        return taskService.createTask(newTask);
    }

    @PutMapping("/tasks/{id}/complete")
    public Task completeTask(@PathVariable Long id) {
        return taskService.markTaskAsComplete(id);
    }

    @PutMapping("/tasks/{id}/incomplete")
    public Task notCompleteTask(@PathVariable Long id) {
        return taskService.markTaskAsIncomplete(id);
    }

    @DeleteMapping("/tasks/{id}")
    public Task deleteTask(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }
}
