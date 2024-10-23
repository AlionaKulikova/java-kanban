package http.handlers;

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


public class SubTasksHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public SubTasksHandler(TaskManager taskManager) {
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
                        System.out.println((id));
                        SubTask subTask = taskManager.getSubTask(id);
                        if (subTask != null) {//если эпик не найден нужно возвращать null????
                            response = gson.toJson(subTask);
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
                    if (taskManager.getAllSubTasks() != null) {
                        String jsonString = gson.toJson(taskManager.getAllSubTasks());
                        response = gson.toJson(jsonString);
                    } else {
                        response = "массив подзадач пуст";
                    }
                }
                break;

            case "POST":
                String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                try {
                    SubTask subTask = gson.fromJson(bodyRequest, SubTask.class);
                    int id = subTask.getTaskId();

                    if (id != 0) {
                        taskManager.updateSubTask(subTask);
                        statusCode = 201;
                        response = "Задача с id: " + id + " обновлена";
                    } else {
                        SubTask subTaskCreated = taskManager.createSubTask(subTask);
                        int idCreated = subTaskCreated.getTaskId();

                        boolean isIntersection = taskManager.checkTimeIntersection(subTask);
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
                        taskManager.deleteSubTask(id);
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
                    taskManager.deleteAllSubTasks();
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
