package uptc.edu.co.interfaz;

import javax.swing.*;
import java.awt.*;

public class Interfaz  extends JFrame{

        private JFrame marco;
        private JPanel panelPrincipal;
        private JLabel etiqueta;
        private JButton botonEstatico,botonDinamico;

        private   GridBagConstraints gbc;
        
        public Interfaz() {
            inicializarVentana();
            inicializarComponentes();
            agregarComponentes();

            marco.setVisible(true);
        }

        private void inicializarVentana() {
            marco = new JFrame();
            marco.setTitle("SIMULADOR GESTION DE MEMORIA ");
            marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            marco.setSize(400, 300);
            marco.setLocationRelativeTo(null);
        }

        private void inicializarComponentes() {
            panelPrincipal = new JPanel();

            gbc = new GridBagConstraints();

            etiqueta = new JLabel("ELIGE UN METODO:");
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            Font nuevaFuente = etiqueta.getFont().deriveFont(Font.BOLD, 20); // Tamaño 18
            etiqueta.setFont(nuevaFuente);

            botonEstatico = new JButton("Estatico");
            botonEstatico.setBackground(new Color(0,0,0));
            botonEstatico.setForeground(Color.WHITE);
            botonEstatico.setPreferredSize(new Dimension(120, 30));

            botonDinamico = new JButton("Dinamico");
            botonDinamico.setBackground(new Color(0,0,0));
            botonDinamico.setForeground(Color.WHITE);
            botonDinamico.setPreferredSize(new Dimension(120, 30));

            panelPrincipal.setLayout(new GridBagLayout());
        }

        private void agregarComponentes() {
            marco.add(panelPrincipal);

            // Configurar restricciones para la etiqueta
            gbc.anchor=GridBagConstraints.CENTER;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;  // Ocupa dos columnas
            gbc.insets = new Insets(10, 0, 0, 0);  // Márgenes superiores
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









    }


