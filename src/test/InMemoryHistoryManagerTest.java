import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();


    @Test
    void add() {
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        task2.setId(1);
        historyManager.add(task2);
        assertEquals("[Task{id=1, title='Task 2', description='Description 2, status=NEW, startTime=2021-04-15T12:00, duration=PT6H,endTime=2021-04-15T18:00}]",
                historyManager.getHistory().toString(), "Ошибка добавления истории"
        );
    }


    @Test
    void getHistory() {
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        task2.setId(1);
        historyManager.add(task2);
        assertEquals("[Task{id=1, title='Task 2', description='Description 2, status=NEW, startTime=2021-04-15T12:00, duration=PT6H,endTime=2021-04-15T18:00}]",
                historyManager.getHistory().toString(), "Ошибка получения истории"
        );
    }

    @Test
    void remove() {
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.NEW, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        task2.setId(1);
        historyManager.add(task2);
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty(), "Ошибка удаления истории");
    }
}