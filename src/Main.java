import manager.Managers;
import manager.TaskManager;
import status.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

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

        SubTask subTaskOneOfEpicOneNew = new SubTask(subTaskOneOfEpicOne.getName(), subTaskOneOfEpicOne.getDescription(), subTaskOneOfEpicOne.getIdEpic());
        subTaskOneOfEpicOneNew.setTaskId(subTaskOneOfEpicOne.getTaskId());
        subTaskOneOfEpicOneNew.setStatus(Status.DONE);
        manager.updateSubTask(subTaskOneOfEpicOneNew);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskOneOfEpicOne.getName() + "':");
        System.out.println(manager.getSubTask(subTaskOneOfEpicOne.getTaskId()));
        System.out.println("");

        SubTask subTaskTwoOfEpicOneNew = new SubTask(subTaskTwoOfEpicOne.getName(), subTaskTwoOfEpicOne.getDescription(), subTaskTwoOfEpicOne.getIdEpic());
        subTaskTwoOfEpicOneNew.setTaskId(subTaskTwoOfEpicOne.getTaskId());
        subTaskTwoOfEpicOneNew.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTaskTwoOfEpicOneNew);
        System.out.println("Обновили статус подзадачи " + "'" + subTaskTwoOfEpicOne.getName() + "':");
        System.out.println(manager.getSubTask(subTaskTwoOfEpicOne.getTaskId()));
        System.out.println("");

        SubTask subTaskOneOfEpicTwoNew = new SubTask(subTaskOneOfEpicTwo.getName(), subTaskOneOfEpicTwo.getDescription(), subTaskOneOfEpicTwo.getIdEpic());
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

        Task taskOneSp = manager.createTask(new Task("Купить билет", "Купить билет на поезд. На 12-ое "
                + "августа."));
        Task taskTwoSp = manager.createTask(new Task("Отправить письмо", "Сообщить друзьям о своём "
                + "приезде."));

        Epic epicOneSp = manager.createEpic(new Epic("Прочитать книгу", "Прочитать книгу за два дня."));
        SubTask subTaskOneOfEpicOneSp = manager.createSubTask(new SubTask("Прочитать первые 10 глав",
                "Прочитать за один день", epicOneSp.getTaskId()));
        SubTask subTaskTwoOfEpicOneSp = manager.createSubTask(new SubTask("Прочитать еще 5 глав",
                "Прочитать за один день", epicOneSp.getTaskId()));
        SubTask subTaskThreeOfEpicOneSp = manager.createSubTask(new SubTask("Прочитать оставшиеся 4 главы",
                "Прочитать за один день", epicOneSp.getTaskId()));

        Epic epicTwoSp = manager.createEpic(new Epic("Приготовить ужин", "Ужин на четверых"));

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
        System.out.println("Просмотрели задачу: " + manager.getTask(taskOneSp.getTaskId()));
        System.out.println("Просмотрели повторно задачу: " + manager.getTask(taskOneSp.getTaskId()));
        System.out.println("Просмотрели задачу: " + manager.getTask(taskTwoSp.getTaskId()));
        System.out.println("Просмотрели повторно задачу: " + manager.getTask(taskTwoSp.getTaskId()));


        System.out.println("Просмотрели эпик: " + manager.getEpic(epicOneSp.getTaskId()));
        System.out.println("Просмотрели повторно эпик: " + manager.getEpic(epicOneSp.getTaskId()));
        System.out.println("Просмотрели эпик: " + manager.getEpic(epicTwoSp.getTaskId()));
        System.out.println("Просмотрели повторно эпик: " + manager.getEpic(epicTwoSp.getTaskId()));


        System.out.println("Просмотрели подзадачу: " + manager.getSubTask(subTaskOneOfEpicOneSp.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: " + manager.getSubTask(subTaskOneOfEpicOneSp.getTaskId()));
        System.out.println("Просмотрели подзадачу: " + manager.getSubTask(subTaskTwoOfEpicOneSp.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: " + manager.getSubTask(subTaskTwoOfEpicOneSp.getTaskId()));
        System.out.println("Просмотрели подзадачу: " + manager.getSubTask(subTaskThreeOfEpicOneSp.getTaskId()));
        System.out.println("Просмотрели повторно подзадачу: " + manager.getSubTask(subTaskThreeOfEpicOneSp.getTaskId()));

        System.out.println("");
        System.out.println("История после просмотров:");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        manager.deleteTask(taskOneSp.getTaskId());
        System.out.println("");
        System.out.println("Удалили задачу " + "'" + taskOneSp.getName() + "'. ");

        System.out.println("");
        System.out.println("История после удаления задачи :" + taskOneSp.getName());

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        manager.deleteEpic(epicOneSp.getTaskId());
        System.out.println("");
        System.out.println("Удалили эпик " + "'" + epicOneSp.getName() + "'. ");

        System.out.println("");
        System.out.println("История после удаления эпика :" + epicOneSp.getName());

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
} 