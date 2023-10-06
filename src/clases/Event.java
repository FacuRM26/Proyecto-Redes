package clases;

public class Event {
    private String type; // Enumeración que representa el tipo de evento

    public Event(String type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
