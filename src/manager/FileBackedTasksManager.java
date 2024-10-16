package manager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import status.Status;
import typesTasks.TypesTasks;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String HEADER_OF_FILE = "id,type,name,status,description,epic \n";
    private File file;

    public FileBackedTasksManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }

            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при создании файла. Файл не найден. " + e.getMessage());
        }

        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(HEADER_OF_FILE);

            for (Task task : super.getAllTasks()) {
                fileWriter.write(toString(task) + "\n");
            }

            for (Epic epic : super.getAllEpics()) {
                fileWriter.write(toString(epic) + "\n");
            }

            for (SubTask subtask : super.getAllSubTasks()) {
                fileWriter.write(toString(subtask) + "\n");
            }

            fileWriter.write("\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении данных в файл. Данные не сохранены. "
                    + e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file, HistoryManager historyManager) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file, historyManager);

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;

            while (br.ready()) {
                line = br.readLine();

                if (line.equals("")) {
                    break;
                }

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                Task task = fromString(line);

                if (task instanceof Epic epic) {
                    manager.addEpic(epic);
                } else if (task instanceof SubTask subTask) {
                    manager.addSubTask(subTask);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось получить данные из файла.");
        }

        return manager;
    }

    private void addTask(Task task) {
        int idTask=task.getTaskId();

     Task resultTask = super.createTask(task);

     resultTask.setTaskId(idTask);
    }

    private void addEpic(Epic epic) {
        int idEpic=epic.getTaskId();

        Epic resultEpic = super.createEpic(epic);
        resultEpic.setTaskId(idEpic);
    }

    private void addSubTask(SubTask subTask) {
        int idSubTask=subTask.getTaskId();

        SubTask resultSubTask = super.createSubTask(subTask);

        resultSubTask.setTaskId(idSubTask);
    }

    private static TypesTasks getTypeOfTask(Task task) {
        if (task instanceof Epic) {

            return TypesTasks.EPIC;
        } else if (task instanceof SubTask) {

            return TypesTasks.SUBTASK;
        }

        return TypesTasks.TASK;
    }

    private static String getEpicIdOfSubtask(Task task) {
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getIdEpic());
        } else {
            return "";
        }
    }

    private static String toString(Task task) {
        String[] fieldsOfTask = {
                Integer.toString(task.getTaskId()),
                getTypeOfTask(task).toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                getEpicIdOfSubtask(task),
                task.getStartTime().toString(),
                task.getEndTime().toString(),
                Long.toString(task.getDuration()),
        };

        return String.join(",", fieldsOfTask);
    }

    private static Task fromString(String value) {
        String[] fieldsOfTask = value.split(",");
        Instant startTime = Instant.parse( fieldsOfTask[6]);
        Instant endTime = Instant.parse( fieldsOfTask[7]);
        long duration = Long.parseLong(fieldsOfTask[8]);

        if (fieldsOfTask[1].equals("EPIC")) {
            Epic epic = new Epic(fieldsOfTask[2], fieldsOfTask[4], startTime, duration);
            epic.setTaskId(Integer.parseInt(fieldsOfTask[0]));
            epic.setStatus(Status.valueOf(fieldsOfTask[3].toUpperCase()));
            epic.setEndTime(endTime);

            return epic;
        } else if (fieldsOfTask[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(fieldsOfTask[2], fieldsOfTask[4], Integer.parseInt(fieldsOfTask[5]), startTime, duration);
            subTask.setTaskId(Integer.parseInt(fieldsOfTask[0]));

            return subTask;
        } else {

            Task task = new Task(fieldsOfTask[2], fieldsOfTask[4],  startTime, duration);
            task.setTaskId(Integer.parseInt(fieldsOfTask[0]));
            task.setTaskId(Integer.parseInt(fieldsOfTask[0]));

            return task;
        }
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();

        return newTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();

        return newEpic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask newSubtask = super.createSubTask(subTask);
        save();

        return newSubtask;
    }

    @Override
    public void deleteTask(int idTask) {
        super.deleteTask(idTask);
        save();
    }

    @Override
    public void deleteEpic(int idEpic) {
        super.deleteEpic(idEpic);
        save();
    }

    @Override
    public void deleteSubTask(int idSubTask) {
        super.deleteSubTask(idSubTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Task getTask(int idTask) {
        Task task = super.getTask(idTask);
        save();

        return task;
    }

    @Override
    public Epic getEpic(int idEpic) {
        Epic epic = super.getEpic(idEpic);
        save();

        return epic;
    }

    @Override
    public SubTask getSubTask(int idSubTask) {
        SubTask subTask = super.getSubTask(idSubTask);
        save();

        return subTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();

        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        save();

        return updatedEpic;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        SubTask updatedSubTask = super.updateSubTask(subTask);
        save();

        return updatedSubTask;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = super.getAllTasks();

        return allTasks;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> allSubTasks = super.getAllSubTasks();

        return allSubTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = super.getAllEpics();

        return allEpics;
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        ArrayList<SubTask> arraySubTasks =super.getSubTasksOfEpic(epic);

        return arraySubTasks;
    }

    @Override
    public List<Task> getHistory(){
        return super.getHistory();
    }
}
