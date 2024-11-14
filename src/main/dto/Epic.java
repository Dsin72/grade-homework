package dto;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Long, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Long id, String name, String description) {
        super(id, name, description);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public HashMap<Long, Subtask> getSubtasks() {
        return subtasks;
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name=" + getName() + "'" +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", stat=" + getStatus() +
                ", subtasks=" + subtasks +
                '}';
    }
}
