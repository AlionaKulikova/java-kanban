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

public class HttpTaskManagerSubTasksTest {

    TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
    Server taskServer = new Server(taskManager);

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public HttpTaskManagerSubTasksTest() throws IOException {
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
    public void testAddSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        ArrayList<SubTask> subTasksFromManager = taskManager.getAllSubTasks();

        assertNotNull(subTasksFromManager, "Подадачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test addNewSubTask", subTasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Задачи не возвращаются");
    }

    @Test
    public void testGetSubTaskId() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        SubTask subTask = taskManager.createSubTask(new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int subTaskId = subTask.getTaskId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks/" + subTaskId);
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Подзадача не возвращаются");
    }

    @Test
    public void testDeleteAllSubTasks() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        taskManager.createSubTask(new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());
        ArrayList<SubTask> subTasksFromManager = taskManager.getAllSubTasks();

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Подзадача не возвращаются");
        assertEquals(0, subTasksFromManager.size(), "Некорректное количество подзадач");
    }

    @Test
    public void testDeleteSubTaskId() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Test addNewEpic", "Test addNewEpic description", Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        SubTask subTask = taskManager.createSubTask(new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic.getTaskId(), Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int subTaskId = subTask.getTaskId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks/" + subTaskId);
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());
        ArrayList<SubTask> subTasksFromManager = taskManager.getAllSubTasks();

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Подзадача не возвращаются");
        assertEquals(0, subTasksFromManager.size(), "Некорректное количество подзадач");
    }
}