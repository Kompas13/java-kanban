package manager;



import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTasksManager(getDefaultHistory());
    }

    public static HttpTaskManager getHttpTaskManager(){
        return new HttpTaskManager(getDefaultHistory());
    }

}
