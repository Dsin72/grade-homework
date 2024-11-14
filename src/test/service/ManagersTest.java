package service;

import dto.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ManagersTest {
    @Test
    @DisplayName("Проверяем тип возрашаемого getDefault объекта")
    void getDefaultShouldInitializeInMemoryTaskManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(),
                "getDefault должен возвращать инстанс класса InMemoryTaskManager");
    }

    @Test
    @DisplayName("Проверяем, что утилитарный класс возвращает готовый к работе экземпляр таск менеджера")
    void getDefaultShouldInitializeReadyToWorkTaskManager() {
        TaskManager manager = Managers.getDefault();
        Task expectedTask = new Task("Раз", "Два");

        assertAll(
                () -> assertDoesNotThrow(() -> manager.createTask(expectedTask),
                        "Полученный менеджер должен иметь возможность создавать таски"),
                () -> assertEquals(expectedTask, manager.getTask(0L),
                        "Полученный менеджер должен корректно возвращать таски")
        );
    }

    @Test
    @DisplayName("Проверяем тип возрашаемого getDefaultHistory объекта")
    void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {
        assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory(),
                "getDefaultHistory должен возвращать инстанс класса InMemoryHistoryManager");
    }

    @Test
    @DisplayName("Проверяем, что утилитарный класс возвращает готовый к работе экземпляр history менеджера")
    void getDefaultShouldInitializeReadyToWorkHistoryManager() {
        HistoryManager manager = Managers.getDefaultHistory();

        Task expectedTask = new Task("Раз", "Два");

        assertAll(
                () -> assertDoesNotThrow(() -> manager.add(expectedTask),
                        "Полученный менеджер должен иметь возможность добавлять таски"),
                () -> assertEquals(expectedTask, manager.getHistory().get(0),
                        "Полученный менеджер должен корректно возвращать таски")
        );
    }
}