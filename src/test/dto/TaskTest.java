package dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    @DisplayName("Проверяем корректность работы метода equals")
    public void tasksWithEqualIdShouldBeEqual() {
        Task task1 = new Task("Первая", "таска");
        Task task2 = new Task("Первая", "таска");
        task1.setId(1L);
        task2.setId(1L);

        assertEquals(task1, task2, "Экземпляры класса Task должны быть равны друг другу, если равен их id!");
    }

}