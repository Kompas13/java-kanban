import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager;
    Task task;


    @BeforeEach
    void addTasksBeforeTest(){
        taskManager = Managers.getDefaultTaskManager();
        task = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        taskManager.createTask(task);
    }

    @Test
    void getIdTest() {
        assertEquals(1, task.getId());
    }

    @Test
    void getTitleTest() {
        assertEquals("Task 1", task.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("Description 1", task.getDescription());
    }

    @Test
    void getStatusTest() {
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    void setIdTest() {
        task.setId(2);
        assertEquals(2, task.getId());
    }

    @Test
    void setTitleTest() {
        task.setTitle("Task 2");
        assertEquals("Task 2", task.getTitle());
    }

    @Test
    void setDescriptionTest() {
        task.setDescription("Description 2");
        assertEquals("Description 2", task.getDescription());
    }

    @Test
    void setStatusTest() {
        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void testToString() {
        assertEquals("Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2021-04-15T12:00, duration=PT6H,endTime=2021-04-15T18:00}",
                task.toString());
    }
}