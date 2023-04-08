import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    TaskManager taskManager;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;

    @BeforeEach
    void addTasksBeforeTest(){
        taskManager = Managers.getDefaultTaskManager();
        epic1 = new Epic("Test epic 1", "Test Description 1");
        taskManager.createEpic(epic1);
        epic2 = new Epic("Test epic 2", "Test Description 1");
        taskManager.createEpic(epic2);
        subtask1 = new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        taskManager.createSubtask(epic1, subtask1);
    }

    @Test
    void getEpicId() {
        assertEquals(1, subtask1.getEpicId(), "Ошибка получения Epic по ID");
    }

    @Test
    void setEpicId() {
        subtask1.setEpicId(2);
        assertEquals(2, subtask1.getEpicId(), "Ошибка установки Id Epic");
    }


}