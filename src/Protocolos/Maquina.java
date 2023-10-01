package Protocolos;

import clases.*;
import java.util.Observable;
import java.util.Random;
import java.util.Observer;

public class Maquina implements Observer {

    public static final int MAX_PKT = 1024; // Determina el tamaño del paquete en bytes
    public static final int MAX_SEQ = 255; // Define el valor máximo para la secuencia

    public static int actFrame = 0;

    public static String name;

    public static boolean event = false;

    public Maquina(String name) {
        this.name = name;
    }

    public synchronized void wait_for_event() {
        while (!event) {
            try {
                wait(); // Espera hasta que se notifique un cambio en event
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        event = false; // Reinicia event después de esperar
    }

    public synchronized void event_occurred() {
        event = true;
        notify(); // Notifica a cualquier hilo esperando en wait_for_event
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

    /* Pass the frame to the physical layer for transmission. */
    public static void to_physical_layer(Frame s, Protocol protocol) {

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

        // Ejemplo: Imprimir los datos de la tram
        /*System.out.println("FrameKind: " + s.getKind());
        System.out.println("Ack: " + s.getAck());
        System.out.println("Info: " + s.getInfo().getData());*/


        Frame r = new Frame();
        r.setAck(s.getAck());
        r.setInfo(s.getInfo());
        r.setKind(s.getKind());
        r.setSeq(s.getSeq());

        protocol.setPhysicalLayer(r);
    }

    // Muestra la información en la capa de red
    public static void to_network_layer(Packet packet) {
        System.out.println("Packet data: " + packet.getData());
    }

    /* Go get an inbound frame from the physical layer and copy it to r. */
    public static void from_physical_layer(Frame r, Protocol protocol) {
        Frame s = protocol.getPhysicalLayer();
        r.setAck(s.getAck());
        r.setInfo(s.getInfo());
        r.setKind(s.getKind());
        r.setSeq(s.getSeq());
        System.out.println(name + ": ha recibido el frame " + r.getSeq());
    }

    public static void start_timer(int k) {
        //pendiente
    }

    public static void stop_timer(int k) {
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

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Protocol) {
            Frame newPhysicalLayer = (Frame) arg;
            // Realiza la acción que necesitas cuando cambia networkLayer
            event_occurred(); // Notifica que ha ocurrido un evento
        }
    }
}
