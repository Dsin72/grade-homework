package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InMemoryTaskManagerTest {
    private static final String NAME = "Наименование";
    private static final String NAME_1 = "Наименование_1";
    private static final String DESCRIPTION = "Описание";
    private static final String DESCRIPTION_1 = "Описание_1";


    @InjectMocks
    InMemoryTaskManager taskManager;

    @BeforeEach
    public void initEach(){
        taskManager.removeTasks();
        taskManager.removeSubtasks();
        taskManager.removeEpics();
        taskManager.clearHistory();
    }

    @Test
    @DisplayName("Менеджер создает задачу и корректно возвращает ее по id")
    void checkCreateTask() {
        Task expectedTask = taskManager.createTask(new Task(NAME, DESCRIPTION));
        Task resultTask = taskManager.getTask(0L);

        assertEquals(expectedTask, resultTask, "Полученная задача должна совпадать с создаваемой");
    }

    @Test
    @DisplayName("Проверяем неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    void checkCreateTaskFields() {
        taskManager.createTask(new Task(NAME, DESCRIPTION));
        Task resultTask = taskManager.getTask(0L);

        assertAll(
                () -> assertEquals(NAME, resultTask.getName(),
                        "Наименование полученной задачи должно совпадать с создаваемым"),
                () -> assertEquals(DESCRIPTION, resultTask.getDescription(),
                        "Описание полученной задачи должно совпадать с создаваемым"),
                () -> assertEquals(Status.NEW, resultTask.getStatus(),
                        "Статус полученной задачи должен совпадать с создаваемым")
        );
    }

    @Test
    @DisplayName("Задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    void checkCreateTaskWithGivenId() {
        Task task0 = taskManager.createTask(new Task(0L, NAME_1, DESCRIPTION_1));
        Task task1 = taskManager.createTask(new Task(NAME, DESCRIPTION));

        assertEquals(new ArrayList<>(List.of(task0, task1)), taskManager.getTasks(),
                "Все задачи должны добавляться, вне зависимости от способа задания id");
    }

    @Test
    @DisplayName("Проверка корректности работы удаления задачи по id")
    void checkRemoveTaskById() {
        taskManager.createTask(new Task(NAME_1, DESCRIPTION_1));
        Task task = taskManager.createTask(new Task(NAME, DESCRIPTION));

        taskManager.removeTaskById(0L);

        assertEquals(new ArrayList<>(List.of(task)), taskManager.getTasks(),
                "Задача должна удалиться из списка после вызова соответствующего метода");
    }

    @Test
    @DisplayName("Проверка корректности обновления задачи")
    void checkUpdateTaskHappyPath() {
        Task initialTask = taskManager.createTask(new Task(NAME_1, DESCRIPTION_1));
        initialTask.setName(NAME);
        initialTask.setDescription(DESCRIPTION);
        initialTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(initialTask);
        Task resultTask = taskManager.getTask(0L);

        assertAll(
                () -> assertEquals(NAME, resultTask.getName(),"Название должно обновиться"),
                () -> assertEquals(DESCRIPTION, resultTask.getDescription(), "Описание должно обновиться"),
                () -> assertEquals(Status.IN_PROGRESS, resultTask.getStatus(), "Статус должен обновиться")
        );
    }

    @Test
    @DisplayName("Проверка обновления задачи при некорректном id")
    void checkUpdateTaskWhileIdIsInvalid() {
        Task initialTask = taskManager.createTask(new Task(NAME_1, DESCRIPTION_1));
        initialTask.setId(10L);
        Task resultTask = taskManager.updateTask(initialTask);

        assertNull(resultTask, "Если id неверный, то метод должен вернуть null");
    }

    @Test
    @DisplayName("Менеджер создает эпик и корректно возвращает его по id")
    void checkCreateEpic() {
        Epic expectedEpic = taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        Epic resultEpic = taskManager.getEpic(0L);

        assertEquals(expectedEpic, resultEpic, "Эпики должны совпадать");
    }

    @Test
    @DisplayName("Проверяем неизменность эпика (по всем полям) при добавлении эпика в менеджер")
    void checkCreateEpicFields() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        Epic resultEpic = taskManager.getEpic(0L);

        assertAll(
                () -> assertEquals(NAME, resultEpic.getName(),
                        "Наименование полученного эпика должно совпадать с создаваемым"),
                () -> assertEquals(DESCRIPTION, resultEpic.getDescription(),
                        "Описание полученного эпика должно совпадать с создаваемым"),
                () -> assertEquals(Status.NEW, resultEpic.getStatus(),
                        "Статус полученного эпика должен совпадать с создаваемым"),
                () -> assertEquals(new HashMap<>(), resultEpic.getSubtasks(),
                        "Список сабтасок должен быть пустым")
        );
    }

    @Test
    @DisplayName("Эпики с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    void checkCreateEpicWithGivenId() {
        Epic epic0 = taskManager.createEpic(new Epic(0L, NAME_1, DESCRIPTION_1));
        Epic epic1 = taskManager.createEpic(new Epic(NAME, DESCRIPTION));

        assertEquals(new ArrayList<>(List.of(epic0, epic1)), taskManager.getEpics(),
                "Все эпики должны добавляться, вне зависимости от способа задания id");
    }

    @Test
    @DisplayName("Проверка корректности работы удаления эпика по id")
    void checkRemoveEpicById() {
        taskManager.createEpic(new Epic(NAME_1, DESCRIPTION_1));
        Epic epic = taskManager.createEpic(new Epic(NAME, DESCRIPTION));

        taskManager.removeEpicById(0L);

        assertEquals(new ArrayList<>(List.of(epic)), taskManager.getEpics(),
                "Эпик должен удалиться из списка после вызова соответствующего метода");
    }

    @Test
    @DisplayName("Проверка корректности обновления эпика")
    void checkUpdateEpicHappyPath() {
        Epic initialEpic = taskManager.createEpic(new Epic(NAME_1, DESCRIPTION_1));
        initialEpic.setName(NAME);
        initialEpic.setDescription(DESCRIPTION);
        initialEpic.setStatus(Status.IN_PROGRESS);

        taskManager.updateEpic(initialEpic);
        Epic resultEpic = taskManager.getEpic(0L);

        assertAll(
                () -> assertEquals(NAME, resultEpic.getName(),"Название должно обновиться"),
                () -> assertEquals(DESCRIPTION, resultEpic.getDescription(),"Описание должно обновиться"),
                () -> assertEquals(Status.NEW, resultEpic.getStatus(),"Статус обновиться не должен")
        );
    }

    @Test
    @DisplayName("Проверка обновления эпика при некорректном id")
    void checkUpdateEpicWhileIdIsInvalid() {
        Epic initialEpic = taskManager.createEpic(new Epic(NAME_1, DESCRIPTION_1));
        initialEpic.setId(10L);
        Task resultEpic = taskManager.updateEpic(initialEpic);

        assertNull(resultEpic, "Если id неверный, то метод должен вернуть null");
    }

    @Test
    @DisplayName("Менеджер создает сабтаску и корректно возвращает ее по id")
    void checkCreateSubtask() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        Subtask expectedSubtask = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L));
        Subtask resultSubtask = taskManager.getSubtask(1L);

        assertEquals(expectedSubtask, resultSubtask, "Сабтаски должны совпадать");
    }

    @Test
    @DisplayName("Проверяем неизменность сабтаски (по всем полям) при добавлении сабтаски в менеджер")
    void checkCreateSubtaskFields() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        Subtask expectedSubtask = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L));
        Subtask resultSubtask = taskManager.getSubtask(
                1L);

        assertAll(
                () -> assertEquals(NAME, resultSubtask.getName(),
                        "Наименование полученной сабтаски должно совпадать с создаваемым"),
                () -> assertEquals(DESCRIPTION, resultSubtask.getDescription(),
                        "Описание полученной сабтаски должно совпадать с создаваемым"),
                () -> assertEquals(Status.NEW, resultSubtask.getStatus(),
                        "Статус полученной сабтаски должен совпадать с создаваемым"),
                () -> assertEquals(expectedSubtask, taskManager.getEpic(0L).getSubtasks().get(1L),
                        "Сабтаска должна добавиться в эпик"),
                () -> assertEquals(Status.NEW, taskManager.getEpic(0L).getStatus(),
                        "Статус эпика не должен измениться")
        );
    }

    @Test
    @DisplayName("Сабтаски с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    void checkCreateSubtaskWithGivenId() {
        taskManager.createEpic(new Epic(0L, NAME_1, DESCRIPTION_1));
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));

        Subtask subtask0 =taskManager.createSubtask(new Subtask(2L, NAME_1, DESCRIPTION_1, 1L));
        Subtask subtask1 = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 1L));

        assertEquals(new ArrayList<>(List.of(subtask0, subtask1)), taskManager.getSubtasks(),
                "Все сабтаски должны добавляться, вне зависимости от способа задания id");
    }

    @Test
    @DisplayName("Проверка корректности работы удаления сабтаски по id")
    void checkRemoveSubtaskById() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        taskManager.createSubtask(new Subtask(NAME_1, DESCRIPTION_1, 0L));
        Subtask subtask = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L));

        taskManager.removeSubtaskById(1L);

        assertEquals(new ArrayList<>(List.of(subtask)), taskManager.getSubtasks(),
                "Сабтаска должна удалиться из списка после вызова соответствующего метода");
    }

    @Test
    @DisplayName("Проверка корректности обновления сабтаски")
    void checkUpdateSubtaskHappyPath() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));

        Subtask initialSubtask = taskManager.createSubtask(new Subtask(NAME_1, DESCRIPTION_1, 0L));
        initialSubtask.setName(NAME);
        initialSubtask.setDescription(DESCRIPTION);
        initialSubtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(initialSubtask);
        Subtask resultSubtask = taskManager.getSubtask(1L);

        assertAll(
                () -> assertEquals(NAME, resultSubtask.getName(), "Название должно обновиться"),
                () -> assertEquals(DESCRIPTION, resultSubtask.getDescription(), "Описание должно обновиться"),
                () -> assertEquals(Status.IN_PROGRESS, resultSubtask.getStatus(), "Статус должен обновиться"),
                () -> assertEquals(NAME, taskManager.getEpic(0L).getSubtasks().get(1L).getName(),
                        "В эпике название сабтаски тоже должно актуализироваться"),
                () -> assertEquals(DESCRIPTION, taskManager.getEpic(0L).getSubtasks().get(1L).getDescription(),
                        "В эпике описание сабтаски тоже должно актуализироваться"),
                () -> assertEquals(Status.IN_PROGRESS, taskManager.getEpic(0L).getSubtasks().get(1L).getStatus(),
                        "В эпике статус сабтаски тоже должно актуализироваться")
        );
    }

    @Test
    @DisplayName("Проверка обновления сабтаски при некорректном id")
    void checkUpdateSubtaskWhileIdIsInvalid() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        Subtask initialSubtask = taskManager.createSubtask(new Subtask(NAME_1, DESCRIPTION_1, 0L));
        initialSubtask.setId(10L);
        Task resultSubtask = taskManager.updateSubtask(initialSubtask);

        assertNull(resultSubtask, "Если id неверный, то метод должен вернуть null");
    }

    @Test
    @DisplayName("При изменении статуса сабтаски должен измениться статус эпика")
    void checkEpicStatusAfterSubtaskStatusChanged() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        assertEquals(Status.NEW, taskManager.getEpic(0L).getStatus(),
                "При создании статус должен быть NEW");

        Subtask subtask1 = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L));
        subtask1.setStatus(Status.IN_PROGRESS);
        Subtask subtask2 = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L));
        taskManager.updateSubtask(subtask1);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(0L).getStatus(),
                "Если хотя бы одна саб-таска перешла в IN_PROGRESS, то статус эпика должен стать аналогичным");

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(Status.DONE, taskManager.getEpic(0L).getStatus(),
                "Если все саб-таски перешли в DONE, то статус эпика должен стать аналогичным");
    }

    @Test
    @DisplayName("Проверяем получение списка задач")
    void checkGetTasks() {
        List<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(taskManager.createTask(new Task(NAME, DESCRIPTION)));
        expectedTasks.add(taskManager.createTask(new Task(NAME, DESCRIPTION)));

        assertEquals(expectedTasks, taskManager.getTasks(),
                "Список задач должен соответствовать созданным задачам");
    }

    @Test
    @DisplayName("Проверяем получение списка эпиков")
    void checkGetEpics() {
        List<Epic> expectedEpics = new ArrayList<>();
        expectedEpics.add(taskManager.createEpic(new Epic(NAME, DESCRIPTION)));
        expectedEpics.add(taskManager.createEpic(new Epic(NAME, DESCRIPTION)));

        assertEquals(expectedEpics, taskManager.getEpics(),
                "Список эпиком должен соответствовать созданным эпикам");
    }

    @Test
    @DisplayName("Проверяем получение списка эпиков")
    void checkGetSubtasks() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        List<Subtask> expectedSubtasks = new ArrayList<>();
        expectedSubtasks.add(taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L)));
        expectedSubtasks.add(taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L)));

        assertEquals(expectedSubtasks, taskManager.getSubtasks(),
                "Список сабтасок должен соответствовать созданным сабтаскам");
    }

    @Test
    @DisplayName("При получении задачи происходит добавление ее в историю")
    void checkCallAddTaskToHistoryWhileGetIt() {
        Task task = taskManager.createTask(new Task(NAME, DESCRIPTION));
        taskManager.getTask(0L);
        taskManager.getTask(0L);

        assertEquals(new ArrayList<>(List.of(task, task)), taskManager.getHistory(),
                "История должна отражать порядок обращения к задачам");
    }

    @Test
    @DisplayName("При получении эпика происходит добавление его в историю")
    void checkCallAddEpicToHistoryWhileGetIt() {
        Epic epic = taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        taskManager.getEpic(0L);
        taskManager.getEpic(0L);

        assertEquals(new ArrayList<>(List.of(epic, epic)), taskManager.getHistory(),
                "История должна отражать порядок обращения к эпикам");
    }

    @Test
    @DisplayName("При получении сабтаски происходит добавление ее в историю")
    void checkCallAddSubtaskToHistoryWhileGetIt() {
        taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        Subtask subtask = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 0L));
        taskManager.getSubtask(1L);
        taskManager.getSubtask(1L);

        assertEquals(new ArrayList<>(List.of(subtask, subtask)), taskManager.getHistory(),
                "История должна отражать порядок обращения к сабтаскам");
    }

    @Test
    @DisplayName("В истории живут всего 10 задач")
    void checkHistoryHoldOnlyTenTasks() {
        Task task = taskManager.createTask(new Task(NAME, DESCRIPTION));
        Epic epic = taskManager.createEpic(new Epic(NAME, DESCRIPTION));
        Subtask subtask = taskManager.createSubtask(new Subtask(NAME, DESCRIPTION, 1L));

        for (int i = 0; i < 4; i++) {
            taskManager.getTask(0L);
            taskManager.getEpic(1L);
            taskManager.getSubtask(2L);
        }
        assertAll(
                () -> assertEquals(10, taskManager.getHistory().size(),
                        "История должна отражать порядок обращения к задачам"),
                () -> assertEquals(
                        new ArrayList<>(List.of(subtask, task, epic, subtask, task, epic, subtask, task, epic, subtask)),
                        taskManager.getHistory(), "История должна отражать порядок обращения к сабтаскам")
        );
    }

    @Test
    @DisplayName("Задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных")
    void checkHistorySaveOldTaskVersion() {
        Task task = taskManager.createTask(new Task(NAME, DESCRIPTION));
        taskManager.getTask(0L);
        task.setName(NAME_1);
        taskManager.updateTask(task);

        assertEquals(NAME, taskManager.getHistory().get(0).getName(),
                "Наименование в истории не должно измениться");
    }

    @Test
    @DisplayName("Объект Subtask нельзя сделать своим же эпиком")
    void checkEpicCanNotBeAddedAsASubtask(){
        assertThrows(RuntimeException.class,
                () -> taskManager.createSubtask(new Subtask(0L, NAME, DESCRIPTION, 0L)));
    }
}