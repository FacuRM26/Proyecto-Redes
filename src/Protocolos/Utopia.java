package Protocolos;
import clases.*;

import static Protocolos.Protocol.*;

public class Utopia {

    public static void sender1() {
        SeqNr seq = new SeqNr(); // Crea un nuevo objeto SeqNr
        SeqNr ack = new SeqNr(); // Crea un nuevo objeto SeqNr
        Packet packet = new Packet();// Crea un nuevo paquete
        Frame s = new Frame(FrameKind.DATA, seq, ack, packet); // Asigna el paquete al Frame

        Packet buffer = new Packet();

        while (true) {
            from_network_layer(buffer); // Obtén algo para enviar desde la capa de red
            s.setInfo(buffer); // Copia el paquete en la trama para la transmisión
            to_physical_layer(s); // Envía la trama a la capa física
            s.toString(); // Imprime los datos de la trama
        }
    }

    public static void receiver1() {
        Frame r = new Frame(FrameKind.DATA, new SeqNr(0), new SeqNr(0), new Packet());
        EventType event = EventType.FRAME_ARRIVAL;

        while (true) {
            wait_for_event(event); // Solo es posible la llegada de una trama
            from_physical_layer(r); // Obtén la trama entrante
            to_network_layer(r.getInfo()); // Pasa los datos a la capa de red
            System.out.println(r); // Imprime los datos de la trama
        }
    }
}

