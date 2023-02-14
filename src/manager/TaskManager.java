package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    String getAllTasksEpicAndSubtasks();
    String getAllTasks();
    void clearAllTasks();
    Task getTaskById(int id);
    void createTask(Task task);
    void updateTask(Task task);
    void deleteTaskById(int id);

    String getAllEpic();
    void clearAllEpics();
    Task getEpicById(int id);
    void createEpic(Epic epic);
    void updateEpic(Epic epic);
    void deleteEpicById(int id);

    String getAllSubtask();
    void clearAllSubtasks();
    void clearAllSubtasksFromEpic(Epic epic);
    Task getSubtaskById(int id);
    void createSubtask(Epic epic, Subtask subtask);
    void updateSubtask(Subtask subtask);
    void deleteSubtaskById(int id);
    String getAllSubtaskFromEpic(Epic epic);
}
