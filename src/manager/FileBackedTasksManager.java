package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String filename;
    private final String NEW_LINE = "\n";

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
        filename = "src/resources/saveData.CSV";
        loadFile(filename);
    }

    //Сохранение в файл
    public void saveAsFile() {
        String dataToString ="id,type,name,status,description,epic";

        for (Task task : getAllTasksEpicAndSubtasks()) {
            dataToString+=toString(task);
        }
        dataToString+=NEW_LINE+historyToString(historyManager); // перенос на новую строку был в методе toString, убрал везде StringBuilder, теперь перенос на 104 строке.
        try (Writer fileWriter = new FileWriter(filename)) {
            fileWriter.write(dataToString);
        }
        catch (IOException e){
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }

    //Чтение файла
    private String readFileContents(String filename){
        try {
            return Files.readString(Path.of(filename));
        }
        catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл.", e);
        }
    }

    //Парсинг считываемого файла
    public void loadFile(String fileName){
        int maxId=1;
        String data=readFileContents(fileName);
        if (data.isEmpty()){
            return;
        }
        String[] lines = data.split("\r?\n");
        for (int i=1; i< lines.length; i++) {
            if (lines[i].isEmpty()){
                i++;
                reloadHistory(lines[i]);
                continue;
            }
            String[] lineData=lines[i].split(",");
            taskFromString(lines[i]);
            if(lineData[0].matches("\\d+")&&Integer.parseInt(lineData[0])>maxId){ //поиск максимального id в файле
                maxId=Integer.parseInt(lineData[0]);
            }
            nextId=maxId+1;
        }
    }

    //Восстановление менеджера истории из CSV.
    public void reloadHistory(String idLine){
        for (Integer historyId : historyFromString(idLine)) {
            if (tasksById.containsKey(historyId)){
                super.getTaskById(historyId);
            }
            else if (epicsById.containsKey(historyId)){
                super.getEpicById(historyId);
            }
            else if (subtasksById.containsKey(historyId)){
                super.getSubtaskById(historyId);
            }
        }
    }

    public static List<Integer> historyFromString(String value){
        String[] lineData=value.split(",");
        List<Integer> taskIds = new ArrayList<>();
        for (String idFromHistory : lineData) {
            try {
                taskIds.add(Integer.parseInt(idFromHistory));
            } catch (NumberFormatException e) {
                throw new ManagerSaveException("Неверный формат истории просмотров в файле", e);
            }
        }
        return taskIds;
    }

    //Сохранения задачи в строку String toString(Task task).
    public String toString(Task task){
        String taskToString = NEW_LINE+task.getId()+","+getTaskType(task)+","+task.getTitle()+","+task.getStatus()+","+task.getDescription()+",";
        if (getTaskType(task)== TaskType.SUBTASK){
            Subtask subtask = (Subtask) task;
            taskToString+=subtask.getEpicId()+",";
        }
        return taskToString;
    }

    //Создание задач из файла
    public void taskFromString(String line){
        String[] lineData=line.split(",");
        int taskId = Integer.parseInt(lineData[0]); //id задачи
        String taskType = lineData[1];
        String taskName = lineData[2];
        TaskStatus taskStatus = TaskStatus.valueOf(lineData[3]);
        String taskDescription = lineData[4];

        if(taskType.equals(TaskType.TASK.toString())){
            Task task = new Task(taskId, taskName, taskDescription, taskStatus);
            tasksById.put(task.getId(), task);
        }
        if(taskType.equals(TaskType.EPIC.toString())){
            Epic epic=new Epic(taskId, taskName, taskDescription, taskStatus);
            epicsById.put(epic.getId(), epic);
        }
        if (taskType.equals(TaskType.SUBTASK.toString())){
            int epicIdBelongSubtask = Integer.parseInt(lineData[5]);
            Subtask subtask = new Subtask(taskId, taskName, taskDescription, taskStatus, epicIdBelongSubtask);
            subtasksById.put(subtask.getId(), subtask);
            LinkedList<Integer> subtasksIdList;
            Epic epic = epicsById.get(epicIdBelongSubtask);
            if (epic.getSubtasksIds()!=null) {
                subtasksIdList = epic.getSubtasksIds();
            } else {
                subtasksIdList = new LinkedList<>();
            }
            subtasksIdList.add(subtask.getId());//положили в LinkedList id substring
            epic.setSubtasksIds(subtasksIdList);//положили в epic обновленный массив
        }
    }

    //Сохранения менеджера истории из CSV.
    public static String historyToString(HistoryManager manager){
        String historyId="\n";
        for (Task task : manager.getHistory()) {
            historyId+=task.getId()+",";
        }
        return historyId;
    }

    //Перевод типов задач в Enum
    public Enum getTaskType (Task task){
        if (String.valueOf(task.getClass()).equals("class tasks.Task")){
            return TaskType.TASK;
        }
        else if (String.valueOf(task.getClass()).equals("class tasks.Epic")){
            return TaskType.EPIC;
        }
        else {
            return TaskType.SUBTASK;
        }
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        saveAsFile();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasksById.get(id));
        saveAsFile();
        return tasksById.get(id);
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        saveAsFile();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        saveAsFile();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        saveAsFile();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        saveAsFile();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epicsById.get(id));
        saveAsFile();
        return epicsById.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        saveAsFile();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        saveAsFile();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        saveAsFile();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        saveAsFile();
    }

    @Override
    public void clearAllSubtasksFromEpic(Epic epic) {
        super.clearAllSubtasksFromEpic(epic);
        saveAsFile();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasksById.get(id));
        saveAsFile();
        return subtasksById.get(id);
    }

    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        super.createSubtask(epic, subtask);
        saveAsFile();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        saveAsFile();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        saveAsFile();
    }

    public static void main(String[] args) { //в ТЗ "проверьте работу сохранения и восстановления менеджера из файла
        //Для этого создайте метод static void main(String[] args) в классе FileBackedTasksManager"
        FileBackedTasksManager fileBackedTasksManager= new FileBackedTasksManager(Managers.getDefaultHistory());
        Epic epic5 = new Epic("my epic 5", "Description 1-5");
        fileBackedTasksManager.createEpic(epic5);
        //fileBackedTasksManager.deleteEpicById(3);
        FileBackedTasksManager fileBackedTasksManager2 = new FileBackedTasksManager(Managers.getDefaultHistory());

        //Выводим всё на экран:
        System.out.print("--Print all tasks--\n");
        for (Task allTasksEpicAndSubtask : fileBackedTasksManager2.getAllTasksEpicAndSubtasks()) {
            System.out.println(allTasksEpicAndSubtask);
        }

        System.out.println("--History--");
        List<Task> allTasksInHistory=fileBackedTasksManager2.getHistory();
        for (Task task1 : allTasksInHistory) {
            System.out.println(task1);
        }
    }


}


