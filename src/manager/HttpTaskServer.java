package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8081;
    public TaskManager taskManager;
    private Gson gson;
    private HttpServer server;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext(String.valueOf(Endpoint.TASKS), this::tasksProcessor);
        server.createContext(String.valueOf(Endpoint.EPICS), this::epicProcessor);
        server.createContext(String.valueOf(Endpoint.SUBTASKS), this::subtaskProcessor);
        server.createContext(String.valueOf(Endpoint.HISTORY), this::historyProcessor);
        server.createContext(String.valueOf(Endpoint.EPIC_SUBTASK), this::epicSubtaskProcessor);
        server.createContext(String.valueOf(Endpoint.PRIORITY), this::prioritizedTaskProcessor);
        taskManager = Managers.getHttpTaskManager();

        gson = new GsonBuilder()
                .serializeNulls()
                .create();
    }

    public void start() {
        server.start();
    }

    private void tasksProcessor(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String query = h.getRequestURI().getQuery();
        switch (method){
            case "GET":
                if(query==null) {
                    List<Task> taskList = taskManager.getAllTasks();
                    if (taskList != null) {
                        String responseBody = gson.toJson(taskList);
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(204, 0);
                    }
                }

                assert query != null;
                if(query.startsWith("id=")){
                    String[] strings = query.split("&")[0].split("=");
                    int id = Integer.parseInt(strings[1]);
                    Task task = taskManager.getTaskById(id);
                    if (task!=null){
                        String responseBody = gson.toJson(task);
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(204, 0);
                    }
                }

                else{
                    h.sendResponseHeaders(404, 0);
                }
                h.close();
                break;

            case "POST":
                InputStream inputStream = h.getRequestBody();
                String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task taskFromJson = gson.fromJson(requestBody, Task.class);
                if (taskFromJson != null) {
                    System.out.println("������� ����������");
                    taskManager.createTask(taskFromJson);
                    h.sendResponseHeaders(201, 0);
                    String responseBody = "ID ��������� ������ "+ taskFromJson.getId();
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                    }
                }
                else {
                    h.sendResponseHeaders(422, 0);
                    String responseBody = "������ ������������ ������ ������";
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                    }
                }
                h.close();
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.clearAllTasks();
                    h.sendResponseHeaders(200, 0);
                }
                assert query != null;
                if (query.startsWith("id=")) {
                    String[] strings = query.split("&")[0].split("=");
                    int id = Integer.parseInt(strings[1]);

                    if (taskManager.getTaskById(id) != null) {
                        taskManager.deleteTaskById(id);
                        h.sendResponseHeaders(200, 0);
                        String responseBody = gson.toJson("������ �������");
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                        }
                    } else {
                        h.sendResponseHeaders(404, 0);
                    }
                }
                else {
                    h.sendResponseHeaders(400, 0);
                }

                h.close();
                break;

            default:
                h.sendResponseHeaders(501, 0);
                h.close();
                break;
        }
    }

    private void epicProcessor(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String query = h.getRequestURI().getQuery();
        switch (method){
            case "GET":
                if(query==null) {
                    List<Task> epicList = taskManager.getAllEpic();
                    if (epicList != null) {
                        String responseBody = gson.toJson(epicList);
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(204, 0);
                    }
                }

                assert query != null;
                if(query.startsWith("id=")){
                    String[] strings = query.split("&")[0].split("=");
                    int id = Integer.parseInt(strings[1]);
                    Epic epic = taskManager.getEpicById(id);
                    if (epic!=null){
                        String responseBody = gson.toJson(epic);
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(204, 0);
                    }
                }
                else{
                    h.sendResponseHeaders(404, 0);
                }
                h.close();
                break;

            case "POST":
                InputStream inputStream = h.getRequestBody();
                String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic epicFromJson = gson.fromJson(requestBody, Epic.class);
                if (epicFromJson != null) {
                    taskManager.createEpic(epicFromJson);
                    h.sendResponseHeaders(201, 0);
                    String responseBody = "ID ��������� ������ "+ epicFromJson.getId();
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                    }
                }
                else {
                    h.sendResponseHeaders(422, 0);
                    String responseBody = "������ ������������ ������ ������";
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                    }
                }
                h.close();
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.clearAllEpics();
                    h.sendResponseHeaders(200, 0);
                }
                assert query != null;
                if (query.startsWith("id=")) {
                    String[] strings = query.split("&")[0].split("=");
                    int id = Integer.parseInt(strings[1]);

                    if (taskManager.getEpicById(id) != null) {
                        taskManager.deleteEpicById(id);
                        h.sendResponseHeaders(200, 0);
                        String responseBody = gson.toJson("������ �������");
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                        }
                    } else {
                        h.sendResponseHeaders(404, 0);
                    }
                }
                else {
                    h.sendResponseHeaders(400, 0);
                }
                h.close();
                break;

            default:
                h.sendResponseHeaders(501, 0);
                h.close();
                break;
        }
    }

    private void subtaskProcessor(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String query = h.getRequestURI().getQuery();
        switch (method){
            case "GET":
                if(query==null) {
                    List<Task> subtaskList = taskManager.getAllSubtask();
                    if (subtaskList != null) {
                        String responseBody = gson.toJson(subtaskList);
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(204, 0);
                    }
                }

                assert query != null;
                if(query.startsWith("id=")){
                    String[] strings = query.split("&")[0].split("=");
                    int id = Integer.parseInt(strings[1]);
                    Subtask subtask = taskManager.getSubtaskById(id);
                    if (subtask!=null){
                        String responseBody = gson.toJson(subtask);
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes());
                        }
                    } else {
                        h.sendResponseHeaders(204, 0);
                    }
                }
                else{
                    h.sendResponseHeaders(404, 0);
                }
                h.close();
                break;

            case "POST":
                InputStream inputStream = h.getRequestBody();
                String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                String epic = requestBody.split("===")[0];
                String subtask = requestBody.split("===")[1];
                Epic epicFromJson = gson.fromJson(epic, Epic.class);
                Subtask subtaskFromJson = gson.fromJson(subtask, Subtask.class);
                if (subtaskFromJson != null) {
                    taskManager.createSubtask(epicFromJson, subtaskFromJson);
                    h.sendResponseHeaders(201, 0);
                    String responseBody = "ID ��������� ������ "+ subtaskFromJson.getId();
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                    }
                }
                else {
                    h.sendResponseHeaders(422, 0);
                    String responseBody = "������ ������������ ������ ������";
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                    }
                }
                h.close();
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.clearAllSubtasks();
                    h.sendResponseHeaders(200, 0);
                }
                assert query != null;
                if (query.startsWith("id=")) {
                    String[] strings = query.split("&")[0].split("=");
                    int id = Integer.parseInt(strings[1]);

                    if (taskManager.getSubtaskById(id) != null) {
                        taskManager.deleteSubtaskById(id);
                        h.sendResponseHeaders(200, 0);
                        String responseBody = gson.toJson("������ �������");
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                        }
                    } else {
                        h.sendResponseHeaders(404, 0);
                    }
                }
                else {
                    h.sendResponseHeaders(400, 0);
                }
                h.close();
                break;

            default:
                h.sendResponseHeaders(501, 0);
                h.close();
                break;
        }
    }

    private void historyProcessor(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String query = h.getRequestURI().getQuery();
        if ("GET".equals(method)) {
            if (query == null) {
                List<Task> taskList = taskManager.getHistory();
                String responseBody = gson.toJson(taskList);
                h.sendResponseHeaders(200, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write(responseBody.getBytes());
                }
            } else {
                h.sendResponseHeaders(404, 0);
            }
        } else {
            h.sendResponseHeaders(501, 0);
        }
        h.close();
    }

    private void epicSubtaskProcessor(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String query = h.getRequestURI().getQuery();
        if ("GET".equals(method)) {
            if (query == null) {
                h.sendResponseHeaders(422, 0);
                String responseBody = "������ ������������ ������ ������";
                try (OutputStream os = h.getResponseBody()) {
                    os.write(responseBody.getBytes(StandardCharsets.UTF_8));
                }
            }

            assert query != null;
            if (query.startsWith("id=")) {
                String[] strings = query.split("&")[0].split("=");
                int id = Integer.parseInt(strings[1]);
                List<Task> allSubtaskFromEpic = taskManager.getAllSubtaskFromEpic(taskManager.getEpicById(id));
                if (allSubtaskFromEpic != null) {
                    String responseBody = gson.toJson(allSubtaskFromEpic);
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(responseBody.getBytes());
                    }
                } else {
                    h.sendResponseHeaders(204, 0);
                }
            } else {
                h.sendResponseHeaders(404, 0);
            }
            h.close();
        }
    }

    private void prioritizedTaskProcessor(HttpExchange h) throws IOException {
        taskManager.updateTreeSetTasksSortedByStartTime();
        String method = h.getRequestMethod();
        String query = h.getRequestURI().getQuery();
        if ("GET".equals(method)) {
            if (query == null) {
                List<Task> taskList = taskManager.getPrioritizedTasks();
                String responseBody = gson.toJson(taskList);
                h.sendResponseHeaders(200, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write(responseBody.getBytes());
                }
            } else {
                h.sendResponseHeaders(404, 0);
            }
        } else {
            h.sendResponseHeaders(501, 0);
        }
        h.close();
    }

    public void stop(){
        server.stop(1);
    }
}

