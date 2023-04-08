import manager.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {

    private T taskManager;
    abstract T createTaskManager();
    Task task;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void setUp() {
        try (Writer fileWriter = new FileWriter("src/resources/saveData.CSV")) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic,");
        }
        catch (IOException e){
            throw new ManagerSaveException("Ошибка записи файла");
        }
        taskManager = createTaskManager();
        task = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2024, 4, 8, 12, 0), Duration.ofHours(6));
        taskManager.createTask(task);
        task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2024, 5, 9, 12, 0), Duration.ofHours(6));
        taskManager.createTask(task2);
        epic1 = new Epic("Test epic 1", "Test Description 1");
        taskManager.createEpic(epic1);
        epic2 = new Epic("Test epic 2", "Test Description 2");
        taskManager.createEpic(epic2);
        subtask1 = new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2024, 4, 10, 12, 0), Duration.ofHours(6));
        taskManager.createSubtask(epic1, subtask1);
        subtask2 = new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2024, 4, 11, 12, 0), Duration.ofHours(6));
        taskManager.createSubtask(epic1, subtask2);

    }

    @Test
    void getHistoryTest(){
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        assertEquals("[Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}, Task{id=3, title='Test epic 1', description='Test Description 1, status=DONE, startTime=2024-04-10T12:00, duration=PT12H,endTime=2024-04-11T18:00}]",
                taskManager.getHistory().toString(), "Ошибка получения истории");

    }

    @Test
    void getHistoryIfHistoryIsEmpty(){
        assertEquals("[]", taskManager.getHistory().toString(), "Ошибка, история не пустая");
    }

    @Test
    void getHistoryIfDoubleRequest(){
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        assertEquals("[Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}]",
                taskManager.getHistory().toString(), "Ошибка получения истории");
    }

    @Test
    void getHistoryIfAllTasksDelAfterAdd(){
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.clearAllTasks();
        assertEquals("[]",
                taskManager.getHistory().toString(), "Ошибка получения истории");
    }

    @Test
    void getHistoryIfLastTaskDel(){
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.deleteTaskById(2);
        assertEquals("[Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}]",
                taskManager.getHistory().toString(), "Ошибка получения истории");
    }

    @Test
    void getHistoryIfFirstTaskDel(){
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.deleteTaskById(1);
        assertEquals("[Task{id=2, title='Task 2', description='Description 2, status=NEW, startTime=2024-05-09T12:00, duration=PT6H,endTime=2024-05-09T18:00}]",
                taskManager.getHistory().toString(), "Ошибка получения истории");
    }

    @Test
    void getAllTasksEpicAndSubtasksTest(){
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2021, 4, 16, 12, 0), Duration.ofHours(6));
        taskManager.createTask(task);
        Epic epic1 = new Epic("Test epic 1", "Test Description 1");
        taskManager.createEpic(epic1);
        assertEquals("[Task{id=7, title='Task 1', description='Description 1, status=NEW, startTime=2021-04-16T12:00, duration=PT6H,endTime=2021-04-16T18:00}, Task{id=8, title='Test epic 1', description='Test Description 1, status=NEW, startTime=null, duration=null,endTime=null}]",
                taskManager.getAllTasksEpicAndSubtasks().toString(), "Неверное получение задач, эпиков и сабтасков");
    }

    @Test
    void getAllTasksTest() {
        assertEquals("[Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}, Task{id=2, title='Task 2', description='Description 2, status=NEW, startTime=2024-05-09T12:00, duration=PT6H,endTime=2024-05-09T18:00}]",
                taskManager.getAllTasks().toString(), "Ошибка получения всех задач");
    }

    @Test
    void clearAllTasksTest(){
        taskManager.clearAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Неверно удаляются задачи");
    }

    @Test
    void getTaskByIdTest(){
        assertEquals("Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}",
                taskManager.getTaskById(1).toString(), "Неверно выводятся задачи по ID");
    }

    @Test
    void createTaskTest(){
        taskManager.clearAllTasks();
        Task task3 = new Task("Task 3", "Description 3", TaskStatus.NEW, LocalDateTime.of(2021, 4, 14, 12, 0), Duration.ofHours(6));
        taskManager.createTask(task3);
        assertEquals("Task{id=7, title='Task 3', description='Description 3, status=NEW, startTime=2021-04-14T12:00, duration=PT6H,endTime=2021-04-14T18:00}",
                taskManager.getTaskById(7).toString(), "Неверно создаются задачи");
    }

    @Test
    void updateTaskTest(){
        Task task3 = new Task("Task 3", "Description 3", TaskStatus.NEW, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        taskManager.updateTask(task3, 1);
        assertEquals("Task{id=1, title='Task 3', description='Description 3, status=NEW, startTime=2021-04-15T12:00, duration=PT6H,endTime=2021-04-15T18:00}",
                taskManager.getTaskById(1).toString(), "Неверно происходит обновление задач");
    }

    @Test
    void deleteTaskByIdTest(){
        taskManager.deleteTaskById(2);
        assertEquals("[Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}]",
                taskManager.getAllTasks().toString(), "Неверно происходит удаление задач");
    }


    @Test
    void getAllEpicsTest() {
        assertEquals("[Task{id=3, title='Test epic 1', description='Test Description 1, status=DONE, startTime=2024-04-10T12:00, duration=PT12H,endTime=2024-04-11T18:00}, Task{id=4, title='Test epic 2', description='Test Description 2, status=NEW, startTime=null, duration=null,endTime=null}]",
                taskManager.getAllEpic().toString(), "Неверно выводятся все Epics");
    }

    @Test
    void clearAllEpicsTest(){
        taskManager.clearAllEpics();
        assertTrue(taskManager.getAllEpic().isEmpty(), "Неверная очистка Epics");
    }

    @Test
    void getEpicByIdTest(){
        assertEquals("Task{id=3, title='Test epic 1', description='Test Description 1, status=DONE, startTime=2024-04-10T12:00, duration=PT12H,endTime=2024-04-11T18:00}",
                taskManager.getEpicById(3).toString(), "Неверное получение Epic по Id");
    }

    @Test
    void createEpicTest(){
        taskManager.clearAllEpics();
        Epic epic3 = new Epic("New testEpic", "Test Description");
        taskManager.createEpic(epic3);
        assertEquals("Task{id=7, title='New testEpic', description='Test Description, status=NEW, startTime=null, duration=null,endTime=null}",
                taskManager.getEpicById(7).toString(), "Epic создается неверно");
    }

    @Test
    void updateEpicTest(){
        Epic epic3 = new Epic("Test epic 2 NEW", "Test Description NEW");
        taskManager.updateEpic(epic3, 4);
        assertEquals("Task{id=4, title='Test epic 2 NEW', description='Test Description NEW, status=NEW, startTime=null, duration=null,endTime=null}",
                taskManager.getEpicById(4).toString(), "Epic обновляется неверно");
    }

    @Test
    void deleteEpicByIdTest(){
        taskManager.deleteEpicById(3);
        assertEquals("[Task{id=4, title='Test epic 2', description='Test Description 2, status=NEW, startTime=null, duration=null,endTime=null}]",
                taskManager.getAllEpic().toString(),"Некорректное удаление Epic по Id");
    }

    @Test
    void getAllSubtasksTest() {
        assertEquals("[Task{id=5, title='subtask#1 epic#1', description='Description 1-1-1, status=DONE, startTime=2024-04-10T12:00, duration=PT6H,endTime=2024-04-10T18:00}, Task{id=6, title='subtask#2 epic#1', description='Description 1-2-2, status=DONE, startTime=2024-04-11T12:00, duration=PT6H,endTime=2024-04-11T18:00}]",
                taskManager.getAllSubtask().toString(), "Неверный вывод Subtasks");
    }

    @Test
    void clearAllSubtasksTest(){
        taskManager.clearAllSubtasks();
        assertTrue(taskManager.getAllSubtask().isEmpty(), "Неверное удаление всех Subtasks");
    }

    @Test
    void clearAllSubtasksFromEpic(){
        taskManager.clearAllSubtasksFromEpic(epic1);
        assertTrue(taskManager.getAllSubtaskFromEpic(epic1).isEmpty(), "Неверное удаление всех Subtasks из Epic");
    }

    @Test
    void getSubtaskByIdTest() {
        assertEquals("Task{id=6, title='subtask#2 epic#1', description='Description 1-2-2, status=DONE, startTime=2024-04-11T12:00, duration=PT6H,endTime=2024-04-11T18:00}",
                taskManager.getSubtaskById(6).toString(), "Неверный вывод Subtask по Id");
    }

    @Test
    void createSubtaskTest(){
        taskManager.clearAllSubtasks();
        Subtask subtask = new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2021, 4, 17, 12, 0), Duration.ofHours(6));
        taskManager.createSubtask(epic1, subtask);
        assertEquals("[Task{id=7, title='subtask#1 epic#1', description='Description 1-1-1, status=DONE, startTime=2021-04-17T12:00, duration=PT6H,endTime=2021-04-17T18:00}]",
                taskManager.getAllSubtask().toString(), "Неверное создание Subtask");
    }

    @Test
    void updateSubtaskTest(){
        Subtask subtask = new Subtask("subtaskTest epic#1", "Description", TaskStatus.DONE, LocalDateTime.of(2021, 4, 18, 12, 0), Duration.ofHours(6));
        taskManager.updateSubtask(subtask, 5);
        assertEquals("Task{id=5, title='subtaskTest epic#1', description='Description, status=DONE, startTime=2021-04-18T12:00, duration=PT6H,endTime=2021-04-18T18:00}",
                taskManager.getSubtaskById(5).toString(), "Некорректное обновление Subtask");
    }

    @Test
    void deleteSubtaskByIdTest(){
        taskManager.deleteSubtaskById(5);
        assertEquals("[Task{id=6, title='subtask#2 epic#1', description='Description 1-2-2, status=DONE, startTime=2024-04-11T12:00, duration=PT6H,endTime=2024-04-11T18:00}]",
                taskManager.getAllSubtask().toString(), "Некорректное удаление Subtask по Id");
    }

    @Test
    void getAllSubtaskFromEpicTest(){
        assertEquals("[Task{id=5, title='subtask#1 epic#1', description='Description 1-1-1, status=DONE, startTime=2024-04-10T12:00, duration=PT6H,endTime=2024-04-10T18:00}, Task{id=6, title='subtask#2 epic#1', description='Description 1-2-2, status=DONE, startTime=2024-04-11T12:00, duration=PT6H,endTime=2024-04-11T18:00}]",
                taskManager.getAllSubtaskFromEpic(epic1).toString(),"Некорректное получение Subtasks из Epic");
    }

    @Test
    void updateEpicStatusTest(){
        Subtask subtask = new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.IN_PROGRESS, LocalDateTime.of(2021, 4, 19, 12, 0), Duration.ofHours(6));
        taskManager.updateSubtask(subtask, 5);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(), "Неверно происходит обновление статуса Epic");
    }

    @Test
    void epicStatusNewWhenNonSubtasksTest(){
        taskManager.clearAllSubtasks();
        assertEquals(TaskStatus.NEW, epic1.getStatus(),"Некорректный вывод статуса Epic при отсутствии Subtasks");
    }

    @Test
    void epicStatusNewWhenAllSubtasksIsNewTest(){
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.NEW);
        taskManager.updateEpicStatus(epic1);
        assertEquals(TaskStatus.NEW, epic1.getStatus(), "Некорректный вывод статуса Epic при всех Subtasks со статусом New");
    }

    @Test
    void epicStatusNewWhenAllSubtasksIsDoneTest(){
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateEpicStatus(epic1);
        assertEquals(TaskStatus.DONE, epic1.getStatus(), "Некорректный вывод статуса Epic при всех Subtasks со статусом Done");
    }

    @Test
    void epicStatusNewWhenFirstSubtasksIsDoneAndSecondIsNewTest(){
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.NEW);
        taskManager.updateEpicStatus(epic1);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(), "Некорректный вывод статуса Epic при разных статусах Subtasks");
    }

    @Test
    void epicStatusNewWhenAllSubtasksIsInProgressTest(){
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpicStatus(epic1);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(), "Некорректный вывод статуса Epic при статусах Subtasks IN_PROGRESS");
    }

    @Test
    void checkTimeOfEpic(){
        Subtask subtask3 = new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2026, 4, 11, 12, 0), Duration.ofHours(6));
        taskManager.createSubtask(epic1, subtask3);
        assertEquals("Task{id=3, title='Test epic 1', description='Test Description 1, status=DONE, startTime=2024-04-10T12:00, duration=PT18H,endTime=2026-04-11T18:00}",
                taskManager.getEpicById(3).toString(), "Неверно обновляются временные параметры epic");
    }

    @Test
    void checkTaskForIntersectionWhenTasksIsCross(){
        Subtask subtask3 = new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2024, 4, 8, 12, 0), Duration.ofHours(6));
        assertTrue(taskManager.checkTaskForIntersection(subtask3), "Неверно определяется временное пересечение задач");
    }

    @Test
    void checkTaskForIntersectionWhenTasksIsNotCross(){
        Subtask subtask4 = new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2022, 4, 8, 12, 0), Duration.ofHours(6));
        assertFalse(taskManager.checkTaskForIntersection(subtask4), "Неверно определяется временное пересечение задач");
    }

    @Test
    void updateTreeSetTasksSortedByStartTime(){
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2024, 5, 9, 12, 0), Duration.ofHours(6));
        taskManager.createTask(task2);
        task = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2024, 4, 8, 12, 0), Duration.ofHours(6));
        taskManager.createTask(task);
        taskManager.updateTreeSetTasksSortedByStartTime();
        assertEquals("[Task{id=8, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}, Task{id=7, title='Task 2', description='Description 2, status=NEW, startTime=2024-05-09T12:00, duration=PT6H,endTime=2024-05-09T18:00}]",
                taskManager.getPrioritizedTasks().toString(), "Некорректная сортировка задач по времени");
    }



}
