package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasksById = new HashMap<>();
    private final HashMap<Integer, Epic> epicsById = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksById = new HashMap<>();
    private final HistoryManager historyManager;

    private int nextId = 1 ;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public int getNextId() {
        return nextId++;
    }

//Вывод всех задач (Tasks и Epics со своими Subtasks)
    @Override
    public List<Task> getAllTasksEpicAndSubtasks(){
        List<Task> tasks = new ArrayList<>(tasksById.values());
        for (Epic valueEpic : epicsById.values()) {
            tasks.add(valueEpic);
            if (valueEpic.getSubtasksIds()!=null) {
                for (Integer subtasksId : valueEpic.getSubtasksIds()) {
                    tasks.add(subtasksById.get(subtasksId));
                }
            }
        }
        return tasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //--Task-->
    //2.1. Получение списка всех задач Task (такое требование ТЗ:).
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksById.values());
    }

    //2.2. Удаление всех задач Task
    @Override
    public void clearAllTasks() {
        for (Integer id : tasksById.keySet()) {
            historyManager.remove(id);
        }
        tasksById.clear();
    }

    //2.3. Получение Task по идентификатору
    @Override
    public Task getTaskById(int id) {
        if(tasksById.get(id)!=null) {
            historyManager.add(tasksById.get(id));
        }
        return tasksById.get(id);
    }

    //2.4. Создание Task
    @Override
    public void createTask(Task task) {
        task.setId(getNextId());
        tasksById.put(task.getId(), task);
    }

    //2.5. Обновление Task
    @Override
    public void updateTask(Task task) {
        tasksById.put(task.getId(), task);
    }

    //2.6. Удаление Task по идентификатору
    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        tasksById.remove(id);
    }

    //--Epic-->
    //2.1. Получение списка всех задач Epic
    @Override
    public List<Task> getAllEpic() {
        return new ArrayList<>(epicsById.values());
    }

    //2.2. Удаление всех задач Epic
    @Override
    public void clearAllEpics() {
        for (Integer id : epicsById.keySet()) {
            historyManager.remove(id);
        }
        epicsById.clear();
        subtasksById.clear();

    }

    //2.3. Получение Epic по идентификатору
    @Override
    public Task getEpicById(int id) {
        if(epicsById.get(id)!=null) {
            historyManager.add(epicsById.get(id));
        }
        return epicsById.get(id);
    }

    //2.4. Создание Epic
    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNextId());
        epicsById.put(epic.getId(), epic);
    }

    //2.5. Обновление Epic
    @Override
    public void updateEpic(Epic epic) {
        epicsById.put(epic.getId(), epic);
    }

    //2.6. Удаление Epic по идентификатору
    @Override
    public void deleteEpicById(int id) {
        clearAllSubtasksFromEpic(epicsById.get(id));
        historyManager.remove(id);

        epicsById.remove(id);
    }

    //--Subtask-->
    //2.1. Получение списка всех задач Subtask
    @Override
    public List<Task> getAllSubtask() {
        return new ArrayList<>(subtasksById.values());
    }

    //2.2. Удаление всех задач Subtask
    @Override
    public void clearAllSubtasks() {
        for (Integer id : subtasksById.keySet()) {
            historyManager.remove(id);
        }
        subtasksById.clear();
        for (Epic value : epicsById.values()) {
            value.setStatus(TaskStatus.NEW);//меняем статусы epic
            value.setSubtasksIds(null); //затираем все ArrayList в epic
        }
    }

    //2.2. Удаление всех задач из Epic
    @Override
    public void clearAllSubtasksFromEpic(Epic epic) {
        ArrayList<Integer> subtasksIdList;
        if (epic.getSubtasksIds()!=null) {
            subtasksIdList = epic.getSubtasksIds();
            for (Integer id : subtasksIdList) {
                historyManager.remove(id);
                subtasksById.remove(id);
            }
        }
        epic.setStatus(TaskStatus.NEW);
    }

    //2.3. Получение Subtask по идентификатору
    @Override
    public Task getSubtaskById(int id) {
        if(subtasksById.get(id)!=null){
            historyManager.add(subtasksById.get(id));
        }
        return subtasksById.get(id);
    }

    //2.4. Создание Subtask
    @Override
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
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasksById.put(subtask.getId(), subtask);
        updateEpicStatus(epicsById.get(subtask.getEpicId())); //обновляем статус epic по ключу, полученному из subtask
    }

    //2.6. Удаление Subtask по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        int idEpic = subtasksById.get(id).getEpicId(); //получаем id epic к которому привязан удаляемый subtask
        epicsById.get(idEpic).getSubtasksIds().remove(Integer.valueOf(id));// удаляем id subtask из ArrayList epic
        historyManager.remove(id);
        subtasksById.remove(id);
        updateEpicStatus(epicsById.get(idEpic));
    }

    //3.1. Получение списка всех подзадач определённого эпика
    @Override
    public List<Task> getAllSubtaskFromEpic(Epic epic){
        List<Task> tasks = new ArrayList<>();
        if (epic.getSubtasksIds()!=null) {
            for (Integer id : epic.getSubtasksIds()) {
                tasks.add(subtasksById.get(id));
            }
        }
        return tasks;
    }

    //Обновление статуса Epic
    @Override
    public void updateEpicStatus(Epic epic) {
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
        } else if (newStatusCalc != epic.getSubtasksIds().size()) { epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }
}