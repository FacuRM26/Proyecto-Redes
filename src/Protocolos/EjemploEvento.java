package Protocolos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EjemploEvento extends JFrame implements ActionListener {

    private JButton btnIniciar, btnPausar, btnResumir;
    private JComboBox<String> comboBoxProtocolos;
    private JLabel lblProtocolo, lblPorcentajeError, lblSender, lblReceiver;
    private JTextField txtPorcentajeError, txtSender, txtReceiver;

    public EjemploEvento() {
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

            double nuevoSenderValor = porcentajeError + 1;
            txtSender.setText(Double.toString(nuevoSenderValor));

            String protocoloSeleccionado = (String) comboBoxProtocolos.getSelectedItem();
            txtReceiver.setText(protocoloSeleccionado);

        } else if (e.getSource() == btnPausar) {
            // Pausar
        } else if (e.getSource() == btnResumir) {
            // Resumir
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EjemploEvento ventana = new EjemploEvento();
            ventana.setVisible(true);
        });
    }
}