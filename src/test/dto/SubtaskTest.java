package dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    @DisplayName("Проверяем корректность работы метода equals")
    public void tasksWithEqualIdShouldBeEqual() {
        Subtask task1 = new Subtask("Первая", "таска", 1L);
        Subtask task2 = new Subtask("Первая", "таска", 1L);
        task1.setId(2L);
        task2.setId(2L);

        assertEquals(task1, task2, "Наследники класса Task должны быть равны друг другу, если равен их id!");
    }
}