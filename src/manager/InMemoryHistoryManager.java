package manager;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private static final int LIMIT_TASKS = 10;
    private List<Task> historyListTasks = new LinkedList();

    @Override
    public void add(Task task) {
        if(historyListTasks.size()==LIMIT_TASKS){
            historyListTasks.remove(0);
        }
        historyListTasks.add(task);
    }

    @Override
    public void delete(Task task) {
        historyListTasks.remove(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyListTasks;
    }
}
