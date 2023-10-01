package Protocolos;
import clases.*;
import java.util.Observable;
import java.util.Observer;

import static Protocolos.Protocol.*;

public class Utopia {

    public static Protocol protocol = new Protocol();
    public static Maquina sender1 = new Maquina("Sender1");
    public static Maquina receiver1 = new Maquina("Receiver1");
   // protocol.setNetworkLayer("Nuevo valor de networkLayer");


    public static void sender() {
        Packet buffer = new Packet();
        Frame s = new Frame("", 0, 0, buffer); // Asigna el paquete al Frame

        while (true) {
            sender1.from_network_layer(buffer); // Obtén algo para enviar desde la capa de red
            s.setInfo(buffer); // Copia el paquete en la trama para la transmisión
            sender1.to_physical_layer(s, protocol); // Envía la trama a la capa física
            System.out.println(sender1.name + ": Frame " + s.getSeq() + " Enviado");
        }
    }

    public static void receiver() {
        protocol.addObserver(receiver1);
        Frame r = new Frame("", 0, 0, new Packet());
        while (true) {
            receiver1.wait_for_event(); // Solo es posible la llegada de una trama
            receiver1.from_physical_layer(r, protocol); // Obtén la trama entrante
            receiver1.to_network_layer(r.getInfo()); // Pasa los datos a la capa de red
        }
    }
}

