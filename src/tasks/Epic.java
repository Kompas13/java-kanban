package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Epic extends Task {

    private LinkedList<Integer> subtasksIds;

    public Epic(int id, String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, title, description, status, startTime, duration);
    }

    public Epic(String title, String description) {
        super(title, description);
        this.setStatus(TaskStatus.NEW);
    }


    public LinkedList<Integer> getSubtasksIds() {
        LinkedList<Integer> subtasksIdsCopy = null;
        if(subtasksIds!=null) {
            subtasksIdsCopy = new LinkedList<>(subtasksIds);
        }
        return subtasksIdsCopy;
    }

    public void setSubtasksIds(LinkedList<Integer> subtasksIds) {
        this.subtasksIds = new LinkedList<>(subtasksIds);
    }

    @Override
    public void setEndTime(LocalDateTime endTime) { //Время завершения Epic
        // вычисляется в InMemoryTaskManager и поменяв здесь модификатор доступа на другой я не смогу его оттуда устанавливать.
        super.setEndTime(endTime);
    }
}