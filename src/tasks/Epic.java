package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksIds;

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }
}