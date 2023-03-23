package main;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTaskManager();

        //Создаем задачи
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);

        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW);
        taskManager.createTask(task2);

        //Создаем эпики
        Epic epic = new Epic("my epic 1", "Description 1-1");
        taskManager.createEpic(epic);

        Epic epic2 = new Epic("my epic 2", "Description 1-2");
        taskManager.createEpic(epic2);

        //Создаем subtasks
        taskManager.createSubtask(epic, new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#3 epic#1", "Description 1-2-2", TaskStatus.DONE));

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
        //taskManager.deleteSubtaskById(7);//удаляем Subtask
        //taskManager.deleteEpicById(4); //удаляем Epic
        //taskManager.deleteEpicById(3);//удаляем Epic
        taskManager.clearAllSubtasksFromEpic(epic);

        //Выводим всё на экран:
        System.out.print("--Print all tasks--\n");
        for (Task allTasksEpicAndSubtask : taskManager.getAllTasksEpicAndSubtasks()) {
            System.out.println(allTasksEpicAndSubtask);
        }

        System.out.println("--Print history--");
        List<Task> allTasksInHistory=taskManager.getHistory();
        for (Task task1 : allTasksInHistory) {
            System.out.println(task1);
        }
    }
}