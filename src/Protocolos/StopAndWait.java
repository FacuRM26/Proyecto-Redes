package Protocolos;

import clases.Event;
import clases.Frame;
import clases.Packet;
import clases.EventType; // Importa tu enum EventType



public class StopAndWait {
    public static Protocol protocol = new Protocol();
    public static Maquina sender2 = new Maquina("Sender1");
    public static Maquina receiver2 = new Maquina("Receiver1");
    public static Interfaz interfaz;
    public static Event event;


    public static void sender() {
        sender2.setInterfaz(StopAndWait.interfaz); //se setea para configurar los mensajes de la interfaz
        protocol.addObserver(sender2);
        Frame s = new Frame("", 0, 0, new Packet()); // buffer para una trama de salida
        Packet buffer = new Packet(); // buffer para un paquete de salida

        while (true) {
            sender2.from_network_layer(buffer);
            s.setInfo(buffer);
            sender2.to_physical_layer(s, protocol);
            System.out.println("Sender2: Frame " + s.getSeq() + " Enviado");
            sender2.wait_for_event(event);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void receiver() {
        receiver2.setInterfaz(StopAndWait.interfaz); //se setea para configurar los mensajes de la interfaz
        protocol.addObserver(receiver2);
        Frame r = new Frame("", 0, 0, new Packet());
        Frame s = new Frame("", 0, 0, new Packet());

        while (true) {
            receiver2.wait_for_event(event);
            receiver2.from_physical_layer(r, protocol);
            if(r.getAck() == 0) {
                receiver2.to_network_layer(r.getInfo());
                r.setAck(1);
                receiver2.to_physical_layer(r, protocol);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setInterfaz(Interfaz interfaz, double error) {
        StopAndWait.interfaz = interfaz;
        sender2.setError(error);
        receiver2.setError(error);
        event = new Event("");
    }
}
