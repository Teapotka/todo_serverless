package org.example.repository;

import org.example.model.Task;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Task> taskTable;

    public TaskRepository(DynamoDbClient dynamoDbClient) {
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.taskTable = enhancedClient.table("tasks", TableSchema.fromBean(Task.class));
    }

    public void saveTask(Task task) {
        taskTable.putItem(task);
    }

    public Task getTask(String userId, String id) {
        return taskTable.getItem(r -> r.key(Key.builder().partitionValue(userId).sortValue(id).build()));
    }

    public List<Task> getAllTasks(String userId) {
        return taskTable.query(QueryConditional.keyEqualTo(Key.builder().partitionValue(userId).build()))
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public String updateTask(String userId, Task updatedTask) {
        Task existingTask = getTask(userId, updatedTask.getId());
        if (existingTask != null) {
            taskTable.putItem(updatedTask);
            return updatedTask.getId();
        }
        return null;
    }

    public String deleteTask(String userId, String taskId) {
        Task task = taskTable.deleteItem(Key.builder().partitionValue(userId).sortValue(taskId).build());
        if (task != null) {
            return task.getId();
        }
        return null;
    }
}
