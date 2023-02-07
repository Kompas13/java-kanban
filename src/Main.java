import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

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
        Subtask subtask1Epic1 = new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE);
        taskManager.createSubtask(epic, subtask1Epic1);

        Subtask subtask2Epic1 = new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE);
        taskManager.createSubtask(epic, subtask2Epic1);

        Subtask subtask1Epic2 = new Subtask("subtask#1 for epic2","Description 2-1-3", TaskStatus.IN_PROGRESS);
        taskManager.createSubtask(epic2, subtask1Epic2);

        //Выводим всё на экран:
        System.out.println("---");
        System.out.println(taskManager.getAllTasksEpicAndSubtasks());
        System.out.println("---");



        //Обновляем задачу subtask2:
        System.out.println("\nRefresh task subtask2Epic1:");
        subtask2Epic1.setTitle("subtask#2_NEW_epic1");
        subtask2Epic1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2Epic1);
        System.out.println("Tasks"+taskManager.getAllTasks());
        System.out.println("\nPrint epics: " + taskManager.getAllEpic());
        System.out.println("\nPrint epic 1 subtasks:" + taskManager.getAllSubtaskFromEpic(epic));

        //Обновляем Epic
        System.out.println("\nRefresh Epic1:");
        epic.setDescription("Ololo");
        taskManager.updateEpic(epic);

        //Выводим всё на экран:
        System.out.println("---1");
        System.out.println(taskManager.getAllTasksEpicAndSubtasks());
        System.out.println("---2");


        //Удаляем subtask 2 и epic 3 по ID:
        System.out.println("\ndelite subtask#2 and epic 3:");
        taskManager.deleteEpicById(5);
        taskManager.deleteSubtaskById(7);
        System.out.println(taskManager.getAllTasksEpicAndSubtasks());

        //удаляем все Subtask входящие в epic
        System.out.println("\ndelite all subtask from epic:");
        taskManager.clearAllSubtasksFromEpic(epic);
        System.out.println(taskManager.getAllTasksEpicAndSubtasks());
    }
}