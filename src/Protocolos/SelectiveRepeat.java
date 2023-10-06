package Protocolos;

import clases.Event;
import clases.Frame;
import clases.Packet;

import java.util.ArrayList;

public class SelectiveRepeat {

    public static Protocol protocol = new Protocol();
    public static Maquina receiver5 = new Maquina("Maquina6");
    public static Interfaz interfaz;
    public static Event event;
    public static int NR_BUFS;

    public static boolean no_nak = true;

    public static boolean between(int valorA, int valorB, int valorC) {
        if (((valorA <= valorB) && (valorB < valorC)) ||
                ((valorC < valorA) && (valorA <= valorB)) ||
                ((valorB < valorC) && (valorC < valorA))) {
            return true;
        } else {
            return false;
        }
    }

    public static void send_frame(String fk, int frame_nr, int frame_expected, ArrayList<Packet> buffer) {
        Frame s = new Frame();
        s.setKind(fk);
        if (fk == "DATA"){
            s.setInfo(buffer.get(frame_nr % NR_BUFS));
        }
        s.setSeq(frame_nr);
        s.setAck((frame_expected + receiver5.getMaxSeq()) % (receiver5.getMaxSeq()+1));
        if (fk == "NAK"){
            no_nak = false;
        }
        receiver5.to_physical_layer_manual(s, protocol);
        if (fk == "DATA"){
            //receiver5.startTimer(frame_nr % NR_BUFS);
        }
        //stop_ack_timer()
    }

    public static void selectiveRepeat (){
        receiver5.setInterfaz(SelectiveRepeat.interfaz);
        protocol.addObserver(receiver5);

        int ack_expected;
        int next_frame_to_send;
        int frame_expected;
        int too_far;
        Frame r = new Frame();
        ArrayList<Packet> out_buf = new ArrayList<Packet>();
        ArrayList<Packet> in_buf = new ArrayList<Packet>();
        ArrayList<Boolean> arrived = new ArrayList<Boolean>();
        int nbuffered;
        int i;

        for ( i = 0; i < receiver5.getMaxSeq() + 1; i++) {
            Packet packet1 = new Packet();
            Packet packet2 = new Packet();
            out_buf.add(packet1);
            in_buf.add(packet2);
            arrived.add(false);
        }

        protocol.setEnableNewtWork(true);
        ack_expected = 0;
        next_frame_to_send =0;
        frame_expected = 0;
        too_far = NR_BUFS;
        nbuffered = 0;

        while (true) {
            receiver5.wait_for_event(event);
            switch (event.getType()) {
                case "network_layer_ready":
                    System.out.println("volvi");
                    nbuffered = nbuffered + 1;
                    receiver5.from_network_layer(out_buf.get(next_frame_to_send % NR_BUFS));
                    send_frame("DATA", next_frame_to_send, frame_expected, out_buf);
                    next_frame_to_send = receiver5.incFrame(next_frame_to_send);
                    break;

                case "frame_arrival":
                    receiver5.from_physical_layer(r,protocol);
                    if (r.getKind()=="DATA"){
                        if ((r.getSeq() != frame_expected) && no_nak){
                            send_frame("NAK", 0, frame_expected, out_buf);
                        } else {
                            //start_ack_timer();
                        }
                        if(between(frame_expected, r.getSeq(), too_far) && (arrived.get(r.getSeq() % NR_BUFS) == false)){
                            arrived.set(r.getSeq() % NR_BUFS, true);
                            in_buf.set(r.getSeq() % NR_BUFS, r.getInfo());
                            while (arrived.get(r.getSeq() % NR_BUFS)){
                                receiver5.to_network_layer(in_buf.get(r.getSeq() % NR_BUFS));
                                no_nak = true;
                                arrived.set(r.getSeq() % NR_BUFS, false);
                                frame_expected = receiver5.incFrame(frame_expected);
                                too_far = receiver5.incFrame(too_far);
                                //start_ack timer
                            }
                        }
                    }
                    if ((r.getKind()=="NAK") && between(ack_expected, (r.getAck()+1) % (receiver5.getMaxSeq()+1), next_frame_to_send)){
                        send_frame("DATA", (r.getAck()+1) % (receiver5.getMaxSeq()+1), frame_expected, out_buf);
                    }
                    while (between(ack_expected, r.getAck(), next_frame_to_send)) {
                        nbuffered = nbuffered - 1;
                        //sender5.stop_timer(ack_expected);
                        ack_expected = receiver5.incFrame(ack_expected);
                    }
                    break;
                case "cksum_err":
                    if (no_nak){
                        send_frame("NAK", 0, frame_expected, out_buf);
                    }
                    break;
                case "timeout":
                    send_frame("ACK", 0, frame_expected, out_buf);
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (nbuffered < NR_BUFS) {
                protocol.setEnableNewtWork(true);
            } else {
                protocol.setEnableNewtWork(false);
            }
        }

    }

    public static void setInterfaz(Interfaz interfaz, double error, int max) {
        SelectiveRepeat.interfaz = interfaz;
        NR_BUFS =  (max +1)/2;
        receiver5.setError(error);
        receiver5.setMAX_SEQ(max);
        event = new Event("");
    }

}
