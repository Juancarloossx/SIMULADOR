package uptc.edu.co.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazPrincipal extends JFrame {

    private JFrame marco;
    private JPanel panelPrincipal;
    private JLabel etiqueta;
    private JButton botonEstatico, botonDinamico;

    private GridBagConstraints gbc;

    public InterfazPrincipal() {
        inicializarVentana();
        inicializarComponentes();
        agregarComponentes();

        marco.setVisible(true);
    }

    private void inicializarVentana() {
        marco = new JFrame();
        marco.setTitle("SIMULADOR GESTION DE MEMORIA");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(400, 300);
        marco.setLocationRelativeTo(null);

        // Establecer un diseño de borde suave
        marco.getRootPane().setBorder(BorderFactory.createSoftBevelBorder(1));
    }

    private void inicializarComponentes() {
        panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(240, 240, 240));

        gbc = new GridBagConstraints();

        etiqueta = new JLabel("ELIGE UN MÉTODO:");
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        Font nuevaFuente = etiqueta.getFont().deriveFont(Font.BOLD, 20);
        etiqueta.setFont(nuevaFuente);

        botonEstatico = new JButton("Estático");
        botonEstatico.setBackground(new Color(0, 102, 204));
        botonEstatico.setForeground(Color.WHITE);
        botonEstatico.setPreferredSize(new Dimension(120, 30));
        botonEstatico.addActionListener(listener);

        botonDinamico = new JButton("Dinámico");
        botonDinamico.setBackground(new Color(0, 102, 204));
        botonDinamico.setForeground(Color.WHITE);
        botonDinamico.setPreferredSize(new Dimension(120, 30));
        botonDinamico.addActionListener(listener2);

        panelPrincipal.setLayout(new GridBagLayout());
    }

    private void agregarComponentes() {
        marco.add(panelPrincipal);

        // Configurar restricciones para la etiqueta
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        panelPrincipal.add(etiqueta, gbc);

        // Configurar restricciones para el espacio entre la etiqueta y el botón
        gbc.gridy = 1;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);

        // Configurar restricciones para el botón dinámico
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelPrincipal.add(botonDinamico, gbc);

        // Configurar restricciones para el espacio entre los botones
        gbc.gridy = 3;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);

        // Configurar restricciones para el botón estático
        gbc.gridy = 4;
        panelPrincipal.add(botonEstatico, gbc);
    }

    ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            VentanaEstatica v = new VentanaEstatica();
        }
    };

    ActionListener listener2 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            VentanaDinamica v = new VentanaDinamica();
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazPrincipal());
    }
}
