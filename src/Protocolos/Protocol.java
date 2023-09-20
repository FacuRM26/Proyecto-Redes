package Protocolos;

import clases.*;

public class Protocol {
    public static final int MAX_PKT = 1024; // Determina el tamaño del paquete en bytes
    public static final int MAX_SEQ = 255; // Define el valor máximo para la secuencia

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
// Modifica la firma de from_network_layer para aceptar un paquete como argumento
    public static void from_network_layer(Packet buffer) {
        // Aquí implementa la lógica para obtener un paquete de la capa de red
        // y almacenar los datos en el objeto buffer.
        // Puedes crear un paquete con datos ficticios o recuperarlos de alguna fuente de datos real.

        // Ejemplo: Crear un objeto Packet con datos ficticios
        byte[] packetData = {0x01, 0x02, 0x03}; // Datos ficticios del paquete
        buffer.setData(packetData);
    }
    /* Deliver information from an inbound frame to the network layer. */
    public static void to_network_layer(Packet packet) {
        // Aquí implementa la lógica para enviar un paquete a la capa de red
        // Puedes imprimir los datos del paquete para verificar que sean correctos

        // Ejemplo: Imprimir los datos del paquete
        System.out.println("Packet data: " + packet.getData());
    }

    /* Go get an inbound frame from the physical layer and copy it to r. */
    public static void from_physical_layer(Frame r) {
        // Aquí implementa la lógica para obtener una trama entrante de la capa física
        // y copiar sus datos en el objeto r.

        // Ejemplo: Crear una trama ficticia con datos ficticios
        FrameKind frameKind = FrameKind.DATA;
        SeqNr seq = new SeqNr(0);
        SeqNr ack = new SeqNr(0);
        Packet packet = new Packet();
        r.setKind(frameKind);
        r.setSeq(seq);
        r.setAck(ack);
        r.setInfo(packet);
    }
    /* Pass the frame to the physical layer for transmission. */
    public static void to_physical_layer(Frame s) {
        // Aquí implementa la lógica para enviar una trama a la capa física
        // Puedes imprimir los datos de la trama para verificar que sean correctos

        // Ejemplo: Imprimir los datos de la trama
        System.out.println("FrameKind: " + s.getKind());
        System.out.println("Seq: " + s.getSeq());
        System.out.println("Ack: " + s.getAck());
        System.out.println("Info: " + s.getInfo());
    }


}
