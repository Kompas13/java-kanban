package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public Epic(String title) {
        super(title);
    }

    private ArrayList<Integer> subtasksIds;


    public ArrayList<Integer> getSubtasksIds() {
        ArrayList<Integer> subtasksIdsCopy = subtasksIds;
        return subtasksIdsCopy;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = new ArrayList<>(subtasksIds);
    }
}