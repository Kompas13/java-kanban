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




/*    HttpTaskManager taskManager = Managers.getHttpTaskManager();
        taskManager.load();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.tasksById.values());
        System.out.println(taskManager.tasksById.keySet());
        System.out.println(taskManager.getTaskById(2));*/
        HttpTaskServer server = new HttpTaskServer();
        server.start();


        //TaskManager taskManager = Managers.getDefaultTaskManager();

     /*  //Создаем задачи
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

        Epic epic4 = new Epic("my epic 4", "Description 1-2");
        taskManager.updateEpic(epic4, 4);


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
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(7);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(7);
        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(4);
        taskManager.deleteSubtaskById(6);*/
      /*  for (Task allTasksEpicAndSubtask : taskManager.getAllTasksEpicAndSubtasks()) {
            System.out.println(allTasksEpicAndSubtask);
        }*/


/*        //Выводим всё на экран:
        System.out.print("--Print all tasks--\n");
        for (Task allTasksEpicAndSubtask : taskManager.getAllTasksEpicAndSubtasks()) {
            System.out.println(allTasksEpicAndSubtask);
        }

        System.out.println("--Print history--");
        List<Task> allTasksInHistory=taskManager.getHistory();
        for (Task task1 : allTasksInHistory) {
            System.out.println(task1);
        }

        System.out.println("--Print sorted tasks by time--");
        for (Task prioritizedTask : taskManager.getPrioritizedTasks()) {
            System.out.println(prioritizedTask);
        }*/
    }
}