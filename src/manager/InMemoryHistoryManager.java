package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_SIZE_VIEWED_TASKS = 10;
    ArrayList<Task> viewedTasks = new ArrayList<>();

    @Override
    public void addTaskToHistory(Task task) {
        if (viewedTasks.size() >= MAX_SIZE_VIEWED_TASKS) {
            viewedTasks.remove(0);
        }
        viewedTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}
