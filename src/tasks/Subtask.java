package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, title, description, status, startTime, duration);
        this.epicId = epicId;
        super.type = TaskType.SUBTASK;

    }

    public Subtask(String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        super.type = TaskType.SUBTASK;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}