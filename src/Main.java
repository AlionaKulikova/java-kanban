import http.Server;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getInMemoryTaskManager(Managers.getDefaultHistory());
        Server serverHttp = new Server(taskManager);

        serverHttp.start();
    }
}