package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;
    HashMap<Integer, Node> linkedMap = new HashMap<>();


    @Override
    public void add(Task task) {
        if (task==null) return;

        if (linkedMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node currentNode = new Node(last, task, null);
        if (last == null) {
            first = currentNode;
        }
        else {
            last.next = currentNode;
        }
        last = currentNode;
        linkedMap.put(task.getId(), currentNode);

    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>();
        Node current = first;
        while (current != null) {
            taskList.add(current.getTask());
            current = current.next;
        }
        return taskList;
    }

    @Override
    public void remove(int id) {
        if (!isEmpty()) {
            if (linkedMap.get(id)==null) return;
            Node temp = linkedMap.get(id);
            Node previousElement = temp.prev;
            Node nextElement = temp.next;
            if (temp == first&&temp.next==null) {
                first = null;
                last = null;
            }
            else if (temp == first) {
                first=first.next;
                first.prev=null;
            }
            else if(temp==last) {
                last=last.prev;
                last.next=null;
            }
            else {
                previousElement.next=nextElement;
                nextElement.prev=previousElement;
            }
        }
        linkedMap.remove(id);
    }

    public boolean isEmpty() {
        return (first == null);
    }
}

class Node  {

    public Task task;
    public Node next;
    public Node prev;

    public Node(Node prev, Task task, Node next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    public Task getTask() {
        return task;
    }
}



