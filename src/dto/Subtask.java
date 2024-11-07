package dto;

public class Subtask extends Task {
    private final long epicId;

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + "'" +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                "}";
    }
}
