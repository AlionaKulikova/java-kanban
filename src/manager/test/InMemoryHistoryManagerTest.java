package manager.test;

import static org.junit.jupiter.api.Assertions.*;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = new InMemoryHistoryManager();
    Task task = new Task("Test addNewTask", "Test addNewTask description");

    @Test
    void shouldNotBeNull() {
        historyManager.addTaskToHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void tasksShouldBeEqual() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        historyManager.addTaskToHistory(task);
        historyManager.addTaskToHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(history.get(0), history.get(1), "Задачи не равны.");
    }
}