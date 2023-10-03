import Protocolos.PAR;
import Protocolos.StopAndWait;
import Protocolos.Utopia;

public class Main {
    public static void main(String[] args) {
// Inicializa y ejecuta el sender y el receiver de Stop-and-Wait en hilos separados
        Thread senderThread = new Thread(StopAndWait::sender);
        Thread receiverThread = new Thread(StopAndWait::receiver);

        senderThread.start();
        receiverThread.start();

        try {
            // Espera a que los hilos terminen (puedes ajustar el tiempo de espera según tus necesidades)
            senderThread.join();
            receiverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
