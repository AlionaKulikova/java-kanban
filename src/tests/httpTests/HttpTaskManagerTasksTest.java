package tests.httpTests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.InstantAdapter;
import http.Server;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
    Server taskServer = new Server(taskManager);

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public HttpTaskManagerTasksTest() throws IOException {
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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        ArrayList<Task> tasksFromManager = taskManager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Задачи не возвращаются");
    }

    @Test
    public void testGetTaskId() throws IOException, InterruptedException {
        Task task = taskManager.createTask(new Task("Test 2", "Testing task 2",
                Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int taskId = task.getTaskId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Задача не возвращаются");
    }

    @Test
    public void testDeleteAllTasks() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Test 2", "Testing task 2",
                Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> tasksFromManager = taskManager.getAllTasks();

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Задача не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testDeleteTaskId() throws IOException, InterruptedException {
        Task task = taskManager.createTask(new Task("Test 2", "Testing task 2",
                Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli()));
        int taskId = task.getTaskId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest requestGetId = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(requestGetId, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> tasksFromManager = taskManager.getAllTasks();

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Задача не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }
}