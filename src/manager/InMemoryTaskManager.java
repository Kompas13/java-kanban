package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public HashMap<Integer, Task> tasksById;
    public HashMap<Integer, Epic> epicsById;
    public HashMap<Integer, Subtask> subtasksById;
    public TreeSet<Task> tasksSortedByStartTime;
    public HistoryManager historyManager;

    public int nextId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasksById = new HashMap<>();
        epicsById = new HashMap<>();
        subtasksById = new HashMap<>();
        tasksSortedByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Task::getId));
        this.historyManager = historyManager;

    }

    protected void setTasksById(HashMap<Integer, Task> tasksById) {
        this.tasksById = tasksById;
    }

    protected void setEpicsById(HashMap<Integer, Epic> epicsById) {
        this.epicsById = epicsById;
    }

    protected void setSubtasksById(HashMap<Integer, Subtask> subtasksById) {
        this.subtasksById = subtasksById;
    }

    protected void setTasksSortedByStartTime(TreeSet<Task> tasksSortedByStartTime) {
        this.tasksSortedByStartTime = tasksSortedByStartTime;
    }

    protected int getNextId() {
        return nextId++;
    }

    //Вывод всех задач (Tasks и Epics со своими Subtasks)
    @Override
    public List<Task> getAllTasksEpicAndSubtasks() {
        List<Task> tasks = new ArrayList<>(tasksById.values());
        for (Epic valueEpic : epicsById.values()) {
            tasks.add(valueEpic);
            if (valueEpic.getSubtasksIds() != null) {
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
        updateTreeSetTasksSortedByStartTime();//здесь удаляются только задачи Tasks (Epic и Subtask остаются)
    }

    //2.3. Получение Task по идентификатору
    @Override
    public Task getTaskById(int id) {
        if (tasksById.containsKey(id)) {
            historyManager.add(tasksById.get(id));
            return tasksById.get(id);
        } throw new ManagerSaveException("Неправильно введен  Task ID");
    }

    //2.4. Создание Task
    @Override
    public void createTask(Task task) {
        if (checkTaskForIntersection(task)) {
            throw new ManagerSaveException("Проверьте, чтобы задачи не пересекались по времени");
        }
            task.setId(getNextId());
            tasksById.put(task.getId(), task);
            tasksSortedByStartTime.add(task);
    }

    //2.5. Обновление Task
    @Override
    public void updateTask(Task task, int id) {
        if (!tasksById.containsKey(id)){
            throw new ManagerSaveException("Неправильно введен  Task ID");
        }
        if (checkTaskForIntersection(task)) {
            throw new ManagerSaveException("Проверьте, чтобы задачи не пересекались по времени");
        }
        task.setId(id);
        tasksById.put(id, task);
        updateTreeSetTasksSortedByStartTime();
    }

    //2.6. Удаление Task по идентификатору
    @Override
    public void deleteTaskById(int id) {
        if (!tasksById.containsKey(id)) {
            throw new ManagerSaveException("Неправильно введен  Task ID");
        }
        historyManager.remove(id);
        tasksById.remove(id);
        updateTreeSetTasksSortedByStartTime();
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
        updateTreeSetTasksSortedByStartTime();
    }

    //2.3. Получение Epic по идентификатору
    @Override
    public Epic getEpicById(int id) {
        if (epicsById.containsKey(id)) {
            historyManager.add(epicsById.get(id));
            return epicsById.get(id);
        } else throw new ManagerSaveException("Неправильно введен Epic ID");
    }

    //2.4. Создание Epic
    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNextId());
        epicsById.put(epic.getId(), epic);
        tasksSortedByStartTime.add(epic);
    }

    //2.5. Обновление Epic
    @Override
    public void updateEpic(Epic epic, int id) {
        LinkedList<Integer> subtasksIdsCopy = epicsById.get(id).getSubtasksIds();
        epic.setId(id);
        if (subtasksIdsCopy!=null){
            epic.setSubtasksIds(subtasksIdsCopy);
        }
        epicsById.put(id, epic);
        checkTimeOfEpic(epic);
        updateTreeSetTasksSortedByStartTime();
    }

    //2.6. Удаление Epic по идентификатору
    @Override
    public void deleteEpicById(int id) {
        if (!epicsById.containsKey(id)) {
            throw new ManagerSaveException("Неправильно введен Epic ID");
        }
        clearAllSubtasksFromEpic(epicsById.get(id));
        historyManager.remove(id);
        epicsById.remove(id);
        updateTreeSetTasksSortedByStartTime();
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
        for (Epic value : epicsById.values()) {
            value.setStatus(TaskStatus.NEW);//меняем статусы epic
            value.setSubtasksIds(new LinkedList<>()); //затираем все ArrayList в epic
            checkTimeOfEpic(value);
        }
        subtasksById.clear();
        updateTreeSetTasksSortedByStartTime();

    }

    //2.2. Удаление всех задач из Epic
    @Override
    public void clearAllSubtasksFromEpic(Epic epic) {
        LinkedList<Integer> subtasksIdList;
        if (epic.getSubtasksIds() != null) {
            subtasksIdList = epic.getSubtasksIds();
            for (Integer id : subtasksIdList) {
                historyManager.remove(id);
                subtasksById.remove(id);
            }
            subtasksIdList.clear();
            epic.setSubtasksIds(subtasksIdList);
            epic.setStatus(TaskStatus.NEW);
            checkTimeOfEpic(epic);
            updateTreeSetTasksSortedByStartTime();
        }
    }

    //2.3. Получение Subtask по идентификатору
    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasksById.containsKey(id)) {
            historyManager.add(subtasksById.get(id));
            return subtasksById.get(id);
        } else throw new ManagerSaveException("Неправильно введен Subtask ID");
    }

    //2.4. Создание Subtask
    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        if (!checkTaskForIntersection(subtask)) {
            subtask.setId(getNextId());
            subtasksById.put(subtask.getId(), subtask);
            LinkedList<Integer> subtasksIdList;
            if (epic.getSubtasksIds() != null) {
                subtasksIdList = epic.getSubtasksIds();
            } else {
                subtasksIdList = new LinkedList<>();
            }
            subtasksIdList.add(subtask.getId());//положили в ArrayList id substring
            epic.setSubtasksIds(subtasksIdList);//положили в epic обновленный массив
            subtask.setEpicId(epic.getId()); //положили в subtask id epic
            updateEpicStatus(epic); //Обновляем статус Epic при добавлении элементов Subtask
            checkTimeOfEpic(epic); //обновляем временные параметры Epic
            updateTreeSetTasksSortedByStartTime();
        }
        else throw new ManagerSaveException("Проверьте, чтобы задачи не пересекались по времени");
    }

    //2.5. Обновление Subtask
    @Override
    public void updateSubtask(Subtask subtask, int id) {
        int epicId = subtasksById.get(id).getEpicId();
        if (!subtasksById.containsKey(id)) {
            throw new ManagerSaveException("Неправильно введен Subtask ID");
        }
        if (checkTaskForIntersection(subtask)) {
            throw new ManagerSaveException("Проверьте, чтобы задачи не пересекались по времени");
        }
        subtask.setId(id);
        subtasksById.put(id, subtask);
        subtask.setEpicId(epicId);
        updateEpicStatus(epicsById.get(subtask.getEpicId())); //обновляем статус epic по ключу, полученному из subtask
        checkTimeOfEpic(epicsById.get(subtask.getEpicId()));
        updateTreeSetTasksSortedByStartTime();
    }

    //2.6. Удаление Subtask по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        if (!subtasksById.containsKey(id)) {
            throw new ManagerSaveException("Неправильно введен Subtask ID");
        }
        int idEpic = subtasksById.get(id).getEpicId(); //получаем id epic к которому привязан удаляемый subtask
        LinkedList<Integer> newSubtaskId;
        newSubtaskId = epicsById.get(idEpic).getSubtasksIds();// удаляем id subtask из ArrayList epic
        newSubtaskId.remove((Integer)id);
        epicsById.get(idEpic).setSubtasksIds(newSubtaskId);
        subtasksById.remove(id);
        historyManager.remove(id);
        updateEpicStatus(epicsById.get(idEpic));
        checkTimeOfEpic(epicsById.get(idEpic));
        updateTreeSetTasksSortedByStartTime();
    }

    //3.1. Получение списка всех подзадач определённого эпика
    @Override
    public List<Task> getAllSubtaskFromEpic(Epic epic) {
        List<Task> tasks = new ArrayList<>();
        if (epic.getSubtasksIds() != null) {
            for (Integer id : epic.getSubtasksIds()) {
                tasks.add(subtasksById.get(id));
            }
        }
        return tasks;
    }

    //Обновление статуса Epic
    @Override
    public void updateEpicStatus(Epic epic) {
        int doneStatusCalc = 0;
        int newStatusCalc = 0;
        for (Integer idElement : epic.getSubtasksIds()) {
            if (subtasksById.get(idElement).getStatus() == (TaskStatus.DONE)) {
                doneStatusCalc++;
            } else if (subtasksById.get(idElement).getStatus() == (TaskStatus.NEW)) {
                newStatusCalc++;
            }
        }
        if (doneStatusCalc == epic.getSubtasksIds().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (newStatusCalc != epic.getSubtasksIds().size()) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
        updateTreeSetTasksSortedByStartTime(); //метод обновления коллекции в которой хранятся отсортированные по времени задачи
    }

    //Установка временных переменных Epic
    @Override
    public void checkTimeOfEpic(Epic epic) {
        if (epic.getSubtasksIds()==null||epic.getSubtasksIds().isEmpty()){
            return;
        }
        TreeSet<Subtask> subtasksSortedByStartTime = new TreeSet<>(Comparator.comparing(Subtask::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Subtask::getId));
        TreeSet<Subtask> subtasksSortedByEndTime = new TreeSet<>(Comparator.comparing(Subtask::getEndTime, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(Subtask::getId).reversed());
        subtasksSortedByStartTime.addAll(subtasksById.values());
        subtasksSortedByEndTime.addAll(subtasksById.values());
        LocalDateTime epicStartTime = subtasksSortedByStartTime.stream().findFirst().get().getStartTime();
        LocalDateTime epicEndTime = subtasksSortedByEndTime.stream().findFirst().get().getEndTime();
        Duration duration = null;
        for (Subtask subtask : subtasksSortedByEndTime) {
            if (duration==null){
                duration = subtask.getDuration();
            }
            else {
                duration = duration.plus(subtask.getDuration());
            }
        }
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(duration);
    }

    //Метод обновления коллекции в которой хранятся отсортированные по времени задачи
    @Override
    public void updateTreeSetTasksSortedByStartTime() {
        tasksSortedByStartTime.clear();
        tasksSortedByStartTime.addAll(tasksById.values());
        tasksSortedByStartTime.addAll(epicsById.values());
        tasksSortedByStartTime.addAll(subtasksById.values());
    }

    //Метод возвращающий отсортированные задачи
    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        updateTreeSetTasksSortedByStartTime();
        return new ArrayList<>(tasksSortedByStartTime);
    }

    //Проверка задач на пересечение по времени
    @Override
    public boolean checkTaskForIntersection(Task task1) {
        if(task1.getStartTime()==null){
            return false;
        }
        else return isTimeSuperImposedOnOther(task1.getStartTime(), task1.getEndTime());
    }

    public boolean isTimeSuperImposedOnOther(LocalDateTime startTime, LocalDateTime endTime){
        boolean checkFlag = false;
        for (Task task2 : tasksSortedByStartTime) {
            if (task2.getStartTime()==null){
                continue;
            }
            if ((startTime.isBefore(task2.getStartTime())&&endTime.isBefore(task2.getStartTime()))||
                    (task2.getStartTime().isBefore(startTime)&&task2.getEndTime().isBefore(startTime))) {
                continue;
            }
            else {
                checkFlag = true;
                break;
            }
        }
        return checkFlag;
    }
}



