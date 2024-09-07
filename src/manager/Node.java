package manager;

import tasks.Task;

public class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Node prev, tasks.Task task, Node next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    public Task getTask() {
        return task;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}