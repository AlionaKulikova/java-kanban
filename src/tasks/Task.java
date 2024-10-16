package tasks;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import status.Status;

public class Task {
    private int taskId;
    private Status status = Status.NEW;
    private String name;
    private String description;
    private Instant startTime;
    private long duration;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, Instant startTime, long duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getEndTime() {
        long SECONDS_IN_MINUTE = 60;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && status == task.status && Objects.equals(name, task.name)
                && Objects.equals(description, task.description) && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, status, name, description, startTime, duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", status='" + status + '\'' +
                ", name=" + name + '\'' +
                ", description=" + description + '\'' +
                ", startTime=" + startTime.toEpochMilli() + '\'' +
                ", endTime=" + getEndTime().toEpochMilli() + '\'' +
                ", duration=" + duration +
                '}';
    }
}
