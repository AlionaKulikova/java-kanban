package tests.managerTests;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager manager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
    Task taskOne = manager.createTask(new Task("Купить билет", "Купить билет на поезд. На 12-ое "
            + "августа.", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
    Task taskTwo = manager.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
            + "приезде.", Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));

    @BeforeEach
    void beforeEach() {
        historyManager.remove(taskOne.getTaskId());
        historyManager.remove(taskTwo.getTaskId());
    }

    @Test
    void shouldNotBeNull() {
        historyManager.addTaskToHistory(taskOne);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void sameTaskShouldNotBeAddedAgain() {
        historyManager.addTaskToHistory(taskOne);
        historyManager.addTaskToHistory(taskOne);

        final List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "Добавилась повторно задача.");
    }

    @Test
    void newTaskShouldBeAddedToHistory() {
        historyManager.addTaskToHistory(taskOne);
        historyManager.addTaskToHistory(taskTwo);

        final List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "Новая задача не добавилась.");
    }

    @Test
    void taskShouldBeAddedAndRemovedFromHistory() {
        historyManager.addTaskToHistory(taskOne);
        historyManager.addTaskToHistory(taskTwo);
        historyManager.remove(taskOne.getTaskId());

        final List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "Задача не удалилась.");
    }
}