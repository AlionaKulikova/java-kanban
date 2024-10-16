package manager.test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());

    @BeforeEach
    void beforeEach() {
        manager.deleteAllSubTasks();
        manager.deleteAllEpics();
        manager.deleteAllTasks();
    }

    @Test
    void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        final int taskId = manager.createTask(task).getTaskId();
        final Task savedTask = manager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

        Task taskTwo = new Task("Test addNewTaskTwo", "Test addNewTaskTwo description", Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        taskTwo.setTaskId(5);
        final int taskTwoId = taskTwo.getTaskId();

        manager.createTask(taskTwo);
        Task foundTask = manager.getTask(taskTwo.getTaskId());

        assertNotEquals(foundTask.getTaskId(), taskTwoId, "метод 'createTask' не проигнорировал добавленный вручную id");
        assertNotEquals(taskTwo, task, "Разные задачи равны");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        final int taskId = manager.createEpic(epic).getTaskId();
        final Epic savedEpic = manager.getEpic(taskId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают");

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        Epic epicTwo = new Epic("Test addNewEpicTwo", "Test addNewEpicTwo description", Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        epicTwo.setTaskId(55);
        final int epicTwoId = epicTwo.getTaskId();

        manager.createEpic(epicTwo);
        Epic foundEpic = manager.getEpic(epicTwo.getTaskId());

        assertNotEquals(foundEpic.getTaskId(), epicTwoId, "метод 'createEpic' не проигнорировал добавленный вручную id");
        assertNotEquals(epicTwo, epic, "Разные эпики равны");
    }

    @Test
    void shouldEpicHaveSubtaskInItsArray() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createSubTask(subTask);
        Epic foundEpic = manager.getEpic(epic.getTaskId());
        ArrayList<Integer> foundSubTasksOfEpic = foundEpic.getIdSubTasks();//получаем id подзадач эпика
        SubTask foundSubTask = manager.getSubTask(foundSubTasksOfEpic.get(0));
        assertEquals(subTask, foundSubTask, "Подзадачи эпика не совпадают");
    }

    @Test
    void createSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        final int subTaskId = manager.createSubTask(subTask).getTaskId();
        final SubTask savedSubTask = manager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");

        final List<SubTask> subTasks = manager.getAllSubTasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");

        SubTask subTaskTwo = new SubTask("Test addNewSubTaskTwo", "Test addNewSubTaskTwo description", epic.getTaskId(), Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        subTaskTwo.setTaskId(88);
        final int subTaskTwoId = subTaskTwo.getTaskId();

        manager.createSubTask(subTaskTwo);
        SubTask foundSubTask = manager.getSubTask(subTaskTwo.getTaskId());

        assertNotEquals(foundSubTask.getTaskId(), subTaskTwoId, "метод 'createSubTask' не проигнорировал добавленный вручную id");
        assertNotEquals(subTaskTwo, subTask, "Разные подзадачи равны");
    }

    @Test
    void shouldSubtaskHaveEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createSubTask(subTask);
        SubTask foundSubTask = manager.getSubTask(subTask.getTaskId());
        final int idEpicOfFoundSubtask = foundSubTask.getIdEpic();
        Epic foundEpic = manager.getEpic(idEpicOfFoundSubtask);
        assertEquals(epic, foundEpic, "Эпики подзадачи не совпадают");
    }

    @Test
    void AfterDeletingSubtasksTheEpicShouldNotHaveTheseSubtasks() {
        Epic epicOne = new Epic("Test addNewEpicOne", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createEpic(epicOne);
        SubTask subTaskOne = new SubTask("Test addNewSubTaskOne", "Test addNewSubTask description", epicOne.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createSubTask(subTaskOne);
        SubTask subTaskTwo = new SubTask("Test addNewSubTask", "Test addNewSubTaskTwo description", epicOne.getTaskId(), Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createSubTask(subTaskTwo);

        manager.deleteAllSubTasks();
        Epic foundEpic = manager.getEpic(epicOne.getTaskId());

        assertEquals(0, foundEpic.getIdSubTasks().size(), "У эпика хранятся удаленные подзадачи");
    }

    @Test
    void DeletedSubtasksShouldNotStoreOldId() {
        Epic epicOne = new Epic("Test addNewEpicOne", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createEpic(epicOne);
        SubTask subTaskOne = new SubTask("Test addNewSubTaskOne", "Test addNewSubTask description", epicOne.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createSubTask(subTaskOne);
        SubTask subTaskTwo = new SubTask("Test addNewSubTask", "Test addNewSubTaskTwo description", epicOne.getTaskId(), Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createSubTask(subTaskTwo);

        int idSubTask = subTaskTwo.getTaskId();
        manager.deleteSubTask(subTaskTwo.getTaskId());
        ArrayList<SubTask> allSubTasks = manager.getAllSubTasks();
        Boolean foundSubTask = allSubTasks.contains(idSubTask);

        Assertions.assertFalse(foundSubTask, "Удаленная подзадача хранит id");
    }

    @Test
    void taskInstanceSettersChangeAnyOfTheirFields() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        final int taskId = manager.createTask(task).getTaskId();
        final Task savedTask = manager.getTask(taskId);
        task.setTaskId(100);
        int modifiedId = task.getTaskId();
        assertNotEquals(modifiedId, taskId, "Id задачи не изменился");
    }

    @Test
    void subTaskInstanceSettersChangeAnyOfTheirFields() {
        Epic epicOne = new Epic("Test addNewEpicOne", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createEpic(epicOne);
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epicOne.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        final int subTaskId = manager.createSubTask(subTask).getTaskId();
        final SubTask savedSubTask = manager.getSubTask(subTaskId);
        savedSubTask.setTaskId(100);
        int modifiedId = subTask.getTaskId();
        assertNotEquals(modifiedId, subTaskId, "Id подзадачи не изменился");
    }

    @Test
    void EpicInstanceSettersChangeAnyOfTheirFields() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        manager.createEpic(epic);
        final int epicId = manager.createEpic(epic).getTaskId();
        final Epic savedEpic = manager.getEpic(epicId);
        savedEpic.setTaskId(100);
        int modifiedId = savedEpic.getTaskId();
        assertNotEquals(modifiedId, epicId, "Id эпика не изменился");
    }
}