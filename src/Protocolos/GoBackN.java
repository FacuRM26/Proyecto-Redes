package Protocolos;

import java.util.ArrayList;
import clases.*;
import Protocolos.*;
import java.util.Random;

/**
 *
 * @author Alina
 */
public class GoBackN {
    public static Protocol protocol = new Protocol();
    public static Maquina receiver5 = new Maquina("Receiver5");
    public static Interfaz interfaz;
    public static Event event;



    //Revisa la secuencia de los numeros circular
    public static boolean between(int valorA, int valorB, int valorC) {
        if (((valorA <= valorB) && (valorB < valorC)) ||
                ((valorC < valorA) && (valorA <= valorB)) ||
                ((valorB < valorC) && (valorC < valorA))) {
            return true;
        } else {
            return false;
        }
    }

    // Construir y enviar un marco de datos.
    public static void send_data1(int frame_nr, int frame_expected, ArrayList<Packet> buffer) {
        Frame s = new Frame();
        s.setInfo(buffer.get(frame_nr)); // Insertar el paquete en el marco
        s.setSeq(frame_nr); // Insertar el número de secuencia en el marco
        int ack = (frame_expected + receiver5.getMaxSeq()) % (receiver5.getMaxSeq() + 1);
        s.setAck(ack);
        receiver5.to_physical_layer_manual(s, protocol);// Transmitir el marco
    }

    public static void receiver () {
        receiver5.setInterfaz(GoBackN.interfaz);
        protocol.addObserver(receiver5);

        int next_frame_to_send;
        int ack_expected; //Confirmacion de espera
        int frame_expected;
        Frame r = new Frame();
        ArrayList<Packet> buffer = new ArrayList<Packet>();

        for (int i = 0; i < receiver5.getMaxSeq() + 1; i++) {
            Packet packet = new Packet();
            buffer.add(packet);
        }

        int nbuffered;
        int i;
        protocol.setEnableNewtWork(true);

        ack_expected = 0;
        next_frame_to_send = 0;
        frame_expected = 0;
        nbuffered = 0;

        while (true) {
            receiver5.wait_for_event(event);
            switch (event.getType()) {
                case "network_layer_ready":
                    System.out.println("1");
                    receiver5.from_network_layer(buffer.get(next_frame_to_send));
                    nbuffered = nbuffered + 1;
                    send_data1(next_frame_to_send, frame_expected, buffer);//Envio numero de secuencia
                    next_frame_to_send = receiver5.incFrame(next_frame_to_send);
                    break;
                case "frame_arrival":
                    System.out.println("2");
                    receiver5.from_physical_layer(r, protocol);
                    if (r.getSeq() == frame_expected) {
                        receiver5.to_network_layer(r.getInfo());
                        frame_expected = receiver5.incFrame(frame_expected);
                    }
                    while (between(ack_expected, r.getAck(), next_frame_to_send)) {
                        nbuffered = nbuffered - 1;
                        //sender5.stop_timer(ack_expected);
                        ack_expected = receiver5.incFrame(ack_expected);
                    }
                    System.out.println(ack_expected+ " "+ r.getAck()+ " "+next_frame_to_send);
                    break;
                case "cksum_err":
                    System.out.println("3");
                    break; //lo ignora
                case "timeout":
                    next_frame_to_send = ack_expected;
                    for (i = 1; i <= nbuffered; i++) {
                        send_data1(next_frame_to_send, frame_expected, buffer);
                        next_frame_to_send = receiver5.incFrame(next_frame_to_send);
                    }
                    System.out.println("4");
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (nbuffered < receiver5.getMaxSeq()) {
                protocol.setEnableNewtWork(true);
            } else {
                protocol.setEnableNewtWork(false);
            }
        }
    }


    public static void setInterfaz(Interfaz interfaz, double error, int max) {
        GoBackN.interfaz = interfaz;
        //sender5.setError(error);
        receiver5.setError(error);
        //sender5.setMAX_SEQ(max);
        receiver5.setMAX_SEQ(max);
        event = new Event("");
    }

}