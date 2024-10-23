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
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

public class HttpTaskManagerHistoryTest {

    TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
    Server taskServer = new Server(taskManager);
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public HttpTaskManagerHistoryTest() throws IOException {
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
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Instant.now(), Instant.now().plus(Duration.ofHours(1)).toEpochMilli());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response, "Задачи не возвращаются");
    }

}