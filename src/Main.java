import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());


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

        Epic epic3 = new Epic("my epic 3", "Description 1-3");
        taskManager.createEpic(epic3);

        //Создаем subtasks
        taskManager.createSubtask(epic, new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#3 epic#1", "Description 1-2-2", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#4 epic#1", "Description 1-2-2", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#5 epic#1", "Description 1-2-2", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#6 epic#1", "Description 1-2-2", TaskStatus.DONE));
        taskManager.createSubtask(epic, new Subtask("subtask#7 epic#1", "Description 1-2-2", TaskStatus.DONE));
        taskManager.createSubtask(epic2, new Subtask("subtask#1 epic#2", "Description 1-2-2", TaskStatus.IN_PROGRESS));

        //Выводим всё на экран:
        System.out.print("--Print all tasks--");
        System.out.println(taskManager.getAllTasksEpicAndSubtasks());

        //Получаем задачи по ID
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getEpicById(5);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(7);
        taskManager.getSubtaskById(8);
        taskManager.getSubtaskById(9);
        taskManager.getSubtaskById(10);
        taskManager.getSubtaskById(11);

        System.out.println("--Print history--");
        List<Task> allTasksInHistory=taskManager.getHistory();
        for (Task task1 : allTasksInHistory) {
            System.out.println(task1);
        }
    }
}