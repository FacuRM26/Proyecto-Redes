package Protocolos;
import clases.*;

import static Protocolos.Protocol.*;

public class PAR {

    public static Protocol protocol = new Protocol();
    public static Protocol protocol2 = new Protocol();

    public static Maquina sender3 = new Maquina("sender3");
    public static Maquina receiver3 = new Maquina("receiver3");

    public static Event event;

    public static Interfaz interfaz;

    public static void sender() {
        protocol2.addObserver(sender3);
        sender3.setInterfaz(PAR.interfaz); //se setea para configurar los mensajes de la interfaz


        Packet buffer = new Packet();
        Frame s = new Frame("", 0, 0, buffer);
        int nxtSend = 0; // Asigna el paquete al Frame
        sender3.from_network_layer(buffer); // Obtén algo para enviar desde la capa de red

        while (true) {
            s.setInfo(buffer);
            s.setSeq(nxtSend); //0
            sender3.to_physical_layer_manual(s, protocol); // Envía el frma a la capa física
            //sender3.start_timer(nxtSend);

            sender3.wait_for_event(event); //espera
            if (event.getType() == "frame_arrival"){
                sender3.from_physical_layer(s, protocol2); //iguala
                if (s.getAck() == nxtSend) { //0 == 0
                   // sender3.stop_timer(s.getAck());
                    sender3.from_network_layer(buffer); //nuevo paquete
                    nxtSend = sender3.incFrame(nxtSend); // 1
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void receiver() {
        receiver3.setInterfaz(PAR.interfaz); //se setea para configurar los mensajes de la interfaz
        protocol.addObserver(receiver3);

        int frame_expected = 0;
        Frame r = new Frame("", 0, 0, new Packet());
        Frame s = new Frame("", 0, 0, new Packet());

        Event event1 = new Event("");

        while (true) {
            receiver3.wait_for_event(event1); //espera a que haya algo en la capa fisica

            if (event1.getType() == "frame_arrival"){ //acá
                receiver3.from_physical_layer(r, protocol); //iguala
                if (r.getSeq()==frame_expected){
                    receiver3.to_network_layer(r.getInfo()); //PRINT
                    System.out.println(frame_expected);
                    frame_expected = receiver3.incFrame(frame_expected); //1
                }
            }
            r.setAck(1 - frame_expected); // 0

            receiver3.to_physical_layer_manual(r, protocol2); //Envia

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setInterfaz(Interfaz interfaz, double error) {
        PAR.interfaz = interfaz;
        sender3.setError(error);
        receiver3.setError(error);
        event = new Event("");
        sender3.setMAX_SEQ(1);
        receiver3.setMAX_SEQ(1);
    }

}
