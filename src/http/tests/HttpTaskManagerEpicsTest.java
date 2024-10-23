package http.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.InstantAdapter;
import http.Server;
import manager.Managers;
import manager.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class HttpTaskManagerEpicsTest {

    TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
    Server taskServer = new Server(taskManager);

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public HttpTaskManagerEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        ArrayList<Epic> epicsFromManager = taskManager.getAllEpics();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Test addNewEpic", epicsFromManager.get(0).getName(), "Некорректное имя эпика");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Эпики не возвращаются");
    }

    @Test
    public void testGetEpicId() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int epicId = epic.getTaskId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Эпик не возвращается");
    }

    @Test
    public void testGetEpicIdSubTask() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int epicId = epic.getTaskId();
        SubTask subTask = taskManager.createSubTask(new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int subTaskId = subTask.getTaskId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/" + subTaskId);
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Эпик не возвращается");
    }

    @Test
    public void testDeleteAllEpics() throws IOException, InterruptedException {
        taskManager.createEpic(new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> tasksFromManager = taskManager.getAllTasks();

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Эпики не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество эпиков");
    }

    @Test
    public void testDeleteEpicId() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int epicId = epic.getTaskId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> tasksFromManager = taskManager.getAllTasks();

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Задача не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }
}