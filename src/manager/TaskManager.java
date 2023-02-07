package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasksById = new HashMap<>();
    private HashMap<Integer, Epic> epicsById = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksById = new HashMap<>();

    private int nextId = 1 ;

    public int getNextId() {
        return nextId++;
    }

    //--Task-->
    //2.1. ��������� ������ ���� ����� Task
    public String getAllTasks() {
        String task = "";
        for (Task value : tasksById.values()) {
            task = task + "\n"+value;
        }
        return task;
    }

    //2.2. �������� ���� ����� Task
    public void clearAllTasks() {
        tasksById.clear();
    }

    //2.3. ��������� Task �� ��������������
    public Task getTaskById(int id) {
        return tasksById.get(id);
    }

    //2.4. �������� Task
    public void createTask(Task task) {
        task.setId(getNextId());
        tasksById.put(task.getId(), task);
    }

    //2.5. ���������� Task
    public void updateTask(Task task) {
        tasksById.put(task.getId(), task);
    }

    //2.6. �������� Task �� ��������������
    public void deleteTaskById(int id) {
        tasksById.remove(id);
    }

    //--Epic-->
    //2.1. ��������� ������ ���� ����� Epic
    public String getAllEpic() {
        String task = "";
        for (Epic value : epicsById.values()) {
            task = task +"\n"+value;
        }
        return task;
    }

    //2.2. �������� ���� ����� Epic
    public void clearAllEpics() {
        epicsById.clear();
    }

    //2.3. ��������� Epic �� ��������������
    public Task getEpicById(int id) {
        return epicsById.get(id);
    }

    //2.4. �������� Epic
    public void createEpic(Epic epic) {
        epic.setId(getNextId());
        epic.setStatus(TaskStatus.NEW);
        epicsById.put(epic.getId(), epic);
    }

    //2.5. ���������� Epic
    public void updateEpic(Epic epic) {
        epicsById.put(epic.getId(), epic);
    }

    //2.6. �������� Epic �� ��������������
    public void deleteEpicById(int id) {
        epicsById.remove(id);
    }

    //--Subtask-->
    //2.1. ��������� ������ ���� ����� Subtask
    public String getAllSubtask() {
        String task = "";
        for (Task value : subtasksById.values()) {
            task = task +"\n"+value;
        }
        return task;
    }

    //2.2. �������� ���� ����� Subtask
    public void clearAllSubtasks() {
        subtasksById.clear();
    }

    //2.2. �������� ���� ����� �� Epic
    public void clearAllSubtasksFromEpic(Epic epic) {
        ArrayList<Integer> subtasksIdList;
        if (epic.getSubtasksIds()!=null) {
            subtasksIdList = epic.getSubtasksIds();
            for (Integer id : subtasksIdList) {
                subtasksById.remove(id);
            }
        }
        epic.setStatus(TaskStatus.NEW);
    }

    //2.3. ��������� Subtask �� ��������������
    public Task getSubtaskById(int id) {
        return subtasksById.get(id);
    }

    //2.4. �������� Subtask
    public void createSubtask(Epic epic, Subtask subtask) {
        subtask.setId(getNextId());
        subtasksById.put(subtask.getId(), subtask);
        ArrayList<Integer> subtasksIdList;
        if (epic.getSubtasksIds()!=null) {
            subtasksIdList = epic.getSubtasksIds();
        } else {
            subtasksIdList = new ArrayList<>();
        }
        subtasksIdList.add(subtask.getId());//�������� � ArrayList id substring
        epic.setSubtasksIds(subtasksIdList);//�������� � epic ����������� ������
        subtask.setEpicId(epic.getId()); //�������� � subtask id epic
        updateEpicStatus(epic); //��������� ������ Epic ��� ���������� ��������� Subtask
    }

    //2.5. ���������� Subtask
    public void updateSubtask(Subtask subtask) {
        tasksById.put(subtask.getId(), subtask);
        updateEpicStatus(epicsById.get(subtask.getEpicId())); //��������� ������ epic �� �����, ����������� �� subtask
    }

    //2.6. �������� Subtask �� ��������������
    public void deleteSubtaskById(int id) {
        int idEpic = subtasksById.get(id).getEpicId(); //�������� id epica � �������� �������� ��������� subtask
        epicsById.get(idEpic).getSubtasksIds().remove(Integer.valueOf(id));// ������� id subtask �� ArrayList epic
        subtasksById.remove(id);
        updateEpicStatus(epicsById.get(idEpic));
    }

    //3.1. ��������� ������ ���� �������� ������������ �����
    public String getAllSubtaskFromEpic(Epic epic){
        String task = "";
        if (epic.getSubtasksIds()!=null) {
            for (Integer id : epic.getSubtasksIds()) {
                task = task + "\n" + subtasksById.get(id);
            }
        }
        return task;
    }

    //���������� ������� Epic
    private void updateEpicStatus(Epic epic) {
        int doneStatusCalc=0;
        int newStatusCalc=0;
        for (Integer idElement : epic.getSubtasksIds()) {
            if(subtasksById.get(idElement).getStatus()==(TaskStatus.DONE)){
                doneStatusCalc++;
            } else if(subtasksById.get(idElement).getStatus()==(TaskStatus.NEW)){
                newStatusCalc++;
            }
        }
        if (doneStatusCalc==epic.getSubtasksIds().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (newStatusCalc==epic.getSubtasksIds().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else epic.setStatus(TaskStatus.IN_PROGRESS);
        {
            updateEpic(epic);
        }
    }
}