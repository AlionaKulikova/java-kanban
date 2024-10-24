package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.InstantAdapter;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;

public class EpicsHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
                        Epic epic = taskManager.getEpic(id);

                        if (epic != null) {
                            response = gson.toJson(epic);
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
                } else if (splitStrings.length == 4) {
                    query = splitStrings[2];
                    try {
                        int id = Integer.parseInt(query);
                        Epic epic = taskManager.getEpic(id);

                        if (epic != null) {
                            ArrayList<SubTask> subTaskOfEpic = taskManager.getSubTasksOfEpic(epic);
                            response = gson.toJson(subTaskOfEpic);
                            statusCode = 200;
                        } else {
                            statusCode = 404;
                            response = "Эпик не найден";
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
                    if (taskManager.getAllEpics() != null) {
                        String jsonString = gson.toJson(taskManager.getAllEpics());
                        response = gson.toJson(jsonString);
                    } else {
                        response = "Массив эпиков пуст";
                    }
                }
                break;

            case "POST":
                String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Epic epic = gson.fromJson(bodyRequest, Epic.class);
                    int id = epic.getTaskId();
                    if (id != 0) {
                        taskManager.updateEpic(epic);
                        statusCode = 201;
                        response = "Задача с id: " + id + " обновлена";
                    } else {
                        Epic epicCreated = taskManager.createEpic(epic);
                        int idCreated = epicCreated.getTaskId();
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
                        taskManager.deleteEpic(id);
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
                    taskManager.deleteAllEpics();
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
