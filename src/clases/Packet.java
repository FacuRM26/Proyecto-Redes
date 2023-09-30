package clases;

import static Protocolos.Protocol.MAX_PKT;

public class Packet {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        if (data.length() <= MAX_PKT) {
            this.data = data;
        }
    }

}

