package http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.InstantAdapter;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


public class TasksHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int statusCode;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String query;

        switch (method) {
            case "GET":
                if (splitStrings.length == 3) {
                    query = splitStrings[2];

                    try {
                        int id = Integer.parseInt(query);
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            response = gson.toJson(task);
                            statusCode = 200;
                        } else {
                            statusCode = 404;
                            response = "Задача с данным id не найдена";
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "Нет параметра id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                } else {
                    statusCode = 200;
                    String jsonString = gson.toJson(taskManager.getAllTasks());
                    response = gson.toJson(jsonString);
                }
                break;
            case "POST":
                String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                try {
                    Task task = gson.fromJson(bodyRequest, Task.class);
                    int id = task.getTaskId();

                    if (id != 0) {
                        taskManager.updateTask(task);
                        statusCode = 201;
                        response = "Задача с id: " + id + " обновлена";
                    } else {
                        Task taskCreated = taskManager.createTask(task);
                        int idCreated = taskCreated.getTaskId();

                        boolean isIntersection = taskManager.checkTimeIntersection(task);
                        if (isIntersection) {
                            statusCode = 406;
                            response = "Задача пересекается во времени с существующими.";
                            break;
                        }

                        statusCode = 201;
                        response = "Создана задача с id: " + idCreated;
                    }
                } catch (JsonSyntaxException e) {
                    statusCode = 400;
                    response = "Неверный формат запроса";
                }
                break;
            case "DELETE":
                if (splitStrings.length == 3) {
                    query = splitStrings[2];

                    try {
                        int id = Integer.parseInt(query);
                        taskManager.deleteTask(id);
                        statusCode = 200;
                        response = "Удалили задачу с id: " + id;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "Нет параметра id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                } else {
                    taskManager.deleteAllTasks();
                    statusCode = 200;
                    response = "Удалили все задачи";
                }
                break;
            default:
                statusCode = 400;
                response = "Некорректный запрос";
        }
        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
