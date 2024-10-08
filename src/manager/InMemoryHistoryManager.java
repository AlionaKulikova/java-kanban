package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private class HandMadeLinkedList {
        private Node head;
        private Node tail;
        Map<Integer, Node> viewedTasks = new HashMap<>();

        private void linkLast(Task task) {
            Node node = new Node(null, task, null);

            if (viewedTasks.containsKey(task.getTaskId())) {
                removeNode(viewedTasks.get(task.getTaskId()));
            }

            if (head == null) {
                head = node;
                tail = node;
                node.setNext(null);
                node.setPrev(null);
            } else {
                node.setPrev(tail);
                node.setNext(null);
                tail.setNext(node);
                tail = node;
            }

            viewedTasks.put(task.getTaskId(), node);
        }

        private List<Task> getTasks() {
            List<Task> arrayOfViewedTasks = new ArrayList<>();
            Node node = head;

            while (node != null) {
                arrayOfViewedTasks.add(node.getTask());
                node = node.getNext();
            }

            return arrayOfViewedTasks;
        }

        private void removeNode(Node node) {
            if (node != null) {
                viewedTasks.remove((node.getTask().getTaskId()));
                Node prev = node.getPrev();
                Node next = node.getNext();

                if (head == node) {
                    head = node.getNext();
                }

                if (tail == node) {
                    tail = node.getPrev();
                }

                if (prev != null) {
                    prev.setNext(next);
                }

                if (next != null) {
                    next.setPrev(prev);
                }
            }
        }

        private Node getNode(int id) {
            return viewedTasks.get(id);
        }
    }

    private HandMadeLinkedList historyTasks = new HandMadeLinkedList();

    @Override
    public void addTaskToHistory(Task task) {
        historyTasks.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyTasks.removeNode(historyTasks.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks.getTasks();
    }
}
