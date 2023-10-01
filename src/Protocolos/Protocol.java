package Protocolos;

import clases.*;
import java.util.Observable;
import java.util.Observer;

import java.util.Random;
import java.util.random.*;

public class Protocol extends Observable {

    public static Packet networkLayer;
    public static Frame physicalLayer;

    public static Maquina Sender, Reciber;


    public Packet getNetworkLayer() {
        return networkLayer;
    }

    public static Frame getPhysicalLayer() {
        return physicalLayer;
    }

    public void setNetworkLayer(Packet networkLayer) {
        if (!networkLayer.equals(this.networkLayer)) {
            this.networkLayer = networkLayer;
            setChanged();
            notifyObservers(networkLayer); // Notifica a los observadores del cambio
        }
    }

    public void setPhysicalLayer(Frame physicalLayer) {
        if (!physicalLayer.equals(this.physicalLayer)) {
            this.physicalLayer = physicalLayer;
            setChanged();
            notifyObservers(physicalLayer); // Notifica a los observadores del cambio
        }
    }

    public static void setSender(Maquina sender) {
        Sender = sender;
    }

    public static void setReciber(Maquina reciber) {
        Reciber = reciber;
    }
}
