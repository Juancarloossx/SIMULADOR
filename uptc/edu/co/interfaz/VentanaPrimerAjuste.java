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
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class VentanaPrimerAjuste extends JFrame {

    private static final int MAX_MEMORIA = 1024;
    private static final int LIMITE_PROCESOS = 10;
    private List<BloqueMemoria> bloquesMemoria = new ArrayList<>();
    private int procesosCreados = 0;
    private char letraActual = 'A';

    private JPanel memoriaPanel;
    private JScrollPane scrollPane;
    private Thread simulacionThread;

    public VentanaPrimerAjuste() {
        inicializarMemoria();
        inicializarInterfaz();
        asignarMemoriaPrimerAjuste();
        actualizarEstadoMemoria();

        setSize(800, 150); // Tamaño ajustado
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        simulacionThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted() && isVisible()) {
                    liberarMemoria();
                    Thread.sleep(2000);
                    asignarMemoriaPrimerAjuste();
                    actualizarEstadoMemoria();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
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
        bloquesMemoria.clear();
        bloquesMemoria.add(new BloqueMemoria(0, MAX_MEMORIA, false));
    }

    private void asignarMemoriaPrimerAjuste() {
        Random random = new Random();

        if (procesosCreados < LIMITE_PROCESOS) {
            int tamanoProceso = random.nextInt(128) + 1;

            bloquesMemoria.sort(Comparator.comparingInt(BloqueMemoria::gettamanno));

            for (BloqueMemoria bloque : bloquesMemoria) {
                if (!bloque.isAsignado() && bloque.gettamanno() >= tamanoProceso) {
                    if (bloque.gettamanno() == tamanoProceso) {
                        Proceso proceso = new Proceso("Proceso:" + letraActual, tamanoProceso, random.nextInt(7) + 4);
                        bloque.setProceso(proceso);
                        bloque.setAsignado(true);

                        letraActual++;
                        if (letraActual > 'Z') {
                            letraActual = 'A';
                        }
                        procesosCreados++;
                        break;
                    } else {
                        BloqueMemoria nuevoBloque = new BloqueMemoria(
                                bloque.getInicio() + tamanoProceso,
                                bloque.gettamanno() - tamanoProceso,
                                false
                        );
                        BloqueMemoria bloqueAsignado = new BloqueMemoria(bloque.getInicio(), tamanoProceso, true);

                        Proceso proceso = new Proceso("Proceso:" + letraActual, tamanoProceso, random.nextInt(7) + 4);
                        bloqueAsignado.setProceso(proceso);
                        letraActual++;
                        if (letraActual > 'Z') {
                            letraActual = 'A';
                        }
                        procesosCreados++;

                        bloquesMemoria.remove(bloque);
                        bloquesMemoria.add(bloqueAsignado);
                        bloquesMemoria.add(nuevoBloque);
                        break;
                    }
                }
            }
        }
    }

    private void actualizarEstadoMemoria() {
        memoriaPanel.removeAll();

        int anchoTotal = 0; // Variable para rastrear el ancho total de los bloques

        for (BloqueMemoria bloque : bloquesMemoria) {
            JPanel bloquePanel = new JPanel();
            JPanel contenedorBloque = new JPanel();
            JPanel instanciasPanel = new JPanel();

            bloquePanel.setPreferredSize(new Dimension(150, 150));

            if (bloque.isAsignado()) {
                int tiempoRestante = bloque.getProceso().getTiempoRestante();
                bloque.getProceso().setTiempoRestante(tiempoRestante - 1);

                int tamanoOcupado = bloque.getProceso().getTamano();
                int tamanoLibre = bloque.gettamanno() - tamanoOcupado;

                String nombre = bloque.getProceso().getNombre();

                JLabel ocupadoLabel = new JLabel("O: " + tamanoOcupado);
                JLabel libreLabel = new JLabel("L: " + tamanoLibre);
                JLabel instanciasLabel = new JLabel("instancias: " + tiempoRestante);
                JLabel nombreProcesoLabel = new JLabel(nombre);

                JLabel tamanoProcesoLabel = new JLabel("Tamaño Proceso: " + tamanoOcupado);
                JLabel tamanoBloqueLabel = new JLabel("Tamaño Bloque: " + bloque.gettamanno());

                JPanel ocupadoPanel = new JPanel();
                ocupadoPanel.setBackground(Color.RED);
                ocupadoPanel.setPreferredSize(new Dimension(150, (tamanoOcupado * 150) / bloque.gettamanno()));
                ocupadoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                ocupadoPanel.add(ocupadoLabel);

                JPanel librePanel = new JPanel();
                librePanel.setBackground(Color.GREEN);
                librePanel.setPreferredSize(new Dimension(150, (tamanoLibre * 150) / bloque.gettamanno()));
                librePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                librePanel.add(libreLabel);

                instanciasPanel = new JPanel();
                instanciasPanel.setLayout(new BorderLayout());
                instanciasPanel.setPreferredSize(new Dimension(150, 30));
                instanciasPanel.add(instanciasLabel, BorderLayout.NORTH);
                instanciasPanel.add(nombreProcesoLabel, BorderLayout.SOUTH);

                bloquePanel.setLayout(new BorderLayout());
                bloquePanel.add(ocupadoPanel, BorderLayout.NORTH);
                bloquePanel.add(librePanel, BorderLayout.CENTER);

                JPanel tamanosPanel = new JPanel();
                tamanosPanel.setLayout(new GridLayout(2, 1));
                tamanosPanel.add(tamanoProcesoLabel);
                tamanosPanel.add(tamanoBloqueLabel);

                contenedorBloque.add(tamanosPanel, BorderLayout.SOUTH);
            } else {
                JLabel libreLabel = new JLabel("L: " + bloque.gettamanno());
                bloquePanel.setBackground(Color.GREEN);
                bloquePanel.setLayout(new BorderLayout());
                bloquePanel.add(libreLabel, BorderLayout.CENTER);
            }

            contenedorBloque.setLayout(new BorderLayout());
            contenedorBloque.add(bloquePanel, BorderLayout.NORTH);
            contenedorBloque.add(instanciasPanel, BorderLayout.SOUTH);

            // Agregar el bloque al panel
            memoriaPanel.add(contenedorBloque);

            // Actualizar el ancho total con el ancho del bloque actual
            anchoTotal += bloquePanel.getPreferredSize().width;
        }

        // Establecer el tamaño del panel de memoria para permitir desplazamiento horizontal
        memoriaPanel.setPreferredSize(new Dimension(anchoTotal, memoriaPanel.getPreferredSize().height));

        // Actualizar la interfaz
        memoriaPanel.revalidate();
        memoriaPanel.repaint();
    }

    private void liberarMemoria() {
        int tamanoProceso = (int) (Math.random() * 128) + 1;

        for (BloqueMemoria bloque : bloquesMemoria) {
            if (bloque.isAsignado() && bloque.getProceso().getTiempoRestante() == 0) {
                bloque.getProceso().setTamano(0);
                bloque.setAsignado(false);
            }
        }

        bloquesMemoria.sort(Comparator.comparingInt(BloqueMemoria::getInicio));
        List<BloqueMemoria> nuevosBloques = new ArrayList<>();
        BloqueMemoria bloqueAnterior = bloquesMemoria.get(0);

        for (int i = 1; i < bloquesMemoria.size(); i++) {
            BloqueMemoria bloqueActual = bloquesMemoria.get(i);

            if (!bloqueAnterior.isAsignado() && !bloqueActual.isAsignado()) {
                BloqueMemoria nuevoBloque = new BloqueMemoria(
                        bloqueAnterior.getInicio(),
                        bloqueAnterior.gettamanno() + bloqueActual.gettamanno(),
                        false
                );
                bloqueAnterior = nuevoBloque;
            } else {
                nuevosBloques.add(bloqueAnterior);
                bloqueAnterior = bloqueActual;
            }
        }

        nuevosBloques.add(bloqueAnterior);
        bloquesMemoria = nuevosBloques;
    }

    private void reiniciarVentana() {
        if (simulacionThread != null && simulacionThread.isAlive()) {
            simulacionThread.interrupt();
        }

        inicializarMemoria();
        asignarMemoriaPrimerAjuste();
        actualizarEstadoMemoria();
        procesosCreados = 0;
        letraActual = 'A';
    }

    private void inicializarInterfaz() {
        memoriaPanel = new JPanel();
        memoriaPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        scrollPane = new JScrollPane(memoriaPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new VentanaPrimerAjuste();
    }
}
