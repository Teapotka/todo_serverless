package org.example.service;

import org.example.dto.TaskPayloadDto;
import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void createTask(String userId, TaskPayloadDto taskDto) {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setUserId(userId);
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.getCompleted());
        taskRepository.saveTask(task);
    }

    public Task getTask(String userId, String id) {
        return taskRepository.getTask(userId, id);
    }

    public List<Task> getAllTasks(String userId) {
        return taskRepository.getAllTasks(userId);
    }

    public String updateTask(String userId, String id, TaskPayloadDto taskDto) {
        Task task = new Task();
        task.setId(id);
        task.setUserId(userId);
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.getCompleted());
        return taskRepository.updateTask(userId, task);
    }

    public String deleteTask(String userId, String id) {
        return taskRepository.deleteTask(userId,id);
    }
}
