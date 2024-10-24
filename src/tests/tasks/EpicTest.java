package tests.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import tasks.Epic;

class EpicTest {
    int epicId = 1;
    Epic epicOne = new Epic("Test addNewEpic", "Test addNewEpic description");
    Epic epicTwo = new Epic("Test addNewEpic", "Test addNewEpic description");

    @Test
    public void shouldReturnEquals() {
        epicOne.setTaskId(epicId);
        epicTwo.setTaskId(epicId);
        assertEquals(epicOne, epicTwo, "Объекты эпиков с одинаковыми id не равны");
    }
}
