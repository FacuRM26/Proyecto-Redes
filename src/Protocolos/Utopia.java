package Protocolos;
import clases.*;
import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

import static Protocolos.Protocol.*;

public class Utopia {

    public static Protocol protocol = new Protocol();

    public static Maquina sender1 = new Maquina("Sender1");
    public static Maquina receiver1 = new Maquina("Receiver1");
    public  static Event event;

    public static Interfaz interfaz;

    public static void sender() {
        sender1.setInterfaz(Utopia.interfaz); //se setea para configurar los mensajes de la interfaz
        Packet buffer = new Packet();
        Frame s = new Frame("", 0, 0, buffer); // Asigna el paquete al Frame
        while (true) {
            sender1.from_network_layer(buffer); // Obtén algo para enviar desde la capa de red
            s.setInfo(buffer);
            sender1.to_physical_layer(s, protocol); // Envía el frma a la capa física
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void receiver() {
        receiver1.setInterfaz(Utopia.interfaz); //se setea para configurar los mensajes de la interfaz
        protocol.addObserver(receiver1);
        Frame r = new Frame("", 0, 0, new Packet());

        while (true) {
            receiver1.wait_for_event(event); //espera a que haya algo en la capa fisica
            receiver1.from_physical_layer(r, protocol);
            receiver1.to_network_layer(r.getInfo()); // Pasa los datos a la capa de red
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setInterfaz(Interfaz interfaz, double error) {
        Utopia.interfaz = interfaz;
        sender1.setError(error);
        receiver1.setError(error);
        event = new Event("");
    }

}

