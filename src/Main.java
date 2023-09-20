import Protocolos.Utopia;

public class Main {
    public static void main(String[] args) {
        // Inicializa y ejecuta el sender y el receiver de Utopía en hilos separados
        Thread senderThread = new Thread(Utopia::sender1);
        Thread receiverThread = new Thread(Utopia::receiver1);

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
