package uptc.edu.co.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaDinamica extends JFrame {

    private JButton btnPrimerAjuste,btnPeorAjuste,btnMejorAjuste;


    public VentanaDinamica() {
        setTitle("Configuración de Ajustes");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear paneles y disposición
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(3, 1, 0, 10));

        // Crear botones
        JButton btnPrimerAjuste = new JButton("Primer Ajuste");
        JButton btnPeorAjuste = new JButton("Peor Ajuste");
        JButton btnMejorAjuste = new JButton("Mejor Ajuste");

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
}

