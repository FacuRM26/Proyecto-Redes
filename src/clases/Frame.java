package clases;

import static Protocolos.Protocol.MAX_SEQ;

public class Frame {
    private FrameKind kind; // ¿Qué tipo de trama es?
    private SeqNr seq; // Número de secuencia
    private SeqNr ack; // Número de confirmación
    private Packet info; // El paquete de la capa de red

    public Frame(FrameKind kind, SeqNr seq, SeqNr ack, Packet info) {
        this.kind = kind;
        this.seq = seq;
        this.ack = ack;
        this.info = info;
    }

    public Frame() {
    }

    public FrameKind getKind() {
        return kind;
    }

    public void setKind(FrameKind kind) {
        this.kind = kind;
    }

    public SeqNr getSeq() {
        return seq;
    }

    public void setSeq(SeqNr seq) {
        this.seq = seq;
    }

    public SeqNr getAck() {
        return ack;
    }

    public void setAck(SeqNr ack) {
        this.ack = ack;
    }

    public Packet getInfo() {
        return info;
    }

    public void setInfo(Packet info) {
        this.info = info;
    }

    // Define la macro inc de forma similar a como se hace en C
    private void inc(SeqNr k) {
        if (k.getValue() < MAX_SEQ) {
            k.setValue(k.getValue() + 1);
        } else {
            k.setValue(0);
        }
    }

    public String getPacketDataAsString() {
        byte[] packetData = info.getData();
        StringBuilder dataString = new StringBuilder();
        for (byte b : packetData) {
            dataString.append(b).append(" ");
        }
        return dataString.toString();
    }
    @Override
    public String toString() {
        return "FrameKind: " + kind +
                "\nSeq: " + seq.getValue() +
                "\nAck: " + ack.getValue() +
                "\nPacket Data:\n" + getPacketDataAsString();
    }


}