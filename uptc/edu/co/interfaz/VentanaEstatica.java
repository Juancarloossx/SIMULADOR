package uptc.edu.co.interfaz;

import uptc.edu.co.controller.BloqueMemoria;
import uptc.edu.co.controller.Proceso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaEstatica extends JFrame {

    private static final int MAX_MEMORIA = 1024; // tamanno máximo de memoria en bytes
    private static java.util.List<BloqueMemoria> bloquesMemoria = new ArrayList<>();
    private static java.util.List<Proceso> procesos = new ArrayList<>();
    private JPanel memoriaPanel;

    private static final int LIMITE_PROCESOS = 10; // Establece el límite de procesos a 26 (de la A a la Z)
    private int procesosCreados = 0; // Contador de procesos creados

    Thread simulacionThread;

    public VentanaEstatica() {

        memoriaPanel = new JPanel();

        JPanel panel = new JPanel();


        JScrollPane scrollPane = new JScrollPane(memoriaPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(panel, "North");
        add(scrollPane, "Center");

        inicializarMemoria();
        actualizarEstadoMemoria();

        setSize(800, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setTitle("ASIGNACION DE MEMORIA ESTATICA");

        // Crear un hilo para la simulación automática
        simulacionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted() && isVisible()) {
                        liberarMemoria();
                        Thread.sleep(2000);
                        asignarMemoria();
                        actualizarEstadoMemoria();
                    }
                } catch (InterruptedException ex) {
                    // Restaura la bandera de interrupción y sale del hilo
                    Thread.currentThread().interrupt();
                }

            }
        });

        simulacionThread.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                reiniciarVentana();
            }
        });

    }



    private void inicializarMemoria() {
        // Inicializar la memoria con bloques libres
        Random random = new Random();
        int blockSize = 128; // tamanno de cada bloque en bytes
        for (int i = 0; i < MAX_MEMORIA; i += blockSize) {
            //int tiempoDeVida = random.nextInt(5) + 1;
            bloquesMemoria.add(new BloqueMemoria(i, blockSize, false));
        }
    }


    private void actualizarEstadoMemoria() {

            // Actualizar el estado de la memoria en el panel
            memoriaPanel.removeAll();

            for (int i=0;i<bloquesMemoria.size();i++) {
                BloqueMemoria bloque = bloquesMemoria.get(i);

                JPanel bloquePanel = new JPanel();
                JPanel contenedorBloque = new JPanel();
                JPanel instanciasPanel = new JPanel();

                contenedorBloque.setPreferredSize(new Dimension(80, 120));
                bloquePanel.setPreferredSize(new Dimension(80, 80));

                // Bloque asignado
                if (bloque.isAsignado()) {

                    int tiempoRestante = bloque.getProceso().getTiempoRestante();
                    bloque.getProceso().setTiempoRestante(tiempoRestante - 1);

                    int tamanoOcupado = bloque.getProceso().getTamano();
                    int tamanoLibre = bloque.gettamanno() - tamanoOcupado;

                    String nombre = bloque.getProceso().getNombre();

                    JLabel ocupadoLabel = new JLabel("O: " + tamanoOcupado);
                    JLabel libreLabel = new JLabel("L: " + tamanoLibre);
                    JLabel instanciasLabel = new JLabel("instancias: " + tiempoRestante); // Nueva etiqueta
                    JLabel nombreProcesoLabel = new JLabel(nombre);


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

                    instanciasPanel = new JPanel(); // Nuevo panel para la etiqueta de tiempo restante
                    instanciasPanel.setLayout(new BorderLayout());
                    instanciasPanel.setPreferredSize(new Dimension(80, 30));
                    instanciasPanel.add(instanciasLabel,BorderLayout.NORTH);
                    instanciasPanel.add(nombreProcesoLabel,BorderLayout.SOUTH);

                    bloquePanel.setLayout(new BorderLayout());
                    bloquePanel.add(ocupadoPanel, BorderLayout.NORTH);
                    bloquePanel.add(librePanel, BorderLayout.CENTER);

                } else {
                    // Bloque libre
                    JLabel libreLabel = new JLabel("L: " + bloque.gettamanno());

                    bloquePanel.setBackground(Color.GREEN);
                    bloquePanel.setLayout(new BorderLayout());
                    bloquePanel.add(libreLabel, BorderLayout.CENTER);

                }
                contenedorBloque.setLayout(new BorderLayout());
                contenedorBloque.add(bloquePanel,BorderLayout.NORTH);
                contenedorBloque.add(instanciasPanel,BorderLayout.SOUTH);

                memoriaPanel.add(contenedorBloque);
            }

            memoriaPanel.revalidate();
            memoriaPanel.repaint();


    }

    private void liberarMemoria() {
        int tamanoProceso = (int) (Math.random()*128)+1;

        for (int i = 0; i < bloquesMemoria.size(); i++) {
            BloqueMemoria bloque = bloquesMemoria.get(i);
            if (bloque.isAsignado()) {
                if (bloque.getProceso().getTiempoRestante() == 0){
                    bloque.getProceso().setTamano(0);
                    bloque.setAsignado(false);

                } /*else if (bloque.getProceso().getTiempoRestante() == -1) {
                    Proceso proceso = new Proceso("Proceso" + i,tamanoProceso,(int) (Math.random()*7)+4);
                    bloque.setProceso(proceso);
                    bloque.setAsignado(true);
                }*/
            }
        }
    }

    private char letraActual = 'A';
    private void asignarMemoria() {
        // Simulación de asignación de memoria est

        Random random = new Random();

        if (procesosCreados<LIMITE_PROCESOS) {
            for (int i = 0; i < bloquesMemoria.size(); i++) {
            BloqueMemoria bloque = bloquesMemoria.get(i);
            // Generar tamanno aleatorio para el nuevo proceso
            int tamanoProceso = random.nextInt(128) + 1;
                if (!bloque.isAsignado() && bloque.gettamanno() >= tamanoProceso) {
                // Bloque de memoria disponible, asignar al proceso
                    Proceso proceso = new Proceso("Proceso:" + letraActual, tamanoProceso, random.nextInt(7) + 4);
                    bloque.setProceso(proceso);
                    bloque.setAsignado(true);

                    letraActual++;
                    if (letraActual > 'Z') {
                    letraActual = 'A'; // Reinicia a 'A' cuando llega a 'Z'
                    }
                    procesosCreados++;
                    break;
                }
            }
        }
    }

    public void reiniciarVentana() {
        // Detén el hilo de simulación si está en ejecución
        if (simulacionThread != null && simulacionThread.isAlive()) {
            simulacionThread.interrupt();
        }

        // Limpia las listas y reinicializa la memoria
        bloquesMemoria.clear();
        procesos.clear();
        actualizarEstadoMemoria();

    }


}
