package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
        loadFile();
    }

    //Сохранение в файл
    public void saveAsFile() {

        StringBuilder dataToStringBuilder = new StringBuilder("id,type,name,status,description,epic");
        for (Task allTasksEpicAndSubtask : getAllTasksEpicAndSubtasks()) {
            if(allTasksEpicAndSubtask!=null){
                dataToStringBuilder.append(toString(allTasksEpicAndSubtask));
            }
        }
        String dataToString = dataToStringBuilder.toString();
        dataToString+="\n"+historyToString(historyManager);
        try (Writer fileWriter = new FileWriter("src/resources/saveData.CSV"))
        {
            fileWriter.write(dataToString);
        }
        catch (IOException e){
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }

    //Чтение файла
    private String readFileContents(){
        try {
            return Files.readString(Path.of("src/resources/saveData.CSV"));
        }
        catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл.", e);
        }
    }

    //Парсинг считываемого файла
    public void loadFile(){
        int maxId=1;
        String data=readFileContents();
        if (data.isEmpty()){
            return;
        }
        String[] lines = data.split("\r?\n");
        for (int i=0; i< lines.length; i++) {
            if (lines[i].isEmpty()){
                reloadHistory(lines[i + 1]);
                return;
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
        List<Integer> list = new ArrayList<>();
        for (String idFromHistory : lineData) {
            list.add(Integer.parseInt(idFromHistory));
        }
        return list;
    }

    //Сохранения задачи в строку String toString(Task task).
    public String toString(Task task){
        String taskToString = "\n"+task.getId()+","+getTaskType(task)+","+task.getTitle()+","+task.getStatus()+","+task.getDescription()+",";
        if (getTaskType(task)==TaskType.SUBTASK){
            Subtask subtask = (Subtask) task;
            taskToString+=subtask.getEpicId()+",";
        }
        return taskToString;
    }

    //Создание задач из файла
    public void taskFromString(String line){
        String[] lineData=line.split(",");
        if(lineData[1].equals(TaskType.TASK.toString())){
            Task task = new Task(lineData[2], lineData[4], TaskStatus.valueOf(lineData[3]));
            task.setId(Integer.parseInt(lineData[0]));
            tasksById.put(task.getId(), task);
            task.setStatus(TaskStatus.valueOf(lineData[3]));
        }
        if(lineData[1].equals(TaskType.EPIC.toString())){
            Epic epic=new Epic(lineData[2], lineData[4]);
            epic.setId(Integer.parseInt(lineData[0]));
            epic.setStatus(TaskStatus.valueOf(lineData[3]));
            epicsById.put(epic.getId(), epic);
        }
        if (lineData[1].equals(TaskType.SUBTASK.toString())){
            Subtask subtask = new Subtask(lineData[2], lineData[4], TaskStatus.valueOf(lineData[3]));
            subtask.setId(Integer.parseInt(lineData[0]));
            subtask.setStatus(TaskStatus.valueOf(lineData[3]));
            subtasksById.put(subtask.getId(), subtask);
            ArrayList<Integer> subtasksIdList;
            Epic epic = epicsById.get(Integer.parseInt(lineData[5]));
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
    }

    //Сохранения менеджера истории из CSV.
    public static String historyToString(HistoryManager manager){
        StringBuilder historyId= new StringBuilder("\n");
        for (Task task : manager.getHistory()) {
            historyId.append(task.getId()).append(",");
        }
        return historyId.toString();
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

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager= new FileBackedTasksManager(Managers.getDefaultHistory());
        Epic epic5 = new Epic("my epic 5", "Description 1-5");
        fileBackedTasksManager.createEpic(epic5);
        //fileBackedTasksManager.deleteEpicById(4);
        //fileBackedTasksManager.deleteSubtaskById(5);
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


