package clases;

public class Event {
    private EventType type; // Enumeración que representa el tipo de evento

    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
