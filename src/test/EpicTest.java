import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager taskManager;
    Epic epic1;
    Subtask subtask1;

    @BeforeEach
    void addTasksBeforeTest(){
        taskManager = Managers.getDefaultTaskManager();
        epic1 = new Epic("Test epic 1", "Test Description 1");
        taskManager.createEpic(epic1);
        subtask1 = new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        taskManager.createSubtask(epic1, subtask1);
   }

    @Test
    void getSubtasksIdsTest() {
        assertEquals("[2]", epic1.getSubtasksIds().toString(), "Возврат некорректного ID");

    }

    @Test
    void setSubtasksIdsTest() {
        epic1.setSubtasksIds(new LinkedList<>());
        assertTrue(epic1.getSubtasksIds().isEmpty(), "Некорректно перезаписывается SubtasksIds");
    }
}