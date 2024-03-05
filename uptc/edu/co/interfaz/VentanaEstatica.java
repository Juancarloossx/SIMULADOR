package uptc.edu.co.interfaz;

import uptc.edu.co.controller.BloqueMemoria;
import uptc.edu.co.controller.Proceso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaEstatica extends JFrame {

    private static final int MAX_MEMORIA = 1024; // tamanno máximo de memoria en bytes
    private static java.util.List<BloqueMemoria> bloquesMemoria = new ArrayList<>();
    private static java.util.List<Proceso> procesos = new ArrayList<>();
    private JPanel memoriaPanel;

    public VentanaEstatica() {

        JButton liberarButton = new JButton("Liberar Memoria");
        memoriaPanel = new JPanel();

        liberarButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event) {
                liberarMemoriaAleatoria();
                actualizarEstadoMemoria();
            }
        });

        JPanel panel = new JPanel();
        panel.add(liberarButton);

        JScrollPane scrollPane = new JScrollPane(memoriaPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(panel, "North");
        add(scrollPane, "Center");

        inicializarMemoria();
        actualizarEstadoMemoria();

        setSize(800, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Crear un hilo para la simulación automática
        Thread simulacionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isVisible()) {
                    asignarMemoria();
                    try {
                        // Esperar 2 segundos antes de la próxima simulación
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    actualizarEstadoMemoria();
                }
            }
        });

        simulacionThread.start();
    }

    private void inicializarMemoria() {
        // Inicializar la memoria con bloques libres
    	Random random = new Random();
        int blockSize = 128; // tamanno de cada bloque en bytes
        for (int i = 0; i < MAX_MEMORIA; i += blockSize) {
        	int tiempoDeVida = random.nextInt(5) + 1;
            bloquesMemoria.add(new BloqueMemoria(i, blockSize, false));
            System.out.println(tiempoDeVida + " creacion");
        }
    }

    private void asignarMemoria() {
        // Simulación de asignación de memoria estática
        Random random = new Random();


        for (BloqueMemoria bloque : bloquesMemoria) {
        	// Generar tamanno aleatorio para el nuevo proceso
        	int tamanoProceso = random.nextInt(128) + 1;
            if (!bloque.isAsignado() && bloque.gettamanno() >= tamanoProceso) {
                // Bloque de memoria disponible, asignar al proceso
                Proceso proceso = new Proceso(tamanoProceso, random.nextInt(5)+1);
                bloque.setProceso(proceso);
                bloque.setAsignado(true);
//                break;
            } else if (bloque.getProceso().getTiempoRestante() == 0){
            	bloque.getProceso().setTamano(0);
            	bloque.setAsignado(false);
            } else if (bloque.getProceso().getTiempoRestante() == -1) {
                Proceso proceso = new Proceso(tamanoProceso, random.nextInt(5)+1);
                bloque.setProceso(proceso);
                bloque.setAsignado(true);
            } 
        }
    }

    private void liberarMemoriaAleatoria() {
        // Simulación de liberación de memoria estática aleatoria
        List<BloqueMemoria> bloquesOcupados = new ArrayList<>();

        for (BloqueMemoria bloque : bloquesMemoria) {
            if (bloque.isAsignado()) {
                bloquesOcupados.add(bloque);
            }
        }

        if (!bloquesOcupados.isEmpty()) {
            // Elegir aleatoriamente un bloque ocupado para liberar
            Random random = new Random();
            BloqueMemoria bloqueALiberar = bloquesOcupados.get(random.nextInt(bloquesOcupados.size()));
            bloqueALiberar.setAsignado(false);
            bloqueALiberar.setProceso(null);
        }
    }
    private void actualizarEstadoMemoria() {
        // Actualizar el estado de la memoria en el panel
        memoriaPanel.removeAll();

        for (BloqueMemoria bloque : bloquesMemoria) {
            JPanel bloquePanel = new JPanel();
            bloquePanel.setPreferredSize(new Dimension(80, 80));

            
            // Bloque asignado
            if (bloque.isAsignado()) {
            	
                int tiempoRestante = bloque.getProceso().getTiempoRestante();
                bloque.getProceso().setTiempoRestante(tiempoRestante-1);
                
                System.out.println(tiempoRestante);
            	
                int tamanoOcupado = bloque.getProceso().getTamano();
                int tamanoLibre = bloque.gettamanno() - tamanoOcupado;

                JLabel ocupadoLabel = new JLabel("O: " + tamanoOcupado);
                JLabel libreLabel = new JLabel("L: " + tamanoLibre);

                JPanel ocupadoPanel = new JPanel();
                ocupadoPanel.setBackground(Color.RED);
                ocupadoPanel.setPreferredSize(new Dimension(80, (tamanoOcupado * 80) / bloque.gettamanno()));
                ocupadoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                ocupadoPanel.add(ocupadoLabel);
                
                JPanel librePanel = new JPanel();
                librePanel.setBackground(Color.GREEN);
                librePanel.setPreferredSize(new Dimension(80, (tamanoLibre * 80) / bloque.gettamanno()));
                librePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                librePanel.add(libreLabel);

                bloquePanel.setLayout(new BorderLayout());
                bloquePanel.add(ocupadoPanel, BorderLayout.NORTH);
                bloquePanel.add(librePanel, BorderLayout.SOUTH);
                
            } else {
                // Bloque libre
                JLabel libreLabel = new JLabel("L: " + bloque.gettamanno());

                bloquePanel.setBackground(Color.GREEN);
                bloquePanel.setLayout(new BorderLayout());
                bloquePanel.add(libreLabel, BorderLayout.CENTER);

            }

            memoriaPanel.add(bloquePanel);
        }
        
        System.out.println("qqwe");
        memoriaPanel.revalidate();
        memoriaPanel.repaint();
    }
}
