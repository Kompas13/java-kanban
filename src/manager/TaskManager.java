package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    List<Task> getAllTasksEpicAndSubtasks();
    List<Task> getAllTasks();
    void clearAllTasks();
    Task getTaskById(int id);
    void createTask(Task task);
    void updateTask(Task task, int id);
    void deleteTaskById(int id);

    List<Task> getAllEpic();
    void clearAllEpics();
    Epic getEpicById(int id);
    void createEpic(Epic epic);
    void updateEpic(Epic epic, int id);
    void deleteEpicById(int id);

    List<Task> getAllSubtask();
    void clearAllSubtasks();
    void clearAllSubtasksFromEpic(Epic epic);
    Subtask getSubtaskById(int id);
    void createSubtask(Epic epic, Subtask subtask);
    void updateSubtask(Subtask subtask, int id);
    void deleteSubtaskById(int id);
    List<Task> getAllSubtaskFromEpic(Epic epic);
    void updateEpicStatus(Epic epic);
    void checkTimeOfEpic(Epic epic);
    ArrayList<Task> getPrioritizedTasks();
    void updateTreeSetTasksSortedByStartTime();
    boolean checkTaskForIntersection(Task task);
}
