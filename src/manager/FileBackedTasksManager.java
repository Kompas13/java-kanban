package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
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

    //���������� � ����
    public void saveAsFile() {
        String dataToString ="id,type,name,status,description,startTime,duration,epic,";

        for (Task task : getAllTasksEpicAndSubtasks()) {
            dataToString+=toString(task);
        }
        dataToString+=NEW_LINE+historyToString(historyManager);
        try (Writer fileWriter = new FileWriter(filename)) {
            fileWriter.write(dataToString);
        }
        catch (IOException e){
            throw new ManagerSaveException("������ ������ �����");
        }
    }

    //������ �����
    public String readFileContents(String filename){
        try {
            return Files.readString(Path.of(filename));
        }
        catch (IOException e) {
            throw new ManagerSaveException("���������� ��������� ����.");
        }
    }

    //������� ������������ �����
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
            if(lineData[0].matches("\\d+")&&Integer.parseInt(lineData[0])>maxId){ //����� ������������� id � �����
                maxId=Integer.parseInt(lineData[0]);
            }
            nextId=maxId+1;
        }
    }

    //�������������� ��������� ������� �� CSV.
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
                throw new ManagerSaveException("�������� ������ ������� ���������� � �����");
            }
        }
        return taskIds;
    }

    //���������� ������ � ������ String toString(Task task).
    public String toString(Task task){
        String taskToString = NEW_LINE+task.getId()+","+getTaskType(task)+","+task.getTitle()+","+task.getStatus()+","+task.getDescription()+","+task.getStartTime()+","+task.getDuration()+",";
        if (getTaskType(task)== TaskType.SUBTASK){
            Subtask subtask = (Subtask) task;
            taskToString+=subtask.getEpicId()+",";
        }
        return taskToString;
    }

    //�������� ����� �� �����
    public void taskFromString(String line){
        String[] lineData=line.split(",");
        int taskId = Integer.parseInt(lineData[0]); //id ������
        String taskType = lineData[1];
        String taskName = lineData[2];
        TaskStatus taskStatus = TaskStatus.valueOf(lineData[3]);
        String taskDescription = lineData[4];
        LocalDateTime startTime = null;
        Duration duration = null;
        if(!lineData[5].equals("null")) {
            startTime = LocalDateTime.parse(lineData[5]);
        }
        if(!lineData[6].equals("null")) {
            duration = Duration.parse(lineData[6]);
        }

        if(taskType.equals(TaskType.TASK.toString())){
            Task task = new Task(taskId, taskName, taskDescription, taskStatus, startTime, duration);
            tasksById.put(task.getId(), task);
        }
        if(taskType.equals(TaskType.EPIC.toString())){
            Epic epic=new Epic(taskId, taskName, taskDescription, taskStatus, startTime, duration);
            epicsById.put(epic.getId(), epic);
        }
        if (taskType.equals(TaskType.SUBTASK.toString())){
            int epicIdBelongSubtask = Integer.parseInt(lineData[7]);
            Subtask subtask = new Subtask(taskId, taskName, taskDescription, taskStatus, startTime, duration, epicIdBelongSubtask);
            subtasksById.put(subtask.getId(), subtask);
            LinkedList<Integer> subtasksIdList;
            Epic epic = epicsById.get(epicIdBelongSubtask);
            if (epic.getSubtasksIds()!=null) {
                subtasksIdList = epic.getSubtasksIds();
            } else {
                subtasksIdList = new LinkedList<>();
            }
            subtasksIdList.add(subtask.getId());//�������� � LinkedList id substring
            epic.setSubtasksIds(subtasksIdList);//�������� � epic ����������� ������
        }
    }

    //���������� ��������� ������� �� CSV.
    public String historyToString(HistoryManager manager){
        String historyId="\n";
        for (Task task : manager.getHistory()) {
            historyId+=task.getId()+",";
        }
        return historyId;
    }

    //������� ����� ����� � Enum
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
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
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
    public void updateEpic(Epic epic, int id) {
        super.updateEpic(epic, id);
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
    public void updateSubtask(Subtask subtask, int id) {
        super.updateSubtask(subtask, id);
        saveAsFile();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        saveAsFile();
    }

    public static void main(String[] args) {
        TaskManager fileBackedTasksManager = Managers.getFileBackedTaskManager();
        Epic epic5 = new Epic("my epic 5", "Description 1-5");
        fileBackedTasksManager.createEpic(epic5);
/*        Epic epic = new Epic("my epic 1", "Description 1-1");
        fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.createSubtask(epic, new Subtask("subtask#1 epic#1", "Description 1-1-1", TaskStatus.DONE, LocalDateTime.of(2021, 4, 8, 12, 0), Duration.ofHours(6)));
        fileBackedTasksManager.createSubtask(epic, new Subtask("subtask#2 epic#1", "Description 1-2-2", TaskStatus.DONE, LocalDateTime.of(2021, 4, 9, 12, 0), Duration.ofHours(6)));
        fileBackedTasksManager.getEpicById(1);
        Task task = new Task("my Task", "Description");
        fileBackedTasksManager.createTask(task);*/

                //������� �� �� �����:
        System.out.print("--Print all tasks--\n");
        for (Task allTasksEpicAndSubtask : fileBackedTasksManager.getAllTasksEpicAndSubtasks()) {
            System.out.println(allTasksEpicAndSubtask);
        }

        System.out.println("--History--");
        List<Task> allTasksInHistory=fileBackedTasksManager.getHistory();
        for (Task task1 : allTasksInHistory) {
            System.out.println(task1);
        }

        System.out.println("--Print sorted tasks by time--");
        for (Task prioritizedTask : fileBackedTasksManager.getPrioritizedTasks()) {
            System.out.println(prioritizedTask);
        }

    }



}


