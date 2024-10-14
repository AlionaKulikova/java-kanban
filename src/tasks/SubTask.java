package tasks;

import java.time.Instant;
import java.util.Objects;

public class SubTask extends Task {
    private final int idEpic;

    public SubTask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String description, int idEpic, Instant startTime, long duration) {
        super(name, description, startTime, duration);
        this.idEpic = idEpic;
    }


    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return idEpic == subTask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "taskId='" + getTaskId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", name=" + getName() + '\'' +
                ", description=" + getDescription() +
                ", idEpic=" + getIdEpic() +
                ", startTime=" + getStartTime().toEpochMilli() + '\'' +
                ", endTime=" + getEndTime().toEpochMilli() + '\'' +
                ", duration=" + getDuration() +
                '}';
    }
}
