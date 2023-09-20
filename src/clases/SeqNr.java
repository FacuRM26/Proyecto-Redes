package clases;

public class SeqNr {
    private int value;
    public SeqNr() {
        //random value
        this.value = (int) (Math.random() * 100);
    }
    public SeqNr(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}


