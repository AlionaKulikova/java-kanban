package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    ArrayList<Task> getAllTasks();

    Task getTask(int idTask);

    Task updateTask(Task task);

    void deleteTask(int idTask);

    void deleteAllTasks();

    Epic createEpic(Epic epic);

    ArrayList<Epic> getAllEpics();

    Epic getEpic(int idEpic);

    Epic updateEpic(Epic epic);

    void deleteEpic(int idEpic);

    void deleteAllEpics();

    SubTask createSubTask(SubTask subTask);

    ArrayList<SubTask> getAllSubTasks();

    SubTask getSubTask(int idSubTask);

    SubTask updateSubTask(SubTask subTask);

    void deleteSubTask(int idSubTask);

    void deleteAllSubTasks();

    ArrayList<SubTask> getSubTasksOfEpic(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean checkTimeIntersection(Task task);
}
