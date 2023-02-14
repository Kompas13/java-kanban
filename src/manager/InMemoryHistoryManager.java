package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task> historyListTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(historyListTasks.size()==10){
            historyListTasks.remove(0);
        }
        historyListTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyListTasks;
    }

}
