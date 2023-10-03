package Protocolos;

import clases.*;
import java.util.Observable;
import java.util.Random;
import java.util.Observer;

public class Maquina implements Observer {

    public static final int MAX_PKT = 1024; // Determina el tamaño del paquete en bytes
    public static final int MAX_SEQ = 255; // Define el valor máximo para la secuencia

    private volatile boolean stopThread = false;

    public static int actFrame = 0;

    public String name;

    private Interfaz interfaz;

    public boolean event = false;

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
    public void to_physical_layer(Frame s, Protocol protocol) {

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

        Frame r = new Frame();
        r.setAck(s.getAck());
        r.setInfo(s.getInfo());
        r.setKind(s.getKind());
        r.setSeq(s.getSeq());

        protocol.setPhysicalLayer(r);
        interfaz.actualizarSenderText(name + ": Frame " + s.getSeq() + " Enviado");
    }

    // Muestra la información en la capa de red
    public static void to_network_layer(Packet packet) {
        System.out.println("Packet data: " + packet.getData());
    }

    public void from_physical_layer(Frame r, Protocol protocol) {
        Frame s = protocol.getPhysicalLayer();
        r.setAck(s.getAck());
        r.setInfo(s.getInfo());
        r.setKind(s.getKind());
        r.setSeq(s.getSeq());
        interfaz.actualizarReceiverText(name + ": ha recibido el frame " + r.getSeq());
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

    // Define la macro inc de forma similar a como se hace en C
    private synchronized void inc() {
        if (actFrame < MAX_SEQ) {
            actFrame = actFrame + 1;
        } else {
            stopThread = true;
        }
    }

    public void setInterfaz(Interfaz interfaz) {
        this.interfaz = interfaz;
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Protocol) {
            Frame newPhysicalLayer = (Frame) arg;
            // Realiza la acción que necesitas cuando cambia networkLayer
            event_occurred(); // Notifica que ha ocurrido un evento
        }
    }

    public void setMAX_SEQ(int maxSeq) {
        this.MAX_SEQ = maxSeq;
    }
}
