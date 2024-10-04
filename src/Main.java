import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import status.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.nio.file.Path;

class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());

        Task taskOne = manager.createTask(new Task("Купить билет", "Купить билет на поезд. На 12-ое "
                + "августа."));
        Task taskTwo = manager.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
                + "приезде."));

        Epic epicOne = manager.createEpic(new Epic("Прочитать книгу", "Прочитать книгу за два дня."));
        SubTask subTaskOneOfEpicOne = manager.createSubTask(new SubTask("Прочитать первые 10 глав",
                "Прочитать за один день", epicOne.getTaskId()));
        SubTask subTaskTwoOfEpicOne = manager.createSubTask(new SubTask("Прочитать оставшиеся 9 глав",
                "Прочитать за один день", epicOne.getTaskId()));

        Epic epicTwo = manager.createEpic(new Epic("Приготовить ужин", "Ужин на четверых"));
        SubTask subTaskOneOfEpicTwo = manager.createSubTask(new SubTask("Заказать еду из ресторана",
                "Выбрать ресторан.", epicTwo.getTaskId()));

        System.out.println("Эпики:");
        System.out.println(manager.getAllEpics());
        System.out.println("");
        System.out.println("Задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("");
        System.out.println("Подзадачи:");
        System.out.println(manager.getAllSubTasks());
        System.out.println("");

        Task taskOneNew = new Task(taskOne.getName(), taskOne.getDescription());
        taskOneNew.setTaskId(taskOne.getTaskId());
        taskOneNew.setStatus(Status.DONE);
        manager.updateTask(taskOneNew);
        System.out.println("Обновили статус задачи " + "'" + taskOne.getName() + "':");
        System.out.println((manager.getTask(taskOne.getTaskId())));
        System.out.println("");

        Task taskTwoNew = new Task(taskTwo.getName(), taskTwo.getDescription());
        taskTwoNew.setTaskId(taskTwo.getTaskId());
        taskTwoNew.setStatus(Status.IN_PROGRESS);
        manager.updateTask(taskTwoNew);
        System.out.println("Обновили статус задачи " + "'" + taskTwo.getName() + "':");
        System.out.println(manager.getTask(taskTwo.getTaskId()));
        System.out.println("");

        SubTask subTaskOneOfEpicOneNew = new SubTask(subTaskOneOfEpicOne.getName(),
                subTaskOneOfEpicOne.getDescription(), subTaskOneOfEpicOne.getIdEpic());
        subTaskOneOfEpicOneNew.setTaskId(subTaskOneOfEpicOne.getTaskId());
        subTaskOneOfEpicOneNew.setStatus(Status.DONE);
        manager.updateSubTask(subTaskOneOfEpicOneNew);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskOneOfEpicOne.getName() + "':");
        System.out.println(manager.getSubTask(subTaskOneOfEpicOne.getTaskId()));
        System.out.println("");

        SubTask subTaskTwoOfEpicOneNew = new SubTask(subTaskTwoOfEpicOne.getName(),
                subTaskTwoOfEpicOne.getDescription(), subTaskTwoOfEpicOne.getIdEpic());
        subTaskTwoOfEpicOneNew.setTaskId(subTaskTwoOfEpicOne.getTaskId());
        subTaskTwoOfEpicOneNew.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTaskTwoOfEpicOneNew);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskTwoOfEpicOne.getName() + "':");
        System.out.println(manager.getSubTask(subTaskTwoOfEpicOne.getTaskId()));
        System.out.println("");

        SubTask subTaskOneOfEpicTwoNew = new SubTask(subTaskOneOfEpicTwo.getName(),
                subTaskOneOfEpicTwo.getDescription(), subTaskOneOfEpicTwo.getIdEpic());
        subTaskOneOfEpicTwoNew.setTaskId(subTaskOneOfEpicTwo.getTaskId());
        subTaskOneOfEpicTwoNew.setStatus(Status.DONE);
        manager.updateSubTask(subTaskOneOfEpicTwoNew);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskOneOfEpicTwo.getName() + "':");
        System.out.println(manager.getSubTask(subTaskOneOfEpicTwo.getTaskId()));
        System.out.println("");

        System.out.println("Обновлённые эпики:");
        System.out.println(manager.getAllEpics());
        System.out.println("");

        manager.deleteTask(taskOne.getTaskId());
        System.out.println("Удалили задачу " + "'" + taskOne.getName() + "'. " + "Оставшиеся задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("");

        manager.deleteEpic(epicTwo.getTaskId());
        System.out.println("Удалили эпик " + "'" + epicTwo.getName() + "'. " + "Оставшиеся эпики:");
        System.out.println(manager.getAllEpics());
        System.out.println("");

        System.out.println("Результаты:");
        System.out.println("");
        System.out.println("Задачи:");

        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("");
        System.out.println("Эпики:");

        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksOfEpic(epic)) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("");
        System.out.println("Подзадачи:");

        for (Task subtask : manager.getAllSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("");
        System.out.println("История:");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        for (int i = 0; i < 8; i++) {
            manager.getTask(taskTwo.getTaskId());
        }

        System.out.println("");
        System.out.println("История после просмотра еще 8 задач:");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("");
        System.out.println("");
        System.out.println("Спринт 6");

        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpics();

        System.out.println("");
        System.out.println("Удалили все задачи:");
        System.out.println("");
        System.out.println("Эпики:");
        System.out.println(manager.getAllEpics());
        System.out.println("");
        System.out.println("Задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("");
        System.out.println("Подзадачи:");
        System.out.println(manager.getAllSubTasks());
        System.out.println("");

        System.out.println("");
        System.out.println("История после удаления всех задач:");

        if (manager.getHistory().isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : manager.getHistory()) {
                System.out.println(task);
            }
        }

        System.out.println("");
        System.out.println("Создаем новые задачи:");

        Task taskOneSpSix = manager.createTask(new Task("Купить билет", "Купить билет на поезд. На"
                + "12-ое "
                + "августа."));
        Task taskTwoSpSix = manager.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
                + "приезде."));

        Epic epicOneSpSix = manager.createEpic(new Epic("Прочитать книгу", "Прочитать книгу за два"
                + "дня."));
        SubTask subTaskOneOfEpicOneSpSix = manager.createSubTask(new SubTask("Прочитать первые 10 глав",
                "Прочитать за один день", epicOneSpSix.getTaskId()));
        SubTask subTaskTwoOfEpicOneSpSix = manager.createSubTask(new SubTask("Прочитать еще 5 глав",
                "Прочитать за один день", epicOneSpSix.getTaskId()));
        SubTask subTaskThreeOfEpicOneSpSix = manager.createSubTask(new SubTask("Прочитать оставшиеся 4 главы",
                "Прочитать за один день", epicOneSpSix.getTaskId()));

        Epic epicTwoSpSix = manager.createEpic(new Epic("Приготовить ужин", "Ужин на четверых"));

        System.out.println("");
        System.out.println("Добавили новые задачи для 6 спринта:");
        System.out.println("");
        System.out.println("Эпики:");
        System.out.println(manager.getAllEpics());
        System.out.println("");
        System.out.println("Задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("");
        System.out.println("Подзадачи:");
        System.out.println(manager.getAllSubTasks());
        System.out.println("");

        System.out.println("");
        System.out.println("История:");

        if (manager.getHistory().isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : manager.getHistory()) {
                System.out.println(task);
            }
        }

        System.out.println("");
        System.out.println("Просмотрели задачу: " + manager.getTask(taskOneSpSix.getTaskId()));
        System.out.println("Просмотрели повторно задачу: " + manager.getTask(taskOneSpSix.getTaskId()));
        System.out.println("Просмотрели задачу: " + manager.getTask(taskTwoSpSix.getTaskId()));
        System.out.println("Просмотрели повторно задачу: " + manager.getTask(taskTwoSpSix.getTaskId()));

        System.out.println("Просмотрели эпик: " + manager.getEpic(epicOneSpSix.getTaskId()));
        System.out.println("Просмотрели повторно эпик: " + manager.getEpic(epicOneSpSix.getTaskId()));
        System.out.println("Просмотрели эпик: " + manager.getEpic(epicTwoSpSix.getTaskId()));
        System.out.println("Просмотрели повторно эпик: " + manager.getEpic(epicTwoSpSix.getTaskId()));

        System.out.println("Просмотрели подзадачу: " + manager.getSubTask(subTaskOneOfEpicOneSpSix.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: "
                + manager.getSubTask(subTaskOneOfEpicOneSpSix.getTaskId()));
        System.out.println("Просмотрели подзадачу: " + manager.getSubTask(subTaskTwoOfEpicOneSpSix.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: "
                + manager.getSubTask(subTaskTwoOfEpicOneSpSix.getTaskId()));
        System.out.println("Просмотрели подзадачу: " + manager.getSubTask(subTaskThreeOfEpicOneSpSix.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: "
                + manager.getSubTask(subTaskThreeOfEpicOneSpSix.getTaskId()));

        System.out.println("");
        System.out.println("История после просмотров:");
        System.out.println("");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        manager.deleteTask(taskOneSpSix.getTaskId());
        System.out.println("");
        System.out.println("Удалили задачу " + "'" + taskOneSpSix.getName() + "'. ");

        System.out.println("");
        System.out.println("История после удаления задачи '" + taskOneSpSix.getName() + "':");
        System.out.println("");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        manager.deleteEpic(epicOneSpSix.getTaskId());
        System.out.println("");
        System.out.println("Удалили эпик " + "'" + epicOneSpSix.getName() + "'. ");

        System.out.println("");
        System.out.println("История после удаления эпика '" + epicOneSpSix.getName() + "':");
        System.out.println("");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
////////////////////////////////////////////7 sprint///////////////////////////////////////////////////////////////////
        System.out.println("");
        System.out.println("");
        System.out.println("Спринт 7");

        Path path = Path.of("fileWriter.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager managerFile = new FileBackedTasksManager(file, Managers.getDefaultHistory());

        Task taskOneSpSeven = managerFile.createTask(new Task("Купить билет", "Купить билет на "
                + "самолёт."));
        Task taskTwoSpSeven = managerFile.createTask(new Task("Отправить электронное письмо",
                "Сообщить родственникам о своём приезде."));

        Epic epicOneSpSeven = managerFile.createEpic(new Epic("Написать книгу", "Написать книгу "
                + "за 6 месяцев"));
        SubTask subTaskOneOfEpicOneSpSeven = managerFile.createSubTask(new SubTask("Написать первые 20 глав",
                "Прочитать за один день", epicOneSpSeven.getTaskId()));
        SubTask subTaskTwoOfEpicOneSpSeven = managerFile.createSubTask(new SubTask("Написать еще 20 глав",
                "Прочитать за один день", epicOneSpSeven.getTaskId()));
        SubTask subTaskThreeOfEpicOneSpSeven = managerFile.createSubTask(new SubTask("Написать оставшиеся"
                + " 20 глав",
                "Прочитать за один день", epicOneSpSeven.getTaskId()));

        Epic epicTwoSpSeven = manager.createEpic(new Epic("Приготовить обед", "Обед на четверых"));

        System.out.println("");
        System.out.println("Задачи из старого менеджера:");

        Task taskOneSeven = managerFile.createTask(new Task("Купить билет", "Купить билет на поезд. На 12-ое "
                + "августа."));
        Task taskTwoSeven = managerFile.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
                + "приезде."));

        Epic epicOneSeven = managerFile.createEpic(new Epic("Прочитать книгу", "Прочитать книгу за два дня."));
        SubTask subTaskOneOfEpicOneSeven = managerFile.createSubTask(new SubTask("Прочитать первые 10 глав",
                "Прочитать за один день", epicOneSeven.getTaskId()));
        SubTask subTaskTwoOfEpicOneSeven = managerFile.createSubTask(new SubTask("Прочитать оставшиеся 9 глав",
                "Прочитать за один день", epicOneSeven.getTaskId()));

        Epic epicTwoSeven = managerFile.createEpic(new Epic("Приготовить ужин", "Ужин на четверых"));
        SubTask subTaskOneOfEpicTwoSeven = manager.createSubTask(new SubTask("Заказать еду из ресторана",
                "Выбрать ресторан.", epicTwoSeven.getTaskId()));

        System.out.println("Эпики:");
        System.out.println(managerFile.getAllEpics());
        System.out.println("");
        System.out.println("Задачи:");
        System.out.println(managerFile.getAllTasks());
        System.out.println("");
        System.out.println("Подзадачи:");
        System.out.println(managerFile.getAllSubTasks());
        System.out.println("");

        Task taskOneNewSeven = new Task(taskOneSeven.getName(), taskOneSeven.getDescription());
        taskOneNewSeven.setTaskId(taskOneSeven.getTaskId());
        taskOneNewSeven.setStatus(Status.DONE);
        managerFile.updateTask(taskOneNewSeven);
        System.out.println("Обновили статус задачи " + "'" + taskOneSeven.getName() + "':");
        System.out.println((managerFile.getTask(taskOneSeven.getTaskId())));
        System.out.println("");

        Task taskTwoNewSeven = new Task(taskTwoSeven.getName(), taskTwoSeven.getDescription());
        taskTwoNewSeven.setTaskId(taskTwoSeven.getTaskId());
        taskTwoNewSeven.setStatus(Status.IN_PROGRESS);
        managerFile.updateTask(taskTwoNewSeven);
        System.out.println("Обновили статус задачи " + "'" + taskTwoSeven.getName() + "':");
        System.out.println(managerFile.getTask(taskTwoSeven.getTaskId()));
        System.out.println("");

        SubTask subTaskOneOfEpicOneNewSeven = new SubTask(subTaskOneOfEpicOneSeven.getName(),
                subTaskOneOfEpicOneSeven.getDescription(), subTaskOneOfEpicOneSeven.getIdEpic());
        subTaskOneOfEpicOneNewSeven.setTaskId(subTaskOneOfEpicOneSeven.getTaskId());
        subTaskOneOfEpicOneNewSeven.setStatus(Status.DONE);
        managerFile.updateSubTask(subTaskOneOfEpicOneNewSeven);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskOneOfEpicOneSeven.getName() + "':");
        System.out.println(managerFile.getSubTask(subTaskOneOfEpicOneSeven.getTaskId()));
        System.out.println("");

        SubTask subTaskTwoOfEpicOneNewSeven = new SubTask(subTaskTwoOfEpicOneSeven.getName(),
                subTaskTwoOfEpicOneSeven.getDescription(), subTaskTwoOfEpicOneSeven.getIdEpic());
        subTaskTwoOfEpicOneNewSeven.setTaskId(subTaskTwoOfEpicOneSeven.getTaskId());
        subTaskTwoOfEpicOneNewSeven.setStatus(Status.IN_PROGRESS);
        managerFile.updateSubTask(subTaskTwoOfEpicOneNewSeven);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskTwoOfEpicOneSeven.getName() + "':");
        System.out.println(managerFile.getSubTask(subTaskTwoOfEpicOneSeven.getTaskId()));
        System.out.println("");

        SubTask subTaskOneOfEpicTwoNewSeven = new SubTask(subTaskOneOfEpicTwoSeven.getName(),
                subTaskOneOfEpicTwoSeven.getDescription(), subTaskOneOfEpicTwoSeven.getIdEpic());
        subTaskOneOfEpicTwoNewSeven.setTaskId(subTaskOneOfEpicTwoSeven.getTaskId());
        subTaskOneOfEpicTwoNewSeven.setStatus(Status.DONE);
        managerFile.updateSubTask(subTaskOneOfEpicTwoNewSeven);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskOneOfEpicTwoSeven.getName() + "':");
        System.out.println(managerFile.getSubTask(subTaskOneOfEpicTwoSeven.getTaskId()));
        System.out.println("");

        System.out.println("Обновлённые эпики:");
        System.out.println(managerFile.getAllEpics());
        System.out.println("");

        managerFile.deleteTask(taskOne.getTaskId());
        System.out.println("Удалили задачу " + "'" + taskOneSeven.getName() + "'. " + "Оставшиеся задачи:");
        System.out.println(managerFile.getAllTasks());
        System.out.println("");

        managerFile.deleteEpic(epicTwoSeven.getTaskId());
        System.out.println("Удалили эпик " + "'" + epicTwoSeven.getName() + "'. " + "Оставшиеся эпики:");
        System.out.println(managerFile.getAllEpics());
        System.out.println("");

        System.out.println("Результаты:");
        System.out.println("");
        System.out.println("Задачи:");

        for (Task task : managerFile.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("");
        System.out.println("Эпики:");

        for (Epic epic : managerFile.getAllEpics()) {
            System.out.println(epic);

            for (Task task : managerFile.getSubTasksOfEpic(epic)) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("");
        System.out.println("Подзадачи:");

        for (Task subtask : managerFile.getAllSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("");
        System.out.println("История:");

        for (Task task : managerFile.getHistory()) {
            System.out.println(task);
        }

        for (int i = 0; i < 8; i++) {
            managerFile.getTask(taskTwoSeven.getTaskId());
        }

        System.out.println("");
        System.out.println("История после просмотра еще 8 задач:");

        for (Task task : managerFile.getHistory()) {
            System.out.println(task);
        }

        System.out.println("");
        System.out.println("");

        managerFile.deleteAllTasks();
        managerFile.deleteAllSubTasks();
        managerFile.deleteAllEpics();

        System.out.println("");
        System.out.println("Удалили все задачи:");
        System.out.println("");
        System.out.println("Эпики:");
        System.out.println(managerFile.getAllEpics());
        System.out.println("");
        System.out.println("Задачи:");
        System.out.println(managerFile.getAllTasks());
        System.out.println("");
        System.out.println("Подзадачи:");
        System.out.println(managerFile.getAllSubTasks());
        System.out.println("");

        System.out.println("");
        System.out.println("История после удаления всех задач:");

        if (manager.getHistory().isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : managerFile.getHistory()) {
                System.out.println(task);
            }
        }

        System.out.println("");
        System.out.println("Создаем новые задачи:");

        Task taskOneSpSixSeven = managerFile.createTask(new Task("Купить билет", "Купить билет на поезд. На"
                + "12-ое "
                + "августа."));
        Task taskTwoSpSixSeven = managerFile.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
                + "приезде."));

        Epic epicOneSpSixSeven = managerFile.createEpic(new Epic("Прочитать книгу", "Прочитать книгу за два"
                + "дня."));
        SubTask subTaskOneOfEpicOneSpSixSeven = managerFile.createSubTask(new SubTask("Прочитать первые 10 глав",
                "Прочитать за один день", epicOneSpSixSeven.getTaskId()));
        SubTask subTaskTwoOfEpicOneSpSixSeven = managerFile.createSubTask(new SubTask("Прочитать еще 5 глав",
                "Прочитать за один день", epicOneSpSixSeven.getTaskId()));
        SubTask subTaskThreeOfEpicOneSpSixSeven = managerFile.createSubTask(new SubTask("Прочитать оставшиеся 4 главы",
                "Прочитать за один день", epicOneSpSixSeven.getTaskId()));

        Epic epicTwoSpSixSeven = managerFile.createEpic(new Epic("Приготовить ужин", "Ужин на четверых"));

        System.out.println("");
        System.out.println("Добавили новые задачи для 6 спринта:");
        System.out.println("");
        System.out.println("Эпики:");
        System.out.println(managerFile.getAllEpics());
        System.out.println("");
        System.out.println("Задачи:");
        System.out.println(managerFile.getAllTasks());
        System.out.println("");
        System.out.println("Подзадачи:");
        System.out.println(managerFile.getAllSubTasks());
        System.out.println("");

        System.out.println("");
        System.out.println("История:");

        if (manager.getHistory().isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : managerFile.getHistory()) {
                System.out.println(task);
            }
        }

        System.out.println("");
        System.out.println("Просмотрели задачу: " + managerFile.getTask(taskOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели повторно задачу: " + managerFile.getTask(taskOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели задачу: " + managerFile.getTask(taskTwoSpSixSeven.getTaskId()));
        System.out.println("Просмотрели повторно задачу: " + managerFile.getTask(taskTwoSpSixSeven.getTaskId()));

        System.out.println("Просмотрели эпик: " + managerFile.getEpic(epicOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели повторно эпик: " + managerFile.getEpic(epicOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели эпик: " + managerFile.getEpic(epicTwoSpSixSeven.getTaskId()));
        System.out.println("Просмотрели повторно эпик: " + managerFile.getEpic(epicTwoSpSixSeven.getTaskId()));

        System.out.println("Просмотрели подзадачу: " + managerFile.getSubTask(subTaskOneOfEpicOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: "
                + managerFile.getSubTask(subTaskOneOfEpicOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели подзадачу: " + managerFile.getSubTask(subTaskTwoOfEpicOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: "
                + managerFile.getSubTask(subTaskTwoOfEpicOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели подзадачу: " + managerFile.getSubTask(subTaskThreeOfEpicOneSpSixSeven.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: "
                + managerFile.getSubTask(subTaskThreeOfEpicOneSpSixSeven.getTaskId()));

        System.out.println("");
        System.out.println("История после просмотров:");
        System.out.println("");

        for (Task task : managerFile.getHistory()) {
            System.out.println(task);
        }

        managerFile.deleteTask(taskOneSpSixSeven.getTaskId());
        System.out.println("");
        System.out.println("Удалили задачу " + "'" + taskOneSpSixSeven.getName() + "'. ");

        System.out.println("");
        System.out.println("История после удаления задачи '" + taskOneSpSixSeven.getName() + "':");
        System.out.println("");

        for (Task task : managerFile.getHistory()) {
            System.out.println(task);
        }

        managerFile.deleteEpic(epicOneSpSixSeven.getTaskId());
        System.out.println("");
        System.out.println("Удалили эпик " + "'" + epicOneSpSixSeven.getName() + "'. ");

        System.out.println("");
        System.out.println("История после удаления эпика '" + epicOneSpSixSeven.getName() + "':");
        System.out.println("");

        for (Task task : managerFile.getHistory()) {
            System.out.println(task);
        }

        System.out.println("");
        System.out.println("");
        System.out.println("Считываем задачи из файла:");
        managerFile.loadFromFile(file);
    }
} 