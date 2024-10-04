package manager.test;

import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {
    public static final Path path = Path.of("test.csv");
    File file;
    FileBackedTasksManager managerFile;

    @BeforeEach
    public void beforeEach() {
        file = new File(String.valueOf(path));
        managerFile = new FileBackedTasksManager(file, Managers.getDefaultHistory());

        managerFile.deleteAllTasks();
        managerFile.deleteAllEpics();
        managerFile.deleteAllSubTasks();
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void shouldCorrectlySaveDataToFileAndLoadDataFromFile() {
        Task taskOne = managerFile.createTask(new Task("Купить билет", "Купить билет на поезд. На 12-ое "
                + "августа."));

        managerFile.loadFromFile(file);

        assertEquals(List.of(taskOne), managerFile.getAllTasks());
    }

    @Test
    public void shouldCorrectlySaveEmptyTasksToFileAndLoadEmptyTasksFromFile() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file, Managers.getDefaultHistory());

        fileManager.save();
        fileManager.loadFromFile(file);

        assertEquals(Collections.emptyList(), fileManager.getAllTasks());
        assertEquals(Collections.emptyList(), fileManager.getAllEpics());
        assertEquals(Collections.emptyList(), fileManager.getAllSubTasks());
    }
}