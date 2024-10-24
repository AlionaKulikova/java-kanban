package tests.tasks;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {
    int subTaskId = 1;
    Epic epicOne = new Epic("Test addNewEpic", "Test addNewEpic description");
    SubTask subTaskOne = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epicOne.getTaskId());
    SubTask subTaskTwo = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epicOne.getTaskId());


    @Test
    public void shouldReturnEquals() {
        subTaskOne.setTaskId(subTaskId);
        subTaskTwo.setTaskId(subTaskId);
        assertEquals(subTaskOne, subTaskTwo, "Объекты подзадач с одинаковыми id не равны");
    }
}