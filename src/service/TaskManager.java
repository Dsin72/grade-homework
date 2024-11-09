package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private long nextId = 1L;
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Subtask> subtasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();

    private long getNextId() {
        return nextId++;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(long id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Task removeTaskById(long id) {
        return tasks.remove(id);
    }

    public Subtask removeSubtaskById(long id) {
        Subtask subtask = getSubtaskById(id);
        subtasks.remove(id);

        Epic epic = getEpicById(subtask.getEpicId());
        getSubtasksByEpicId(epic.getId()).remove(id);
        epic.setStatus(calculateEpicStatus(epic));

        return subtask;
    }

    public Epic removeEpicById(long id) {
        for (long subtaskId : getSubtasksByEpicId(id).keySet()) {
            subtasks.remove(subtaskId);
        }

        return epics.remove(id);
    }

    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);

        epic.setStatus(calculateEpicStatus(epic));
        return subtask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return null;
        }
        return tasks.replace(task.getId(), task);
    }

    public Subtask updateSubtask(Subtask subtask) {
        long subtaskId = subtask.getId();

        if (!subtasks.containsKey(subtaskId)) {
            return null;
        }

        Epic epic = getEpicById(subtask.getEpicId());
        getSubtasksByEpicId(epic.getId()).replace(subtaskId, subtask);
        epic.setStatus(calculateEpicStatus(epic));

        return subtasks.replace(subtask.getId(), subtask);
    }

    public Epic updateEpic(Epic epic) {
        epic.setStatus(calculateEpicStatus(epic));
        epics.put(epic.getId(), epic);
        return epic;
    }

    private Status calculateEpicStatus(Epic epic) {
        HashMap<Long, Subtask> subTasks = epic.getSubtasks();
        if (subTasks.isEmpty()) {
            return Status.NEW;
        }

        HashMap<Status, Integer> subtasksStatuses = new HashMap<>();
        subtasksStatuses.put(Status.DONE, 0);
        subtasksStatuses.put(Status.IN_PROGRESS, 0);
        subtasksStatuses.put(Status.NEW, 0);

        for (var pair : subTasks.entrySet()) {
            Status status = pair.getValue().getStatus();
            subtasksStatuses.put(status, subtasksStatuses.get(status) + 1);
        }

        if (subtasksStatuses.get(Status.NEW) == subTasks.size()) {
            return Status.NEW;
        } else if (subtasksStatuses.get(Status.DONE) == subTasks.size()) {
            return Status.DONE;
        }

        return Status.IN_PROGRESS;
    }

    public HashMap<Long, Subtask> getSubtasksByEpicId(long id) {
        return epics.get(id).getSubtasks();
    }

}
