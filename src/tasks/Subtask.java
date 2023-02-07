package tasks;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, TaskStatus status) {
        super(title, status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}