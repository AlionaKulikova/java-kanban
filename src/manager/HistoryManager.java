package manager;

import java.util.List;

import tasks.Task;

public interface HistoryManager {
    void addTaskToHistory(Task task);

    void remove(int id);

    List<Task> getHistory();
}
