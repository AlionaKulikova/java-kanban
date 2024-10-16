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
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {
    public static final Path path = Path.of("test.csv");
    File file;
    FileBackedTasksManager managerFileConstructor;
    FileBackedTasksManager managerFile;


    @BeforeEach
    public void beforeEach() {
        file = new File(String.valueOf(path));
        managerFileConstructor = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        managerFileConstructor.deleteAllTasks();
        managerFileConstructor.deleteAllEpics();
        managerFileConstructor.deleteAllSubTasks();
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
        Task taskOneConstructor = managerFileConstructor.createTask(new Task("Купить билет", "Купить билет на поезд. На"
                + "12-ое "
                + "августа.", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()  ));
        Task taskTwoConstructor = managerFileConstructor.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
                + "приезде.", Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));

        managerFileConstructor.deleteTask(taskOneConstructor.getTaskId());
        managerFile = FileBackedTasksManager.loadFromFile(file, Managers.getDefaultHistory());

        assertEquals(List.of(managerFileConstructor.getAllTasks().size()), List.of(managerFile.getAllTasks().size()));
    }

    @Test
    public void shouldCorrectlySaveEmptyTasksToFileAndLoadEmptyTasksFromFile() {
        Task taskOneConstructor = managerFileConstructor.createTask(new Task("Купить билет", "Купить билет на поезд. На"
                + "12-ое "
                + "августа.", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        Task taskTwoConstructor = managerFileConstructor.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
                + "приезде.", Instant.now().plus(Duration.ofHours(5)), Instant.now().plus(Duration.ofHours(1)).toEpochMilli() ));

        managerFileConstructor.deleteAllTasks();
        managerFile = FileBackedTasksManager.loadFromFile(file, Managers.getDefaultHistory());

        assertEquals(Collections.emptyList(), managerFile.getAllTasks());
        assertEquals(Collections.emptyList(), managerFile.getAllEpics());
        assertEquals(Collections.emptyList(), managerFile.getAllSubTasks());
    }
}