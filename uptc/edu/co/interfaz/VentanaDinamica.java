package uptc.edu.co.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaDinamica extends JFrame {

    public VentanaDinamica() {
        setTitle("Configuración de Ajustes");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Crear paneles y disposición
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(3, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Crear botones con estilo personalizado
        JButton btnPrimerAjuste = crearBoton("Primer Ajuste");
        JButton btnPeorAjuste = crearBoton("Peor Ajuste");
        JButton btnMejorAjuste = crearBoton("Mejor Ajuste");

        // Agregar ActionListener a cada botón
        btnPrimerAjuste.addActionListener(e -> abrirVentanaPrimerAjuste());
        btnPeorAjuste.addActionListener(e -> abrirVentanaPeorAjuste());
        btnMejorAjuste.addActionListener(e -> abrirVentanaMejorAjuste());

        // Agregar botones al panel
        panelPrincipal.add(btnPrimerAjuste);
        panelPrincipal.add(btnPeorAjuste);
        panelPrincipal.add(btnMejorAjuste);

        // Agregar panel principal al frame
        add(panelPrincipal);

        setVisible(true);
    }

    // Método para crear botones con estilo
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(0, 102, 204));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente más grande y en negrita
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Añadir margen interno
        return boton;
    }

    // Método para abrir la ventana del Primer Ajuste
    private void abrirVentanaPrimerAjuste() {
        VentanaPrimerAjuste ventanaPrimerAjuste = new VentanaPrimerAjuste();
        setVisible(false);
    }

    // Método para abrir la ventana del Peor Ajuste
    private void abrirVentanaPeorAjuste() {
        VentanaPeorAjuste ventanaPeorAjuste = new VentanaPeorAjuste();
        setVisible(false);
    }

    // Método para abrir la ventana del Mejor Ajuste
    private void abrirVentanaMejorAjuste() {
        VentanaMejorAjuste ventanaMejorAjuste = new VentanaMejorAjuste();
        setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaDinamica::new);
    }
}
