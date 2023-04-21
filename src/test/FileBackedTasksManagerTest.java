import manager.FileBackedTasksManager;
import manager.ManagerSaveException;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private FileBackedTasksManager fileBackedTaskManager;

    @Override
    FileBackedTasksManager createTaskManager() {
        fileBackedTaskManager = (FileBackedTasksManager) Managers.getFileBackedTaskManager();
        return fileBackedTaskManager;
    }

    @Test
    void saveAsFile() throws IOException {
        fileBackedTaskManager.clearAllTasks();
        fileBackedTaskManager.clearAllEpics();
        task = new Task("Task 99", "Description 1", TaskStatus.NEW, LocalDateTime.of(2021, 4, 15, 12, 0), Duration.ofHours(6));
        task.setId(1);
        fileBackedTaskManager.createTask(task);
        List<String> result = Files.readAllLines(Path.of("src/resources/saveData.CSV"));
        Assertions.assertEquals(
                "[id,type,name,status,description,startTime,duration,epic, 7,TASK,Task 99,NEW,Description 1,2021-04-15T12:00,PT6H]",
                result.toString(), "Некорректное сохранение файла"
        );
    }

    @Test
    void loadFile() {
        String fileName = "src/resources/saveData.CSV";
        fileBackedTaskManager.clearAllTasks();
        fileBackedTaskManager.clearAllEpics();
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write("id,type,name,status,description,epic\n" +
                    "1,TASK,Task 1,NEW,Description 1,2021-04-15T12:00,PT6H,, ");
        }
        catch (IOException e){
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        fileBackedTaskManager.load();
        assertEquals("Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2021-04-15T12:00, duration=PT6H,endTime=2021-04-15T18:00}",
                fileBackedTaskManager.getTaskById(1).toString(), "Ошибка чтения файла"
        );
    }

    @Test
    void reloadHistory() {
        fileBackedTaskManager.reloadHistory("1,3");
        assertEquals("[Task{id=1, title='Task 1', description='Description 1, status=NEW, startTime=2024-04-08T12:00, duration=PT6H,endTime=2024-04-08T18:00}, Task{id=3, title='Test epic 1', description='Test Description 1, status=DONE, startTime=2024-04-10T12:00, duration=PT12H,endTime=2024-04-11T18:00}]",
                fileBackedTaskManager.getHistory().toString(), "Ошибка перезаписи истории"
        );
    }

    @Test
    void historyFromString() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);
        String value = "1,2,3";
        List<Integer> historyListByInteger = fileBackedTaskManager.historyFromString(value);
        assertEquals(integerList, historyListByInteger, "Неверно возвращается история из String");
    }

    @Test
    void testToString() {
        assertEquals("\n3,EPIC,Test epic 1,DONE,Test Description 1,2024-04-10T12:00,PT12H", fileBackedTaskManager.toString(epic1), "Некорректное преобразование задачи в строку");
    }

    @Test
    void taskFromString() {
        fileBackedTaskManager.taskFromString(
                "2,TASK,Task 2,NEW,Description 2,2021-04-09T12:00,PT6H,");
        assertNotNull(fileBackedTaskManager.getTaskById(2), "Некорректное преобразование задачи в строку");
    }

    @Test
    void fileBackedTaskManagerIsEmpty(){
        fileBackedTaskManager.clearAllTasks();
        fileBackedTaskManager.clearAllEpics();
        fileBackedTaskManager.load();
        assertEquals("[]", fileBackedTaskManager.getAllTasksEpicAndSubtasks().toString(),"Файл должен быть пустым");
    }

    @Test
    void removeTasksFromBeginnerMiddleEndFileBackedTaskManagerIsEmpty(){
        fileBackedTaskManager.deleteTaskById(1);
        fileBackedTaskManager.deleteEpicById(4);
        fileBackedTaskManager.deleteSubtaskById(6);
        fileBackedTaskManager.load();
        assertEquals("[Task{id=2, title='Task 2', description='Description 2, status=NEW, startTime=2024-05-09T12:00, duration=PT6H,endTime=2024-05-09T18:00}, Task{id=3, title='Test epic 1', description='Test Description 1, status=DONE, startTime=2024-04-10T12:00, duration=PT6H,endTime=2024-04-10T18:00}, Task{id=5, title='subtask#1 epic#1', description='Description 1-1-1, status=DONE, startTime=2024-04-10T12:00, duration=PT6H,endTime=2024-04-10T18:00}]",
                fileBackedTaskManager.getAllTasksEpicAndSubtasks().toString(), "Неверно удаляются задачи из файла");
    }
}