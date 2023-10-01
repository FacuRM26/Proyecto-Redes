package clases;

import static Protocolos.Maquina.MAX_SEQ;

public class Frame {
    private String kind; // ¿Qué tipo de frame es?
    private int seq; // Número de secuencia
    private int ack; // Número de confirmación
    private Packet packet; // El paquete de la capa de red
    private Frame nextFrame;
    private Frame prevFrame;

    public Frame(String kind, int seq, int ack, Packet packet) {
        this.kind = kind;
        this.seq = seq;
        this.ack = ack;
        this.packet = packet;
    }

    public Frame() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public Packet getInfo() {
        return packet;
    }

    public void setInfo(Packet packet) {
        this.packet = packet;
    }


    @Override
    public String toString() {
        return "FrameKind: " + kind +
                "\nSeq: " + seq +
                "\nAck: " + ack +
                "\nPacket Data:\n" + packet.getData();
    }
}