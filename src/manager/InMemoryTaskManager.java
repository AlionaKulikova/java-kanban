package manager;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
    private Comparator<Task> comparatorOfTask = Comparator.comparing(Task::getStartTime);
    protected Set<Task> prioritizedTasks = new TreeSet<>(comparatorOfTask);

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }

        int taskId = generateId();
        task.setTaskId(taskId);
        tasks.put(task.getTaskId(), task);
        addPrioritizedTask(task);

        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> arrayTasks = new ArrayList<>();
        tasks.values().forEach(task -> arrayTasks.add(task));

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
        addPrioritizedTask(task);

        return task;
    }

    @Override
    public void deleteTask(int idTask) {
        historyManager.remove(idTask);
        tasks.remove(idTask);
        prioritizedTasks.removeIf(task -> task.getTaskId() == idTask);
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }

        int taskId = generateId();
        epic.setTaskId(taskId);
        epics.put(epic.getTaskId(), epic);

        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> arrayEpics = new ArrayList<>();
        epics.values().forEach(epic -> arrayEpics.add(epic));

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

        idSubTasksDeleted.forEach(idSubTask -> {
            prioritizedTasks.removeIf(task -> Objects.equals(task.getTaskId(), idSubTask));
            historyManager.remove(idSubTask);
            subTasks.remove(idSubTask);
        });
        historyManager.remove(idEpic);
        epics.remove(idEpic);
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        subTasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subTasks.clear();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        if (subTask == null) {
            return null;
        }

        int taskId = generateId();
        subTask.setTaskId(taskId);
        addPrioritizedTask(subTask);
        subTasks.put(subTask.getTaskId(), subTask);
        int idEpic = subTask.getIdEpic();

        if (epics.keySet().contains(idEpic)) {
            Epic foundEpic = epics.get(idEpic);
            foundEpic.getIdSubTasks().add(subTask.getTaskId());
            ArrayList<Integer> idSubTaskOfEpic = epics.get(subTask.getIdEpic()).getIdSubTasks();
            Status statusEpic = updateStatusEpic(idSubTaskOfEpic);
            updateTimeOfEpic(foundEpic);
            foundEpic.setStatus(statusEpic);
        }

        return subTask;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public SubTask getSubTask(int idSubTask) {
        historyManager.addTaskToHistory(subTasks.get(idSubTask));

        return subTasks.get(idSubTask);
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        addPrioritizedTask(subTask);
        subTasks.put(subTask.getTaskId(), subTask);
        Integer idEpic = subTask.getIdEpic();

        if (epics.containsKey(idEpic)) {
            Epic foundEpic = epics.get(idEpic);
            ArrayList<Integer> idSubTaskOfEpic = foundEpic.getIdSubTasks();
            Status statusEpic = updateStatusEpic(idSubTaskOfEpic);
            updateTimeOfEpic(foundEpic);
            foundEpic.setStatus(statusEpic);
        }

        return subTask;
    }

    @Override
    public void deleteSubTask(int idSubTask) {
        SubTask foundSubTask = subTasks.get(idSubTask);
        int idEpic = foundSubTask.getIdEpic();
        historyManager.remove(idSubTask);
        prioritizedTasks.remove(foundSubTask);
        subTasks.remove(idSubTask);
        Epic foundEpic = epics.get(idEpic);

        if (foundEpic != null) {
            ArrayList<Integer> idSubTasks = foundEpic.getIdSubTasks();
            idSubTasks.remove((Integer) idSubTask);
            Status statusEpic = updateStatusEpic(idSubTasks);
            updateTimeOfEpic(foundEpic);
            foundEpic.setStatus(statusEpic);
        }
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.keySet().forEach((idTask) -> {
            SubTask subTask = subTasks.get(idTask);
            prioritizedTasks.remove(subTask);
            historyManager.remove(idTask);
        });
        subTasks.clear();

        epics.values().forEach((epic) -> {
            ArrayList<Integer> idSubTasks = epic.getIdSubTasks();
            idSubTasks.clear();
            epic.setStatus(Status.NEW);
        });
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        int idEpic = epic.getTaskId();

        return subTasks.values().stream()
                .filter(subTask -> idEpic == subTask.getIdEpic())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Status updateStatusEpic(ArrayList<Integer> idSubTaskOfEpic) {
        Status statusEpic;
        boolean allDone = true;
        boolean allNew = true;

        ArrayList<Status> statuses = idSubTaskOfEpic.stream().filter(subTasks::containsKey)
                .map(subTasks::get)
                .map(SubTask::getStatus)
                .collect(Collectors.toCollection(ArrayList::new));

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
        } else if (allNew) {
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

    public void updateTimeOfEpic(Epic epic) {
        List<SubTask> subTasks = getSubTasksOfEpic(epic);
        Instant startTime = subTasks.getFirst().getStartTime();
        Instant endTime = subTasks.getFirst().getEndTime();

        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
            if (subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        long duration = endTime.toEpochMilli() - startTime.toEpochMilli();
        epic.setDuration(duration);
    }

    private void addPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        validatePrioritizedTask();
    }

    public boolean checkTimeIntersection(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        int countNullTime = 0;
        if (!tasks.isEmpty()) {
            for (Task taskSave : tasks) {
                if (taskSave.getStartTime() != null && taskSave.getEndTime() != null) {
                    if (task.getStartTime().isBefore(taskSave.getStartTime())
                            && task.getEndTime().isBefore(taskSave.getStartTime())) {
                        return true;
                    } else if (task.getStartTime().isAfter(taskSave.getEndTime())
                            && task.getEndTime().isAfter(taskSave.getEndTime())) {
                        return true;
                    }
                } else {
                    countNullTime++;
                }
            }

            return countNullTime == tasks.size();
        } else {
            return true;
        }
    }

    private void validatePrioritizedTask() {
        List<Task> tasks = getPrioritizedTasks();
        tasks.forEach((task) -> {
            boolean isIntersection = checkTimeIntersection(task);

            if (isIntersection) {
                throw new ManagerValidateException("Задачи пересекаются во времени.");
            }
        });
    }

    private List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }
}
