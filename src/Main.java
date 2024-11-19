import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Создаем задачки:");
        Task haveFun = new Task("Радоваться жизни", "Открыть калитку балдура");
        manager.createTask(haveFun);
        Task completeFun = new Task("Завершить дорогу приключений", "Прострелить колено");
        manager.createTask(completeFun);
        System.out.println(manager.getTasks());

        Epic completeQuest = new Epic("Пройти квест", "Всех хорошенько победить");
        long epicId = manager.createEpic(completeQuest).getId();

        Subtask increaseStealth = new Subtask("Повысить навык скрытности", "Упорно тренируем гусиный шаг", epicId);
        Subtask findArmor = new Subtask("Разжиться понтовыми латами", "Своровать броню у кузнеца", epicId);
        manager.createSubtask(increaseStealth);
        manager.createSubtask(findArmor);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

        System.out.println("\nОбновляем задачки:");
        haveFun.setStatus(Status.DONE);
        manager.updateTask(haveFun);
        completeFun.setStatus(Status.IN_PROGRESS);
        manager.updateTask(completeFun);
        System.out.println(manager.getTasks());

        increaseStealth.setStatus(Status.DONE);
        manager.updateSubtask(increaseStealth);
        findArmor.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(findArmor);
        System.out.println(manager.getSubtasks());

        completeQuest.setStatus(Status.DONE);
        manager.updateEpic(completeQuest);
        System.out.println(manager.getEpics());

        System.out.println("\nУдаляем задачки:");
        manager.removeTaskById(1);
        System.out.println(manager.getTasks());
        manager.removeSubtaskById(4);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

        manager.removeEpicById(3);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

        System.out.println("\nСмотрим историю:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
