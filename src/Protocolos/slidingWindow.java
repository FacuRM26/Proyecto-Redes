package Protocolos;
import clases.*;

import java.util.Random;

import static Protocolos.Protocol.*;

public class slidingWindow {

    public static Protocol protocol = new Protocol();
    public static Maquina sender4 = new Maquina("Sender4");
    public static Maquina receiver4 = new Maquina("Receiver4");
    public static Interfaz interfaz = new Interfaz();
    public static Event event;
public static void protocol4(){
    double error= Interfaz.errorProb;
    System.out.println("Error: "+error);
    sender4.setInterfaz(slidingWindow.interfaz); //se setea para configurar los mensajes de la interfaz
    protocol.addObserver(sender4);
    int next_frame_to_send = 0; /* 0 or 1 only */
    int frame_expected = 0; /* 0 or 1 only */
    Frame r = new Frame("", 0, 0, new Packet()); /* scratch variables */
    Frame s = new Frame("", 0, 0, new Packet());/* scratch variables */

    Packet buffer = new Packet(); // buffer para un paquete de salida
    sender4.from_network_layer(buffer); /* fetch a packet from the network layer */
    s.setInfo(buffer);  /* prepare to send the initial frame */
    s.setSeq(next_frame_to_send); /* insert sequence number into frame */
    s.setAck(1-frame_expected); /* piggybacked ack */
    sender4.to_physical_layer_manual(s, protocol); /* transmit the frame */
    sender4.start_timer(s.getSeq()); /* start the timer running */
    while (true){
        sender4.wait_for_event(event); /* frame_arrival, cksum_err, or timeout */
        if(event.getType() == "frame_arrival"){ /* a frame has arrived undamaged */
            sender4.from_physical_layer(r, protocol); /* go get it */
            if(r.getSeq() == frame_expected) { /* handle inbound frame stream */
                sender4.from_network_layer(r.getInfo());/* pass packet to network layer */
            }
                frame_expected = sender4.incFrame(frame_expected); /* invert 0/1 */
            if(r.getAck()==next_frame_to_send){ /* handle outbound frame stream */
                sender4.stop_timer(r.getAck());/* turn the timer off */
                sender4.from_network_layer(buffer); /* fetch new pkt from network layer */
                next_frame_to_send = sender4.incFrame(next_frame_to_send); /* invert sender’s sequence number */
            }
        }

        s.setInfo(buffer); /* construct outbound frame */
        s.setSeq(next_frame_to_send); /* insert sequence number into it */
        s.setAck(1-frame_expected);/* seq number of last received frame */
        sender4.to_physical_layer_manual(s, protocol); /* transmit a frame */
        sender4.start_timer(s.getSeq()); /* start the timer running */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public static void receiver4(){
    receiver4.setInterfaz(slidingWindow.interfaz); //se setea para configurar los mensajes de la interfaz
    protocol.addObserver(receiver4);
    int frame_expected = 0; /* next frame_expected on the inbound stream */
    int ack_expected = 0; /* ack_expected of last inbound frame */
    int next_frame_to_send = 0; /* seq number of next frame to send */
    Frame r = new Frame("", 0, 0, new Packet()); /* scratch variables */
    Frame s = new Frame("", 0, 0, new Packet());/* scratch variables */
    while (true){
        receiver4.wait_for_event(); /* frame_arrival */
        receiver4.from_physical_layer(r, protocol); /* go get the inbound frame */
        if(r.getSeq() == frame_expected){
            receiver4.to_network_layer(r.getInfo()); /* pass packet to network layer */
            frame_expected = receiver4.incFrame(frame_expected); /* invert sequence number expected next */
        }
        if(r.getAck() == ack_expected){ /* frames may be accepted in any order */
            receiver4.stop_timer(r.getAck()); /* turn off the timer */
            next_frame_to_send= sender4.incFrame(next_frame_to_send); /* ack expected has been acked */
        }
        s.setAck(1-frame_expected); /* tell sender what we want */
        receiver4.to_physical_layer_manual(s, protocol); /* send acknowledgement */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

    public static void setInterfaz(Interfaz interfaz, double error) {
        slidingWindow.interfaz = interfaz;
        sender4.setError(error);
        receiver4.setError(error);
        event = new Event("");
        sender4.setMAX_SEQ(1);
        receiver4.setMAX_SEQ(1);
    }
}
