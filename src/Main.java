import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Создаем задачки:");
        Task haveFun = new Task("Радоваться жизни", "Открыть калитку балдура");
        taskManager.createTask(haveFun);
        Task completeFun = new Task( "Завершить дорогу приключений", "Прострелить колено");
        taskManager.createTask(completeFun);
        System.out.println(taskManager.getTasks());


        Epic completeQuest = new Epic("Пройти квест", "Всех хорошенько победить");
        long epicId = taskManager.createEpic(completeQuest).getId();

        Subtask increaseStealth = new Subtask("Повысить навык скрытности", "Упорно тренируем гусиный шаг", epicId);
        Subtask findArmor = new Subtask("Разжиться понтовыми латами", "Своровать броню у кузнеца", epicId);
        taskManager.createSubtask(increaseStealth);
        taskManager.createSubtask(findArmor);
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());

        System.out.println("\nОбновляем задачки:");
        haveFun.setStatus(Status.DONE);
        taskManager.updateTask(haveFun);
        completeFun.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(completeFun);
        System.out.println(taskManager.getTasks());

        increaseStealth.setStatus(Status.DONE);
        taskManager.updateSubtask(increaseStealth);
        findArmor.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(findArmor);
        System.out.println(taskManager.getSubTasks());

        completeQuest.setStatus(Status.DONE);
        taskManager.updateEpic(completeQuest);
        System.out.println(taskManager.getEpics());

        System.out.println("\nУдаляем задачки:");
        taskManager.removeTaskById(1);
        System.out.println(taskManager.getTasks());
        taskManager.removeSubtaskById(4);
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());

        taskManager.removeEpicById(3);
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());
    }
}
