package manager;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }

    public static TaskManager getInMemoryTaskManager(HistoryManager historyManager) {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        return taskManager;
    }
}
