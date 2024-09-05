package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubTasks = new ArrayList();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(int idSubTask) {
        this.idSubTasks.add(idSubTask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubTasks, epic.idSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTasks);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "taskId='" + getTaskId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", name=" + getName() + '\'' +
                ", description=" + getDescription() + '\'';

        if (idSubTasks != null) {
            result = result + ", idSubTasks.size=" + idSubTasks.size();
        } else {
            result = result + ", idSubTasks=null";
        }

        return result +
                ", idSubTasks=" + idSubTasks +
                '}';
    }
}
