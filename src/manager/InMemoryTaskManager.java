package manager;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import status.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    private final HistoryManager historyManager;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public InMemoryTaskManager(HistoryManager historyManager){
        this.historyManager=historyManager;
    }

    @Override
    public Task createTask(Task task) {
        int taskId = generateId();
        task.setTaskId(taskId);
        tasks.put(task.getTaskId(), task);

        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> arrayTasks = new ArrayList<>();

        for (Task task : tasks.values()) {
            arrayTasks.add(task);
        }

        return arrayTasks;
    }

    @Override
    public Task getTask(int idTask) {
        historyManager.addTaskToHistory(tasks.get(idTask));
        return tasks.get(idTask);
    }

    @Override
    public Task updateTask(Task task) {
        tasks.put(task.getTaskId(), task);

        return task;
    }

    @Override
    public void deleteTask(int idTask) {
        historyManager.remove(idTask);
        tasks.remove(idTask);

    }

    @Override
    public void deleteAllTasks() {
        for(int idTask:tasks.keySet()) {
            historyManager.remove(idTask);
        }
        tasks.clear();
    }

    @Override
    public Epic createEpic(Epic epic) {
        int taskId = generateId();
        epic.setTaskId(taskId);
        epics.put(epic.getTaskId(), epic);

        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> arrayEpics = new ArrayList<>();

        for (Epic epic : epics.values()) {
            arrayEpics.add(epic);
        }

        return arrayEpics;
    }

    @Override
    public Epic getEpic(int idEpic) {
        historyManager.addTaskToHistory(epics.get(idEpic));
        return epics.get(idEpic);
    }

    @Override
    public Epic updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);

        return epic;
    }

    @Override
    public void deleteEpic(int idEpic) {
        Epic foundEpic = epics.get(idEpic);
        ArrayList<Integer> idSubTasksDeleted = foundEpic.getIdSubTasks();

        for (int idSubTask : idSubTasksDeleted) {
            historyManager.remove(idSubTask);
            subTasks.remove(idSubTask);
        }
        historyManager.remove(idEpic);
        epics.remove(idEpic);
    }

    @Override
    public void deleteAllEpics() {
        for(int idTask:epics.keySet()) {
            historyManager.remove(idTask);
        }
        for(int idTask:subTasks.keySet()) {
            historyManager.remove(idTask);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        int taskId = generateId();
        subTask.setTaskId(taskId);
        subTasks.put(subTask.getTaskId(), subTask);
        int idEpic = subTask.getIdEpic();

        if (epics.keySet().contains(idEpic)) {
            Epic foundEpic = epics.get(idEpic);
            foundEpic.getIdSubTasks().add(subTask.getTaskId());
            ArrayList<Integer> idSubTaskOfEpic = epics.get(subTask.getIdEpic()).getIdSubTasks();
            Status statusEpic = updateStatusEpic(idSubTaskOfEpic);
            foundEpic.setStatus(statusEpic);
        }

        return subTask;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> arraySubTasks = new ArrayList<>();

        for (SubTask subTask : subTasks.values()) {
            arraySubTasks.add(subTask);
        }

        return arraySubTasks;
    }

    @Override
    public SubTask getSubTask(int idSubTask) {
        historyManager.addTaskToHistory(subTasks.get(idSubTask));
        return subTasks.get(idSubTask);
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskId(), subTask);
        Integer idEpic = subTask.getIdEpic();

        if (epics.keySet().contains(idEpic)) {
            Epic foundEpic = epics.get(idEpic);
            ArrayList<Integer> idSubTaskOfEpic = foundEpic.getIdSubTasks();
            Status statusEpic = updateStatusEpic(idSubTaskOfEpic);
            foundEpic.setStatus(statusEpic);
        }

        return subTask;
    }

    @Override
    public void deleteSubTask(int idSubTask) {
        SubTask foundSubTask = subTasks.get(idSubTask);
        int idEpic = foundSubTask.getIdEpic();
        historyManager.remove(idSubTask);
        subTasks.remove(idSubTask);
        Epic foundEpic = epics.get(idEpic);

        if (foundEpic != null) {
            ArrayList<Integer> idSubTasks = foundEpic.getIdSubTasks();
            idSubTasks.remove(idSubTasks.indexOf(idSubTask));
            Status statusEpic = updateStatusEpic(idSubTasks);
            foundEpic.setStatus(statusEpic);
        }
    }

    @Override
    public void deleteAllSubTasks() {
        for(int idTask:subTasks.keySet()) {
            historyManager.remove(idTask);
        }
        subTasks.clear();

        for (Epic epic : epics.values()) {
            ArrayList<Integer> idSubTasks = epic.getIdSubTasks();
            idSubTasks.clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        ArrayList<SubTask> arraySubTasks = new ArrayList<>();
        int idEpic = epic.getTaskId();

        for (SubTask subTask : subTasks.values()) {
            if (idEpic == subTask.getIdEpic()) {
                arraySubTasks.add(subTask);
            }
        }

        return arraySubTasks;
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    private Status updateStatusEpic(ArrayList<Integer> idSubTaskOfEpic) {
        ArrayList<Status> statuses = new ArrayList<>();
        Status statusEpic;
        boolean allDone = true;
        boolean allNew = true;

        for (int idSubTask : idSubTaskOfEpic) {
            if (subTasks.keySet().contains(idSubTask)) {
                Status status = subTasks.get(idSubTask).getStatus();
                statuses.add(status);
            }
        }

        for (Status statusSubTask : statuses) {
            if (!statusSubTask.equals(Status.DONE)) {
                allDone = false;
            }

            if (!statusSubTask.equals(Status.NEW)) {
                allNew = false;
            }
        }

        if (allDone) {
            statusEpic = Status.DONE;
        } else if (statuses.isEmpty() || allNew) {
            statusEpic = Status.NEW;
        } else {
            statusEpic = Status.IN_PROGRESS;
        }

        return statusEpic;
    }

    private int generateId() {
        id++;

        return id;
    }
}
