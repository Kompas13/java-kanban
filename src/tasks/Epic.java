package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description);
        this.setStatus(TaskStatus.NEW);
    }

    public ArrayList<Integer> getSubtasksIds() {
        ArrayList<Integer> subtasksIdsCopy = subtasksIds;
        return subtasksIdsCopy;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = new ArrayList<>(subtasksIds);
    }
}