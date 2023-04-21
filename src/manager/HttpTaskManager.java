package manager;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager{

    private KVTaskClient client;
    GsonBuilder gsonBuilder;
    Gson gson;

    public HttpTaskManager(HistoryManager historyManager) {
        super(historyManager);
        //startKvServer(); //Запуск KVServer для тестирования работы программы (предполагается,что KVServer будет запущен на отдельном устройстве)
        client = new KVTaskClient("http://localhost:"+KVServer.PORT);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.registerTypeAdapter(DurationAdapter.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter())
                .create();
        load();
    }

    @Override
    public void save() {
        String tasksMapToJson = gson.toJson(super.tasksById);
        client.put("TaskMap", tasksMapToJson);

        String epicsMapToJson = gson.toJson(super.epicsById);
        client.put("EpicsMap", epicsMapToJson);

        String subtaskMapToJson = gson.toJson(super.subtasksById);
        client.put("SubtaskMap", subtaskMapToJson);

        String historyManagerToJson = gson.toJson(super.historyManager.getHistory());
        client.put("HistoryList", historyManagerToJson);

    }

    @Override
    public void load() {
        String taskMapJson = client.load("taskMap");
        if (taskMapJson!=null&&!taskMapJson.isBlank()) {
            HashMap<Integer, Task> taskMap = new HashMap<>();
            Type MapTypeTask = new TypeToken<HashMap<Integer, Task>>() {}.getType();
            taskMap = gson.fromJson(taskMapJson, MapTypeTask);
            super.setTasksById(taskMap);
        }

        String epicsMapJson = client.load("EpicsMap");
        if (epicsMapJson!=null&&!epicsMapJson.isBlank()) {
            HashMap<Integer, Epic> epicsMap = new HashMap<>();
            Type MapTypeEpic = new TypeToken<HashMap<Integer, Epic>>() {}.getType();
            epicsMap = gson.fromJson(epicsMapJson, MapTypeEpic);
            super.setEpicsById(epicsMap);
        }

        String subtaskMapJson = client.load("SubtaskMap");
        if (subtaskMapJson!=null&&!subtaskMapJson.isBlank()){
            HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
            Type MapTypeSub = new TypeToken<HashMap<Integer, Subtask>>() {}.getType();
            subtaskMap = gson.fromJson(subtaskMapJson, MapTypeSub);
            super.setSubtasksById(subtaskMap);
        }

        String historyManagerJson = client.load("HistoryList");
        if (historyManagerJson!=null&&!historyManagerJson.isBlank()){
            Type listType = new TypeToken<List<Task>>() {}.getType();
            List<Task> historyManagerList = gson.fromJson(historyManagerJson, listType);
            for (Task task : historyManagerList) {
                super.historyManager.add(task);
            }
        }

        System.out.println("------------");
        //System.out.println(taskMapJson+"\n"+epicsMapJson+"\n"+subtaskMapJson+"\n"+historyManagerJson);
    }

    public void startKvServer(){
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
