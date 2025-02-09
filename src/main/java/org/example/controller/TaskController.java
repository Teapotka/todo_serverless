package org.example.controller;

import org.example.dto.TaskPayloadDto;
import org.example.dto.UserRegistrationDto;
import org.example.model.Task;
import org.example.model.User;
import org.example.service.TaskService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping()
    public String hello() {
        return "Hello";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto user) {
        String token = userService.register(user.getUsername(), user.getPassword());
        return ResponseEntity.ok("User registered successfully. Token: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRegistrationDto user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok("Login successful. Token: " + token);
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(
            @RequestHeader("Authorization") String token,
            @RequestBody TaskPayloadDto task) {
        User user = userService.authenticate(token);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        taskService.createTask(user.getId(), task);
        return ResponseEntity.ok("Task created successfully!");
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTask(
            @RequestHeader("Authorization") String token,
            @PathVariable String id) {
        User user = userService.authenticate(token);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        Task task = taskService.getTask(user.getId(), id);
        if (task == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.authenticate(token);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        List<Task> tasks = taskService.getAllTasks(user.getId());
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> updateTask(
            @RequestHeader("Authorization") String token,
            @PathVariable String id,
            @RequestBody TaskPayloadDto updatedTask
    ) {
        User user = userService.authenticate(token);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        String taskId = taskService.updateTask(user.getId(), id, updatedTask);
        if (taskId == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        return ResponseEntity.ok("Task updated successfully!");
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(
            @RequestHeader("Authorization") String token,
            @PathVariable String id) {
        User user = userService.authenticate(token);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        String taskId = taskService.deleteTask(user.getId(), id);
        if (taskId == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        return ResponseEntity.ok("Task deleted successfully!");
    }

}