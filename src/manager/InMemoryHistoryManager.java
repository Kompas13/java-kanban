package manager;

import tasks.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private Node <Task> first;
    private Node <Task> last;
    private final List<Task> taskList = new LinkedList<>();
    HashMap<Integer, Node<Task>> linkedMap = new HashMap<>();


    @Override
    public void add(Task task) {
        Node <Task> currentNode = new Node<>(task);

        if(linkedMap.containsKey(task.getId())){
            remove(task.getId());
            linkedMap.remove(task.getId());
        }

        if (isEmpty()) {
            last.next = currentNode;
            currentNode.next = null;
            currentNode.prev = last;
        } else {
            currentNode.next = null;
            currentNode.prev = null;
            first = currentNode;
        }
        last = currentNode;

        linkedMap.put(task.getId(), currentNode);
    }

    @Override
    public List<Task> getHistory() {
        Node <Task> current = first;
        while (current != null) {
            taskList.add(current.getTask());
            current = current.next;
        }
        return taskList;
    }

    @Override
    public void remove(int id) {
        if (isEmpty()) {
            Node <Task> temp = linkedMap.get(id);
            if (temp == first&&temp.next==null)
            {
                first= null;
                last=null;
            }

            else if (temp == first)
            {
                first=first.next;
                first.prev=null;
            }
            else if(temp==last){
                last=last.prev;
                last.next=null;
            }
            else {
                temp.prev.next=temp.next;
                temp.next.prev=temp.prev;
            }
        }
    }

    public boolean isEmpty() {
        return (first != null);
    }
}

class Node <Task> {

    public Task task;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}



