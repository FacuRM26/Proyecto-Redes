package Protocolos;

import clases.*;

import java.util.Random;

public class Maquina {

    public static final int MAX_PKT = 1024; // Determina el tamaño del paquete en bytes
    public static final int MAX_SEQ = 255; // Define el valor máximo para la secuencia

    public static int actFrame = 0;

    // Método que espera un evento y devuelve el tipo de evento
    public Event wait_for_event() {
        // Aquí implementa la lógica para esperar un evento
        // Puedes definir y asignar el tipo de evento según tu lógica
        EventType eventType = EventType.FRAME_ARRIVAL; // Ejemplo

        // Crea un objeto Event con el tipo de evento y devuélvelo
        return new Event(eventType);
    }

    /* Wait for an event to happen; return its type in event. */
    public static void wait_for_event(EventType event) {
        // Aquí implementa la lógica para esperar un evento
        // Puedes definir y asignar el tipo de evento según tu lógica
        event = EventType.FRAME_ARRIVAL; // Ejemplo
    }

    // Modifica el paquete para simular la entrada de un buffer
    public static void from_network_layer(Packet buffer) {
        // Se crea el paquete ficticio utilizando un randomize de cuatro números
        Random random = new Random();
        String data = "";
        for (int i = 0; i<=4; i++){
            int randNumb = random.nextInt(10);
            data = data + randNumb;
        }
        buffer.setData(data);
    }

    // Muestra la información en la capa de red
    public static void to_network_layer(Packet packet) {
        System.out.println("Packet data: " + packet.getData());
    }

    /* Go get an inbound frame from the physical layer and copy it to r. */
    public static void from_physical_layer(Frame r) {

    }

    /* Pass the frame to the physical layer for transmission. */
    public static void to_physical_layer(Frame s) {

        // Se usa un randomize para elegir el tipo de FRAME
        Random random = new Random();
        int randNumb = random.nextInt(4);

        // data (datos), ack (confirmacion), nak (no confirmacion)
        String frameKind = "";
        switch (randNumb){
            case (1):
                frameKind = "DATA";
                break;
            case (2):
                frameKind = "ACK";
                break;
            case (3):
                frameKind = "NAK";
                break;
        }

        int seq = actFrame;
        int ack = 0;
        inc();

        s.setKind(frameKind);
        s.setSeq(seq);
        s.setAck(ack);

        // Ejemplo: Imprimir los datos de la trama
        System.out.println("FrameKind: " + s.getKind());
        System.out.println("Seq: " + s.getSeq());
        System.out.println("Ack: " + s.getAck());
        System.out.println("Info: " + s.getInfo());
    }

    public static void start_timer(SeqNr k) {
        //pendiente
    }

    public static void stop_timer(SeqNr k) {
        //pendiente
    }

    // Define la macro inc de forma similar a como se hace en C
    private static void inc() {
        if (actFrame < MAX_SEQ) {
            actFrame = actFrame + 1;
        } else {
            actFrame = 0;
        }
    }

}
