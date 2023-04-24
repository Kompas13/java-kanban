package manager;

public enum Endpoint {
        TASKS("/tasks/task"),
        EPICS("/tasks/epic"),
        SUBTASKS("/tasks/subtask"),
        HISTORY("/tasks/history"),
        EPIC_SUBTASK("/tasks/subtask/epic"),
        PRIORITY("/tasks");


        private  String link;
        private Endpoint(String link) {
                this.link = link;
        }
}

