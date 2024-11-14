package dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    @DisplayName("Проверяем корректность работы метода equals")
    public void tasksWithEqualIdShouldBeEqual() {
        Epic epic1 = new Epic("Эпик", "первый");
        Epic epic2 = new Epic("Эпик", "второй");
        epic1.setId(1L);
        epic2.setId(1L);

        assertEquals(epic1, epic2, "Наследники класса Task должны быть равны друг другу, если равен их id!");
    }
}