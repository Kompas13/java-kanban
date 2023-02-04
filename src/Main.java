import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        //Создаем задачи
        Task task = new Task();
        task.setTitle("Task 1");
        task.setStatus(TaskStatus.NEW);
        taskManager.createTask(task);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setStatus(TaskStatus.NEW);
        taskManager.createTask(task2);

        //Создаем эпики
        Epic epic = new Epic();
        epic.setTitle("my epic 1");
        taskManager.createEpic(epic);

        Epic epic2 = new Epic();
        epic2.setTitle("my epic 2");
        taskManager.createEpic(epic2);

        Epic epic3 = new Epic();
        epic3.setTitle("my epic 3");
        taskManager.createEpic(epic3);

        //Создаем subtasks
        Subtask subtask1Epic1 = new Subtask();
        subtask1Epic1.setTitle("subtask#1 epic#1");
        subtask1Epic1.setStatus(TaskStatus.DONE);
        taskManager.createSubtask(epic, subtask1Epic1);

        Subtask subtask2Epic1 = new Subtask();
        subtask2Epic1.setTitle("subtask#2 epic#1");
        subtask2Epic1.setStatus(TaskStatus.DONE);
        taskManager.createSubtask(epic, subtask2Epic1);

        Subtask subtask1Epic2 = new Subtask();
        subtask1Epic2.setTitle("subtask#1 for epic2");
        subtask1Epic2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createSubtask(epic2, subtask1Epic2);

        //Выводим всё на экран:
        System.out.println("\nPrint tasks: " + taskManager.getAllTasks());
        System.out.println("\nPrint epics: " + taskManager.getAllEpic());
        System.out.println("\nPrint epic 1 subtasks: " + taskManager.getAllSubtaskFromEpic(epic));
        System.out.println("\nPrint epic 2 subtasks: " + taskManager.getAllSubtaskFromEpic(epic2));

        //Обновляем задачу subtask2:
        System.out.println("\nRefresh task subtask2Epic1:");
        subtask2Epic1.setTitle("subtask#2_NEW_epic1");
        subtask2Epic1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2Epic1);
        System.out.println("\nPrint epics: " + taskManager.getAllEpic());
        System.out.println("\nPrint epic 1 subtasks:" + taskManager.getAllSubtaskFromEpic(epic));

        //Удаляем subtask 2 и epic 3:
        System.out.println("\ndelite subtask#2 and epic 3:");
        taskManager.deleteEpicById(5);
        taskManager.deleteSubtaskById(7);
        System.out.println("\nPrint epics: " + taskManager.getAllEpic());
        System.out.println("\nPrint epic 1 subtasks: " + taskManager.getAllSubtaskFromEpic(epic));
        System.out.println("\nPrint epic 2 subtasks: " + taskManager.getAllSubtaskFromEpic(epic2));
    }
}