package org.example.repository;

import org.example.model.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Repository
public class UserRepository {
    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbClient dynamoDbClient) {
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.userTable = enhancedClient.table("users", TableSchema.fromBean(User.class));
    }

    public void registerUser(User user) {
        userTable.putItem(user);
    }

    public User getUserById(String userId) {
        return userTable.getItem(Key.builder().partitionValue(userId).build());
    }

    public User findByUsername(String username) {
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User findByToken(String token) {
        System.out.println(token);
        return userTable.scan()
                .items()
                .stream()
                .filter(user -> token.equals(user.getToken()))
                .findFirst()
                .orElse(null);
    }

    public void updateUserToken(String userId, String newToken) {
        User user = getUserById(userId);
        if (user != null) {
            user.setToken(newToken);
            userTable.putItem(user);
        }
    }
}

