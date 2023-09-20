package clases;

import static Protocolos.Protocol.MAX_PKT;

public class Packet {
    private byte[] data = new byte[MAX_PKT];

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        if (data.length <= MAX_PKT) {
            this.data = data;
        }
    }

}

