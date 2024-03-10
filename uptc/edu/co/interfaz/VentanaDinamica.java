package uptc.edu.co.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaDinamica extends JFrame {

    public VentanaDinamica() {
        setTitle("Configuración de Ajustes");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Crear paneles y disposición
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(3, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Crear botones
        JButton btnPrimerAjuste = crearBoton("Primer Ajuste");
        JButton btnPeorAjuste = crearBoton("Peor Ajuste");
        JButton btnMejorAjuste = crearBoton("Mejor Ajuste");

        // Agregar ActionListener a cada botón
        btnPrimerAjuste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para el primer ajuste
                VentanaPrimerAjuste ventanaPrimerAjuste = new VentanaPrimerAjuste();
            }
        });

        btnPeorAjuste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para el peor ajuste
                JOptionPane.showMessageDialog(null, "Se seleccionó Peor Ajuste");
            }
        });

        btnMejorAjuste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para el mejor ajuste
                JOptionPane.showMessageDialog(null, "Se seleccionó Mejor Ajuste");
            }
        });

        // Agregar botones al panel
        panelPrincipal.add(btnPrimerAjuste);
        panelPrincipal.add(btnPeorAjuste);
        panelPrincipal.add(btnMejorAjuste);

        // Agregar panel principal al frame
        add(panelPrincipal);

        setVisible(true);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(0, 102, 204));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.PLAIN, 12)); // Tamaño de la fuente reducido
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaDinamica());
    }
}
