package main;

import manager.*;
import tasks.Epic;
import tasks.Task;
import tasks.TaskStatus;
import tasks.Subtask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
       HttpTaskServer server = new HttpTaskServer();
        server.start();
/*
    //----оставлено для ручного тестирования работы программы----
        HttpTaskManager taskManager = Managers.getHttpTaskManager();
        taskManager.load();

      //Создаем задачи
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2021, 4, 11, 18, 1), Duration.ofHours(6));
        taskManager.createTask(task);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2021, 4, 11, 12, 0), Duration.ofHours(6));
        System.out.println(taskManager.checkTaskForIntersection(task2));
        taskManager.createTask(task2);
        //Создаем эпики
        Epic epic = new Epic("my epic 1", "Description 1-1");
        taskManager.createEpic(epic);
        Epic epic2 = new Epic("my epic 2", "Description 1-2");
        taskManager.createEpic(epic2);
        //Создаем subtasks
        taskManager.createSubtask(epic, new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2021, 4, 8, 12, 0), Duration.ofHours(6)));
        taskManager.createSubtask(epic, new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2021, 4, 9, 9, 0), Duration.ofHours(6)));
        taskManager.createSubtask(epic, new Subtask("subtask#3 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2021, 4, 10, 12, 0), Duration.ofHours(6)));
        //Получаем задачи по ID
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.deleteTaskById(1);

 */   }

}