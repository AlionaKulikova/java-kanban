package tasks.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import tasks.Task;

class TaskTest {
    int taskId = 1;
    Task taskOne = new Task("Test addNewTask", "Test addNewTask description");
    Task taskTwo = new Task("Test addNewTask", "Test addNewTask description");

    @Test
    public void shouldReturnEquals() {
        taskOne.setTaskId(taskId);
        taskTwo.setTaskId(taskId);
        assertEquals(taskOne, taskTwo, "Объекты задач с одинаковыми id не равны");
    }
}