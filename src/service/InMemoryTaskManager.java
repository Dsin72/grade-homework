package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private long nextId = 0L;
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Subtask> subtasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();

    private long getNextId() {
        return nextId++;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void clearHistory() {
        historyManager.clear();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTask(long id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(long id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(long id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task removeTaskById(long id) {
        return tasks.remove(id);
    }

    @Override
    public Subtask removeSubtaskById(long id) {
        Subtask subtask = getSubtask(id);
        subtasks.remove(id);

        Epic epic = getEpic(subtask.getEpicId());
        getSubtasksByEpicId(epic.getId()).remove(id);
        epic.setStatus(calculateEpicStatus(epic));

        return subtask;
    }

    @Override
    public Epic removeEpicById(long id) {
        for (long subtaskId : getSubtasksByEpicId(id).keySet()) {
            subtasks.remove(subtaskId);
        }

        return epics.remove(id);
    }

    @Override
    public Task createTask(Task task) {
        if (task.getId() == null) {
            long id = getNextId();
            while (tasks.containsKey(id)) {
                id = getNextId();
            }
            task.setId(id);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            long id = getNextId();
            while (subtasks.containsKey(id)) {
                id = getNextId();
            }
            subtask.setId(id);
        }
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);

        epic.setStatus(calculateEpicStatus(epic));
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic.getId() == null) {
            long id = getNextId();
            while (epics.containsKey(id)) {
                id = getNextId();
            }
            epic.setId(id);
        }
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return null;
        }
        return tasks.replace(task.getId(), task);
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        long subtaskId = subtask.getId();

        if (!subtasks.containsKey(subtaskId)) {
            return null;
        }

        Epic epic = getEpic(subtask.getEpicId());
        getSubtasksByEpicId(epic.getId()).replace(subtaskId, subtask);
        epic.setStatus(calculateEpicStatus(epic));

        return subtasks.replace(subtask.getId(), subtask);
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return null;
        }
        epic.setStatus(calculateEpicStatus(epic));
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public HashMap<Long, Subtask> getSubtasksByEpicId(long id) {
        return epics.get(id).getSubtasks();
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
}
