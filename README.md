# Serverless Todo-Task Manager

This project is a simple **serverless** application built using **Spring Boot**, **AWS Lambda**, and **DynamoDB** to manage **To-Do** tasks. The app is designed to be deployed on AWS and offers basic operations like creating, reading, updating, deleting tasks, and user registration/login.

## Features

- **Serverless** architecture using **AWS Lambda** and **API Gateway**.
- **DynamoDB** as the database to store tasks.
- Basic **CRUD** operations for To-Do tasks.
- **User registration** and **login** functionality.
- **Authorization** for task ownership.
- **Authentication** of CRUD operations using user tokens.
- Built with **Spring Boot** for simplicity and maintainability.

## Prerequisites

Before running the application locally or deploying it to AWS, ensure you have the following tools installed:

- **Java 11+**
- **Maven 3+**
- **AWS CLI** (configured with your AWS credentials)

## Project Setup

1. **Clone the Repository**

   ```bash
   git clone https://github.com/teapotka/todo_serverless.git
   cd todo_serverless
   ```

2. **Build the Project**

   Use Maven to build the project:

   ```bash
   mvn clean package
   ```

3. **Configure AWS Credentials**
   
   Ensure that your AWS CLI is configured properly to access your AWS account:

    ```bash 
    aws configure
    ```
    This will prompt for your AWS Access Key, Secret Key, and region.

## API Endpoints
The following endpoints are available:

### User Operations
- POST /register: Register a new user
- POST /login: Login as an existing user

### Tasks Operations

>Note: **Authentication token is required**

- GET /tasks: Retrieve all to-do tasks.
- POST /tasks: Create a new to-do task.
- GET /tasks/{id}: Retrieve a specific to-do task by ID.
- PUT /tasks/{id}: Update a to-do task by ID.
- DELETE /tasks/{id}: Delete a to-do task by ID.

