package service;

import dto.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int VOLUME_OF_HISTORY_STORAGE = 10;
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (historyList.size() == VOLUME_OF_HISTORY_STORAGE) {
            historyList.removeFirst();
        }
        try {
            historyList.add(task.clone());
        } catch (CloneNotSupportedException e) {
            System.out.println("Что-то пошло не так при копировании объекта. Ошибка: " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        historyList.clear();
    }
}
