import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.HttpTaskServer;
import manager.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskServer server;
    HttpClient httpClient;

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();

    @BeforeEach
    void setUp() throws IOException {
        try {
            kvServer = new KVServer();
            kvServer.start();//запускаем сервер хранящий задачи
        } catch (IOException e) {
            e.printStackTrace();
        }
        server = new HttpTaskServer();
        server.start();
        httpClient = HttpClient.newHttpClient();
        //Создаем задачи
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2021, 4, 11, 18, 1), Duration.ofHours(6));
        server.taskManager.createTask(task);

        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2021, 4, 11, 12, 0), Duration.ofHours(6));
        System.out.println(server.taskManager.checkTaskForIntersection(task2));
        server.taskManager.createTask(task2);

        //Создаем эпики
        Epic epic = new Epic("my epic 1", "Description 1-1");
        server.taskManager.createEpic(epic);

        //Создаем subtasks
        server.taskManager.createSubtask(epic, new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2021, 4, 8, 12, 0), Duration.ofHours(6)));
        server.taskManager.createSubtask(epic, new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2021, 4, 9, 9, 0), Duration.ofHours(6)));

        //Получаем задачи по ID
        server.taskManager.getTaskById(1);
        server.taskManager.getEpicById(3);
        server.taskManager.getSubtaskById(4);
        server.taskManager.deleteTaskById(1);
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    void tasksProcessor() throws IOException, InterruptedException { //tasks/task
        Task task3 = new Task("Task 3", "Description 3", TaskStatus.NEW, LocalDateTime.of(2023, 4, 11, 18, 1), Duration.ofHours(6));
        String taskToJson = gson.toJson(task3);
        HttpRequest.BodyPublisher taskBody = HttpRequest.BodyPublishers.ofString(taskToJson);
        URI uriTask = URI.create("http://localhost:8081/tasks/task");
        HttpRequest postCreateTaskRequest = HttpRequest.newBuilder()
                .uri(uriTask)
                .POST(taskBody)
                .build();
        HttpResponse<String> createEpicResponse = httpClient.send(postCreateTaskRequest
                , HttpResponse.BodyHandlers.ofString());
        assertEquals("ID созданной задачи 5", createEpicResponse.body());
        assertEquals(201, createEpicResponse.statusCode());
    }
    @Test
    void epicProcessor() throws IOException, InterruptedException {//tasks/epic
        Epic epic5 = new Epic("my epic 5", "Description 1-1");
        String taskToJson = gson.toJson(epic5);
        HttpRequest.BodyPublisher epicBody = HttpRequest.BodyPublishers.ofString(taskToJson);
        URI uriTask = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest postCreateEpicRequest = HttpRequest.newBuilder()
                .uri(uriTask)
                .POST(epicBody)
                .build();
        HttpResponse<String> createEpicResponse = httpClient.send(postCreateEpicRequest
                , HttpResponse.BodyHandlers.ofString());
        assertEquals("ID созданной задачи 5", createEpicResponse.body());
        assertEquals(201, createEpicResponse.statusCode());
    }
    @Test
    void subtaskProcessor() throws IOException, InterruptedException {//tasks/subtask
        Epic epic6 = new Epic("my epic 6", "Description 1-1");
        Subtask subtask5 = new Subtask("Subtask3", "Description 3", TaskStatus.NEW, LocalDateTime.of(2023, 4, 11, 18, 1), Duration.ofHours(6));
        String epicToJson = gson.toJson(epic6);
        String subtaskToJson = gson.toJson(subtask5);
        String taskToJson = epicToJson+"==="+subtaskToJson;
        HttpRequest.BodyPublisher subtaskBody = HttpRequest.BodyPublishers.ofString(taskToJson);
        URI uriTask = URI.create("http://localhost:8081/tasks/subtask");
        HttpRequest postCreateSubtaskRequest = HttpRequest.newBuilder()
                .uri(uriTask)
                .POST(subtaskBody)
                .build();
        HttpResponse<String> createSubtaskResponse = httpClient.send(postCreateSubtaskRequest
                , HttpResponse.BodyHandlers.ofString());
        assertEquals("ID созданной задачи 5", createSubtaskResponse.body());
        assertEquals(201, createSubtaskResponse.statusCode());
    }
    @Test
    void historyProcessor() throws IOException, InterruptedException {//tasks/history
        List<Task> history = server.taskManager.getHistory();
        String jsonHistory = gson.toJson(history);
        HttpRequest getHistoryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/tasks/history"))
                .GET()
                .build();
        HttpResponse getHistoryResponse = httpClient.send(getHistoryRequest
                , HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getHistoryResponse.statusCode());
        assertEquals(jsonHistory, getHistoryResponse.body());
    }

    @Test
    void prioritizedTaskProcessor() throws IOException, InterruptedException { //tasks
        ArrayList<Task> priority = server.taskManager.getPrioritizedTasks();
        String jsonPriority = gson.toJson(priority);
        HttpRequest getHistoryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/tasks"))
                .GET()
                .build();
        HttpResponse getHistoryResponse = httpClient.send(getHistoryRequest
                , HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getHistoryResponse.statusCode());
        assertEquals(jsonPriority, getHistoryResponse.body());
    }

}