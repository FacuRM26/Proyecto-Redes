package Protocolos;

import clases.*;

import java.nio.DoubleBuffer;
import java.util.Observable;
import java.util.Random;
import java.util.Observer;

public class Maquina implements Observer {

    public static final int MAX_PKT = 1024; // Determina el tamaño del paquete en bytes
    public static int MAX_SEQ = Integer.MAX_VALUE;
    public static int MAX = Integer.MAX_VALUE; // Define el valor máximo para la secuencia

    private volatile boolean stopThread = false;

    public static int actFrame = 0;

    public String name;

    private Interfaz interfaz;

    public double error;

    public String event = "nada";
    public Boolean event2=false;

    public long tInicio;
    public long tFinal;
    public String ttransc;

    public Maquina(String name) {
        this.name = name;
        tInicio = -1;
        tFinal = -1;
        ttransc = "00:00:000";
    }
    public synchronized void wait_for_event() {
        while (!event2) {
            try {
                wait(); // Espera hasta que se notifique un cambio en event
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        event2 = false; // Reinicia event después de esperar
    }
    public synchronized void wait_for_event(Event evento) {
        while (event=="nada") {
            try {
                wait(); // Espera hasta que se notifique un cambio en event
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        evento.setType(event);
        event = "nada"; // Reinicia event después de esperar
    }

    public synchronized void event_occurred(String tipo) {
        event = tipo;
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
    public void to_physical_layer(Frame s, Protocol protocol) {

        // Se usa un randomize para elegir el tipo de FRAME
        Random random = new Random();

        // data (datos), ack (confirmacion), nak (no confirmacion)
        String frameKind = "";

        double randomValue = random.nextDouble(); // Generar un valor aleatorio entre 0.0 y 1.0

        if (randomValue < (error/100)) {
            frameKind = "NAK";
        } else {
            frameKind = "DATA";
        }

        int seq = actFrame;
        inc();

        s.setKind(frameKind);
        s.setSeq(seq);

        Frame r = new Frame();
        r.setAck(s.getAck());
        r.setInfo(s.getInfo());
        r.setKind(s.getKind());
        r.setSeq(s.getSeq());

        protocol.setPhysicalLayer(r);
        interfaz.actualizarSenderText(name + ": Frame " + s.getSeq() + " Enviado");
        // interfaz.actualizarDATA("KIND: " + r.getKind() + " - ACK: " + r.getAck() + " - Data: " + r.getInfo().getData());
    }
    public void to_physical_layer(Frame s, Protocol protocol, EventType event) {
        // Se usa un randomize para elegir el tipo de FRAME
        Random random = new Random();
        int randNumb = random.nextInt(4);

        // data (datos), ack (confirmacion), nak (no confirmacion)
        String frameKind = "";
        switch (randNumb) {
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
        System.out.println(event);
        if (event == EventType.CKSUM_ERR) {
            interfaz.actualizarSenderText(name + ": Frame " + s.getSeq() + " Ha fallado");

        } else if (event == EventType.TIMEOUT) {
            interfaz.actualizarSenderText(name + ": Frame " + s.getSeq() + " Se ha agotado el tiempo");
        } else if (event == EventType.FRAME_ARRIVAL) {
            Frame r = new Frame();
            r.setAck(s.getAck());
            r.setInfo(s.getInfo());
            r.setKind(s.getKind());
            r.setSeq(s.getSeq());

            protocol.setPhysicalLayer(r);
            interfaz.actualizarSenderText(name + ": Frame " + s.getSeq() + " Enviado");
        }
    }

    public void to_physical_layer_manual(Frame s, Protocol protocol) {

        // Se usa un randomize para elegir el tipo de FRAME
        Random random = new Random();

        // data (datos), ack (confirmacion), nak (no confirmacion)
        String frameKind = "";

        double randomValue = random.nextDouble(); // Generar un valor aleatorio entre 0.0 y 1.0

        if (randomValue < (error/100)) {
            frameKind = "NAK";
        } else {
            frameKind = "DATA";
        }

        s.setKind(frameKind);

        Frame r = new Frame();
        r.setAck(s.getAck());
        r.setInfo(s.getInfo());
        r.setKind(s.getKind());
        r.setSeq(s.getSeq());

        protocol.setPhysicalLayer(r);

        interfaz.actualizarSenderText(name + ": Frame " + s.getSeq() + " Enviado");
    }

    // Muestra la información en la capa de red
    public void to_network_layer(Packet packet) {
        //System.out.println("Packet data: " + packet.getData());
        //interfaz.actualizarDATA(packet.getData());
    }

    public void from_physical_layer(Frame r, Protocol protocol) {
        Frame s = protocol.getPhysicalLayer();
        r.setAck(s.getAck());
        r.setInfo(s.getInfo());
        r.setKind(s.getKind());
        r.setSeq(s.getSeq());

        if (s.getKind() == "NAK"){

        } else {
            interfaz.actualizarDATA("KIND: " + r.getKind() + " - ACK: " + r.getAck() + " - Data: " + r.getInfo().getData());
            interfaz.actualizarReceiverText(name + ": ha recibido el frame " + r.getSeq());
        }

        //interfaz.actualizarDATA("KIND: " + r.getKind() + " - ACK: " + r.getAck() + " - Data: " + r.getInfo().getData());
    }

    // Define la macro inc de forma similar a como se hace en C
    public synchronized void inc() {
        if (actFrame < MAX_SEQ) {
            actFrame = actFrame + 1;
        } else {
            actFrame = 0;
        }
    }

    public synchronized int incFrame(int actFrame1) {
        if (actFrame1 < MAX_SEQ) {
            actFrame1 = actFrame1 + 1;
        } else {
            actFrame1 = 0;
        }
        return actFrame1;
    }

    public void setInterfaz(Interfaz interfaz) {
        this.interfaz = interfaz;
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Protocol) {
            Protocol newProto = (Protocol) arg;

            if (newProto.isEnableNewtWork()){
                event_occurred("network_layer_ready");
                System.out.println("xdnt");
                newProto.resetNetwork(false);
            } else {

                if (newProto.getPhysicalLayer().getKind() == "DATA") {
                    event_occurred("frame_arrival");
                }
                if (newProto.getPhysicalLayer().getKind() == "NAK") {
                    this.interfaz.actualizarDATA("FRAME " + newProto.getPhysicalLayer().getSeq() + " PERDIDO - DATA: " + newProto.getPhysicalLayer().getInfo().getData());
                    this.interfaz.actualizarReceiverText("   ");
                    event_occurred("cksum_err");
                }

                if (Protocol.getPhysicalLayer().getKind() == "ACK") {
                    event_occurred("network_layer_ready");
                }
            }

            /*// Realiza la acción que necesitas cuando cambia networkLayer
            if (newPhysicalLayer.getKind() == "DATA"){
                event_occurred("frame_arrival");
            }
            if (newPhysicalLayer.getKind() == "DATA"){
                event_occurred("cksum_err");
            }
            if (newPhysicalLayer.getKind() == "ack_timeout" || newPhysicalLayer.getKind() == "ack_timeout"){
                event_occurred("ack_timeout");
            }
            if (newPhysicalLayer.getKind() == "DATA"){
                event_occurred("timeout");
            }
            if (newPhysicalLayer.getKind() == "DATA"){
                event_occurred("network_layer_ready");
            }*/
             // Notifica que ha ocurrido un evento
        }
    }

    public void setError(double error) {
        this.error = error;
    }

    public static void setActFrame(int actFrame) {
        Maquina.actFrame = actFrame;
    }

    public static int getActFrame() {
        return actFrame;
    }

    public void start_timer(int k) {
        tInicio = System.currentTimeMillis();
    }

    public void stop_timer(int k) {
        tFinal = System.currentTimeMillis();
        calcularTiempoTransc();
    }

    private void calcularTiempoTransc(){
        if (tInicio == -1 || tFinal == -1){
            ttransc = ("00:00:000");
            return ;
        }
        long milesimas = tFinal-tInicio;
        long segundos = milesimas /1000;
        long minutos = segundos/60;
        milesimas -= segundos*1000;
        segundos -= minutos*60;

        String min;
        String seg;
        String mil;

        if( minutos < 10 ) {
            min = "0" + minutos;
        }else{
            min = Long.toString(minutos);
        }

        if( segundos < 10 ) {
            seg = "0" + segundos;
        }else{
            seg = Long.toString(segundos);
        }

        if( milesimas < 10 ){
            mil = "00" + milesimas;
        }else{
            if( milesimas < 100 ) {
                mil = "0" + milesimas;
            }else{
                mil = Long.toString(milesimas);
            }
        }
        ttransc = (min + ":" + seg + ":" + mil);
    }

    public String tTranscurrido(){
        return ttransc;
    }

    public void setCronometro(){
        tInicio = -1;
        tFinal = -1;
        ttransc = "00:00:000";
    }




    public Interfaz getInterfaz() {
        return interfaz;
    }



    public void setMAX_SEQ(int maxSeq) {
        this.MAX_SEQ = maxSeq;
    }


    public static int getMaxSeq() {
        return MAX_SEQ;
    }
}
