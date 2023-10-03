package Protocolos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame implements ActionListener {

    private JButton btnIniciar, btnPausar, btnResumir;
    private JComboBox<String> comboBoxProtocolos;
    private JLabel lblProtocolo, lblPorcentajeError, lblSender, lblReceiver;
    private JTextField txtPorcentajeError, txtSender, txtReceiver;
    private ProtocolWorker protocolWorker;

    public Interfaz() {
        setTitle("Interfaz Gráfica");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        lblProtocolo = new JLabel("Seleccione el protocolo");
        comboBoxProtocolos = new JComboBox<>(new String[]{"Protocolo Utopia", "Protocolo Stop-and-wait", "Protocolo PAR", "Protocolo sliding window de 1 bit", "Protocolo go-back-n", "Protocolo selective-repeat"});
        lblPorcentajeError = new JLabel("Porcentaje de error");
        txtPorcentajeError = new JTextField();
        lblSender = new JLabel("Sender");
        txtSender = new JTextField();
        lblReceiver = new JLabel("Receiver");
        txtReceiver = new JTextField();
        btnIniciar = new JButton("Iniciar");
        btnPausar = new JButton("Pausar");
        btnResumir = new JButton("Resumir");

        add(lblProtocolo);
        add(comboBoxProtocolos);
        add(lblPorcentajeError);
        add(txtPorcentajeError);
        add(lblSender);
        add(txtSender);
        add(lblReceiver);
        add(txtReceiver);
        add(btnIniciar);
        add(btnPausar);
        add(btnResumir);

        btnIniciar.addActionListener(this);
        btnPausar.addActionListener(this);
        btnResumir.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIniciar) {
            String porcentajeErrorStr = txtPorcentajeError.getText();
            double porcentajeError = 0.0;

            // Verificar si el campo no está vacío
            if (!porcentajeErrorStr.isEmpty()) {
                try {
                    porcentajeError = Double.parseDouble(porcentajeErrorStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Porcentaje de error no válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String protocoloSeleccionado = (String) comboBoxProtocolos.getSelectedItem();

            txtSender.setText(("estado del sender"));
            txtReceiver.setText("estado del receiver");

            if (protocolWorker != null) {
                protocolWorker.cancel(true); // Detener el trabajo anterior si existe
            }

            protocolWorker = new ProtocolWorker(protocoloSeleccionado, this);
            protocolWorker.execute(); // Ejecutar el trabajo en segundo plano
        } else if (e.getSource() == btnPausar) {
            // Pausar
        } else if (e.getSource() == btnResumir) {
            // Resumir
        }
    }

    public void actualizarSenderText(String mensaje) {
        SwingUtilities.invokeLater(() -> txtSender.setText(mensaje));
    }

    public void actualizarReceiverText(String mensaje) {
        SwingUtilities.invokeLater(() -> txtReceiver.setText(mensaje));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz ventana = new Interfaz();
            ventana.setVisible(true);
        });
    }
}

class ProtocolWorker extends SwingWorker<Void, Void> {
    private String protocolo;
    private Interfaz interfaz;

    public ProtocolWorker(String protocolo, Interfaz interfaz) {
        this.protocolo = protocolo;
        this.interfaz = interfaz;
    }

    @Override
    protected Void doInBackground() throws Exception {
        switch (protocolo){
            case("Protocolo Utopia"):
                Utopia.setInterfaz(interfaz);

                Thread senderThread = new Thread(Utopia::sender);
                Thread receiverThread = new Thread(Utopia::receiver);

                senderThread.start();
                receiverThread.start();

                try {
                    // Espera a que los hilos terminen (puedes ajustar el tiempo de espera según tus necesidades)
                    senderThread.join();
                    receiverThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case ("Protocolo Stop-and-wait"):
                StopAndWait.setInterfaz(interfaz);

                Thread senderThread2 = new Thread(StopAndWait::sender);
                Thread receiverThread2 = new Thread(StopAndWait::receiver);

                senderThread2.start();
                receiverThread2.start();

                try {
                    // Espera a que los hilos terminen (puedes ajustar el tiempo de espera según tus necesidades)
                    senderThread2.join();
                    receiverThread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case ("Protocolo PAR"):
                break;
            case ("Protocolo sliding window de 1 bit"):
                break;
            case ("Protocolo go-back-n"):
                break;
            case ("Protocolo selective-repeat"):
                break;
        }

        return null;
    }
}