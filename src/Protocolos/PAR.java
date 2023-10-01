package Protocolos;
import clases.*;

import static Protocolos.Protocol.*;

public class PAR {

    /*/
    public static void sender3() {

        SeqNr next_frame_to_send = new SeqNr(); // Crea un nuevo objeto SeqNr

        SeqNr ack = new SeqNr(); // Crea un nuevo objeto SeqNr
        SeqNr seq = new SeqNr(); // Crea un nuevo objeto SeqNr
        Packet packet = new Packet();// Crea un nuevo paquete
        Frame s = new Frame(FrameKind.DATA, seq, ack, packet); // Asigna el paquete al Frame

        EventType event = EventType.FRAME_ARRIVAL;
        next_frame_to_send.setValue(0);
        Packet buffer = new Packet();
        from_network_layer(buffer); // Obtén algo para enviar desde la capa de red

        while (true) {
            s.setInfo(buffer); // Copia el paquete en la trama para la transmisión
            s.setSeq(next_frame_to_send);
            to_physical_layer(s); // Envía la trama a la capa física
            s.toString(); // Imprime los datos de la trama
            start_timer(s.getSeq());
            wait_for_event(event);
            if (event == EventType.FRAME_ARRIVAL) {
                from_physical_layer(s); /* get the acknowledgement
                if (s.getAck() == next_frame_to_send) {
                    stop_timer(s.getAck()); /* turn the timer off
                    from_network_layer(buffer); /* get the next one to send
                    next_frame_to_send.setValue(next_frame_to_send.getValue()+1);; /* invert next frame to send
                }
            }
        }
    }

    public static void receiver3() {
        SeqNr frame_expected = new SeqNr();
        Frame r = new Frame(FrameKind.DATA, new SeqNr(0), new SeqNr(0), new Packet());
        Frame s = new Frame(FrameKind.DATA, new SeqNr(0), new SeqNr(0), new Packet());
        EventType event = EventType.FRAME_ARRIVAL;
        frame_expected.setValue(0);

        while (true) {
            wait_for_event(event); // Solo es posible la llegada de una trama
            if (event == EventType.FRAME_ARRIVAL){
                from_physical_layer(r); // Obtén la trama entrante
                if (r.getSeq().getValue() == 0){
                    to_network_layer(r.getInfo()); // Pasa los datos a la capa de red
                    frame_expected.setValue(frame_expected.getValue()+1);
                }
            }

            SeqNr ack = s.getAck();
            ack.setValue(1- frame_expected.getValue());
            s.setAck(ack);

            System.out.println(r); // Imprime los datos de la trama
        }
    }
    */
}
