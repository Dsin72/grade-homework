package service;

import dto.Epic;
import dto.Subtask;
import dto.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    void clearHistory();

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Task getTask(long id);

    Subtask getSubtask(long id);

    Epic getEpic(long id);

    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    Task removeTaskById(long id);

    Subtask removeSubtaskById(long id);

    Epic removeEpicById(long id);

    Task createTask(Task task);

    Subtask createSubtask(Subtask subtask);

    Epic createEpic(Epic epic);

    Task updateTask(Task task);

    Subtask updateSubtask(Subtask subtask);

    Epic updateEpic(Epic epic);

    HashMap<Long, Subtask> getSubtasksByEpicId(long id);
}
