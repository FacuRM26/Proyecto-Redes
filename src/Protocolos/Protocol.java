package Protocolos;

import clases.*;
import java.util.Observable;
import java.util.Observer;

import java.util.Random;

public class Protocol extends Observable {

    public static Packet networkLayer;
    public boolean enableNewtWork = true;
    public boolean firstFlag = false;
    public static Frame physicalLayer;



    public static Maquina Sender, Reciber;


    public Packet getNetworkLayer() {
        return networkLayer;
    }

    public static Frame getPhysicalLayer() {
        return physicalLayer;
    }

    public void setNetworkLayer(Packet networkLayer) {
        if (!networkLayer.equals(this.networkLayer) && this.enableNewtWork) {
            this.networkLayer = networkLayer;
            setChanged();
            notifyObservers(networkLayer); // Notifica a los observadores del cambio
        }
    }

    public void setPhysicalLayer(Frame physicalLayer) {
        if (!physicalLayer.equals(this.physicalLayer)) {
            this.physicalLayer = physicalLayer;
            setChanged();
            notifyObservers(this); // Notifica a los observadores del cambio
        }
    }

    public static void setSender(Maquina sender) {
        Sender = sender;
    }

    public static void setReciber(Maquina reciber) {
        Reciber = reciber;
    }

    public void setEnableNewtWork(boolean enable){
        this.enableNewtWork = enable;
        setChanged();
        notifyObservers(this);
    }

    public boolean isEnableNewtWork() {
        return enableNewtWork;
    }

    public  void resetNetwork(boolean enable){
        this.enableNewtWork = enable;
    }

    public boolean isFirstFlag() {
        return firstFlag;
    }

    public String randomAvailability (){
        Random random = new Random();
        String numAle = String.valueOf(random.nextInt(2));
        return numAle;
    }
}
