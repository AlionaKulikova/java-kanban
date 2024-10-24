package tasks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubTasks = new ArrayList();
    private Instant endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Instant startTime, long duration) {
        super(name, description, startTime, duration);
        this.endTime = super.getEndTime();
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(int idSubTask) {
        this.idSubTasks.add(idSubTask);
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
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
                ", description=" + getDescription() + '\'' +
                ", startTime=" + getStartTime().toEpochMilli() + '\'' +
                ", endTime=" + getEndTime().toEpochMilli() + '\'' +
                ", duration=" + getDuration();

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
