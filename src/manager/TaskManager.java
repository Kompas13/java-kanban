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
    //2.1. Получение списка всех задач Task
    public String getAllTasks() {
        String task = "";
        for (Task value : tasksById.values()) {
            task = task + "\n"+value;
        }
        return task;
    }

    //2.2. Удаление всех задач Task
    public void clearAllTasks() {
        tasksById.clear();
    }

    //2.3. Получение Task по идентификатору
    public Task getTaskById(int id) {
        return tasksById.get(id);
    }

    //2.4. Создание Task
    public void createTask(Task task) {
        task.setId(getNextId());
        tasksById.put(task.getId(), task);
    }

    //2.5. Обновление Task
    public void updateTask(Task task) {
        tasksById.put(task.getId(), task);
    }

    //2.6. Удаление Task по идентификатору
    public void deleteTaskById(int id) {
        tasksById.remove(id);
    }

    //--Epic-->
    //2.1. Получение списка всех задач Epic
    public String getAllEpic() {
        String task = "";
        for (Epic value : epicsById.values()) {
            task = task +"\n"+value;
        }
        return task;
    }

    //2.2. Удаление всех задач Epic
    public void clearAllEpics() {
        epicsById.clear();
    }

    //2.3. Получение Epic по идентификатору
    public Task getEpicById(int id) {
        return epicsById.get(id);
    }

    //2.4. Создание Epic
    public void createEpic(Epic epic) {
        epic.setId(getNextId());
        epic.setStatus(TaskStatus.NEW);
        epicsById.put(epic.getId(), epic);
    }

    //2.5. Обновление Epic
    public void updateEpic(Epic epic) {
        epicsById.put(epic.getId(), epic);
    }

    //2.6. Удаление Epic по идентификатору
    public void deleteEpicById(int id) {
        epicsById.remove(id);
    }

    //--Subtask-->
    //2.1. Получение списка всех задач Subtask
    public String getAllSubtask() {
        String task = "";
        for (Task value : subtasksById.values()) {
            task = task +"\n"+value;
        }
        return task;
    }

    //2.2. Удаление всех задач Subtask
    public void clearAllSubtasks() {
        subtasksById.clear();
    }

    //2.2. Удаление всех задач из Epic
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

    //2.3. Получение Subtask по идентификатору
    public Task getSubtaskById(int id) {
        return subtasksById.get(id);
    }

    //2.4. Создание Subtask
    public void createSubtask(Epic epic, Subtask subtask) {
        subtask.setId(getNextId());
        subtasksById.put(subtask.getId(), subtask);
        ArrayList<Integer> subtasksIdList;
        if (epic.getSubtasksIds()!=null) {
            subtasksIdList = epic.getSubtasksIds();
        } else {
            subtasksIdList = new ArrayList<>();
        }
        subtasksIdList.add(subtask.getId());//положили в ArrayList id substring
        epic.setSubtasksIds(subtasksIdList);//положили в epic обновленный массив
        subtask.setEpicId(epic.getId()); //положили в subtask id epic
        updateEpicStatus(epic); //Обновляем статус Epic при добавлении элементов Subtask
    }

    //2.5. Обновление Subtask
    public void updateSubtask(Subtask subtask) {
        tasksById.put(subtask.getId(), subtask);
        updateEpicStatus(epicsById.get(subtask.getEpicId())); //обновляем статус epic по ключу, полученному из subtask
    }

    //2.6. Удаление Subtask по идентификатору
    public void deleteSubtaskById(int id) {
        int idEpic = subtasksById.get(id).getEpicId(); //получаем id epica к которому привязан удаляемый subtask
        epicsById.get(idEpic).getSubtasksIds().remove(Integer.valueOf(id));// удаляем id subtask из ArrayList epic
        subtasksById.remove(id);
        updateEpicStatus(epicsById.get(idEpic));
    }

    //3.1. Получение списка всех подзадач определённого эпика
    public String getAllSubtaskFromEpic(Epic epic){
        String task = "";
        if (epic.getSubtasksIds()!=null) {
            for (Integer id : epic.getSubtasksIds()) {
                task = task + "\n" + subtasksById.get(id);
            }
        }
        return task;
    }

    //Обновление статуса Epic
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