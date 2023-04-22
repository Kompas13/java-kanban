import manager.HttpTaskManager;
import manager.KVServer;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    KVServer kvServer;

    @BeforeEach
    void setUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();//запускаем сервер хранящий задачи
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    void saveAndLoad() {
        HttpTaskManager taskManager = Managers.getHttpTaskManager(); //1 HttpTaskManager
        //Создаем задачи
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2021, 4, 11, 18, 1), Duration.ofHours(6));
        taskManager.createTask(task);

        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2021, 4, 11, 12, 0), Duration.ofHours(6));
        System.out.println(taskManager.checkTaskForIntersection(task2));
        taskManager.createTask(task2);

        //Создаем эпики
        Epic epic = new Epic("my epic 1", "Description 1-1");
        taskManager.createEpic(epic);

        //Создаем subtasks
        taskManager.createSubtask(epic, new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2021, 4, 8, 12, 0), Duration.ofHours(6)));
        taskManager.createSubtask(epic, new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2021, 4, 9, 9, 0), Duration.ofHours(6)));

        //Получаем задачи по ID
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.deleteTaskById(1);

        HttpTaskManager taskManager2 = Managers.getHttpTaskManager(); //создаем второй HttpTaskManager, он подгружает задачи из KVServer()

        assertEquals(taskManager2.getAllTasks().toString(), taskManager.getAllTasks().toString());
        assertEquals(taskManager2.getAllEpic().toString(), taskManager.getAllEpic().toString());
        assertEquals(taskManager2.getAllSubtask().toString(), taskManager.getAllSubtask().toString());
        assertEquals(taskManager2.getAllTasksEpicAndSubtasks().toString(), taskManager.getAllTasksEpicAndSubtasks().toString());
        assertEquals(taskManager2.getHistory().toString(), taskManager.getHistory().toString());
        assertEquals(taskManager2.getPrioritizedTasks().toString(), taskManager.getPrioritizedTasks().toString());
    }
}