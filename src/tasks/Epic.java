package tasks;

import java.util.ArrayList;
import java.util.LinkedList;

public class Epic extends Task {

    private LinkedList<Integer> subtasksIds;

    public Epic(int id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
    }

    public Epic(String title, String description) {
        super(title, description);
        this.setStatus(TaskStatus.NEW);
    }

    public LinkedList<Integer> getSubtasksIds() {
        LinkedList<Integer> subtasksIdsCopy = subtasksIds;
        return subtasksIdsCopy;
    }

    public void setSubtasksIds(LinkedList<Integer> subtasksIds) {
        this.subtasksIds = new LinkedList<>(subtasksIds);
    }
}