package Protocolos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame implements ActionListener {

    private JButton btnIniciar, btnPausar, btnResumir;
    private JComboBox<String> comboBoxProtocolos;
    private JLabel lblProtocolo, lblPorcentajeError, lblSender, lblReceiver, lblData, lblMax;
    private JTextField txtPorcentajeError, txtSender, txtReceiver, txtData, txtMax;
    private ProtocolWorker protocolWorker;
    public static double errorProb=0.0;
    public Interfaz() {
        setTitle("Interfaz Gráfica");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2));

        lblProtocolo = new JLabel("Seleccione el protocolo");
        comboBoxProtocolos = new JComboBox<>(new String[]{"Protocolo Utopia", "Protocolo Stop-and-wait", "Protocolo PAR", "Protocolo sliding window de 1 bit", "Protocolo go-back-n", "Protocolo selective-repeat"});
        lblPorcentajeError = new JLabel("Porcentaje de error");
        txtPorcentajeError = new JTextField();
        lblSender = new JLabel("Sender");
        txtSender = new JTextField();
        lblReceiver = new JLabel("Receiver");
        txtReceiver = new JTextField();
        lblData = new JLabel("Frame Data");
        txtData = new JTextField();
        lblMax = new JLabel("MaxSEQ");
        txtMax = new JTextField();
        txtReceiver = new JTextField();
        btnIniciar = new JButton("Iniciar");
        btnPausar = new JButton("Pausar");
        btnResumir = new JButton("Resumir");

        add(lblProtocolo);
        add(comboBoxProtocolos);
        add(lblPorcentajeError);
        add(txtPorcentajeError);
        add(lblMax);
        add(txtMax);
        add(lblSender);
        add(txtSender);
        add(lblReceiver);
        add(txtReceiver);
        add(lblData);
        add(txtData);
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
            String maxSeqStr = txtMax.getText();
            double porcentajeError = 0.0;
            int maxSeq = Integer.MAX_VALUE;

            // Verificar si el campo no está vacío
            if (!porcentajeErrorStr.isEmpty()) {
                try {
                    porcentajeError = Double.parseDouble(porcentajeErrorStr);
                    errorProb= porcentajeError;

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Porcentaje de error no válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!maxSeqStr.isEmpty()) {
                try {
                    maxSeq = Integer.parseInt(maxSeqStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "MAXIMMO NO VALIDO", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String protocoloSeleccionado = (String) comboBoxProtocolos.getSelectedItem();


            if (protocolWorker != null) {
                protocolWorker.cancel(true); // Detener el trabajo anterior si existe
            }

            protocolWorker = new ProtocolWorker(protocoloSeleccionado, porcentajeError, maxSeq ,this);
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

    public void actualizarDATA(String mensaje) {
        SwingUtilities.invokeLater(() -> txtData.setText(mensaje));
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

    private double porcentajeError;
    private int maxSeq;

    public ProtocolWorker(String protocolo, Double porcentajeError, int maxSeq, Interfaz interfaz) {
        this.protocolo = protocolo;
        this.interfaz = interfaz;
        this.porcentajeError = porcentajeError;
        this.maxSeq = maxSeq;
    }

    @Override
    protected Void doInBackground() throws Exception {
        switch (protocolo){
            case("Protocolo Utopia"):
                Utopia.setInterfaz(interfaz, 0);

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
                StopAndWait.setInterfaz(interfaz, 0);

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
                PAR.setInterfaz(interfaz, porcentajeError);

                Thread senderThread3 = new Thread(PAR::sender);
                Thread receiverThread3 = new Thread(PAR::receiver);

                senderThread3.start();
                receiverThread3.start();

                try {
                    // Espera a que los hilos terminen (puedes ajustar el tiempo de espera según tus necesidades)
                    senderThread3.join();
                    receiverThread3.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case ("Protocolo sliding window de 1 bit"):
                slidingWindow.setInterfaz(interfaz, porcentajeError);
                Thread senderThread4 = new Thread(slidingWindow::protocol4);
                Thread receiverThread4 = new Thread(slidingWindow::receiver4);

                senderThread4.start();
                receiverThread4.start();

                try {
                    // Espera a que los hilos terminen (puedes ajustar el tiempo de espera según tus necesidades)
                    senderThread4.join();
                    receiverThread4.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case ("Protocolo go-back-n"):
                GoBackN.setInterfaz(interfaz, porcentajeError, maxSeq);

                Thread protocolo = new Thread(GoBackN::receiver);
                Thread inicializador = new Thread(GoBackN::receiver);

                protocolo.start();
                inicializador.start();

                try {
                    // Espera a que los hilos terminen (puedes ajustar el tiempo de espera según tus necesidades)
                    protocolo.join();
                    inicializador.join();
                } catch (InterruptedException e) {
                    System.out.println("xd");
                    e.printStackTrace();
                }
                break;
            case ("Protocolo selective-repeat"):
                SelectiveRepeat.setInterfaz(interfaz, porcentajeError, maxSeq);

                Thread senderThread6 = new Thread(SelectiveRepeat::selectiveRepeat);
                Thread receiverThread6 = new Thread(SelectiveRepeat::selectiveRepeat);

                senderThread6.start();
                receiverThread6.start();

                try {
                    // Espera a que los hilos terminen (puedes ajustar el tiempo de espera según tus necesidades)
                    receiverThread6.join();
                    receiverThread6.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }

        return null;
    }
}