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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VentanaPeorAjuste extends JFrame {

    private static final int MAX_MEMORIA = 1024;
    private static final int LIMITE_PROCESOS = 100;
    private List<BloqueMemoria> bloquesMemoria = new ArrayList<>();
    private int procesosCreados = 0;
    private char letraActual = 'A';

    private JPanel memoriaPanel;
    private JScrollPane scrollPane;
    private ScheduledExecutorService scheduler;
    private JButton reiniciarButton;

    public VentanaPeorAjuste() {
        inicializarMemoria();
        inicializarInterfaz();
        asignarMemoriaPeorAjuste();
        actualizarEstadoMemoria();

        setSize(1500, 350);
        setTitle("Peor Ajuste");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::asignarMemoriaPeriodicamente, 0, 2, TimeUnit.SECONDS);

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

    private void asignarMemoriaPeorAjuste() {
        Random random = new Random();

        if (procesosCreados < LIMITE_PROCESOS) {
            int tamanoProceso = random.nextInt(128) + 1;

            List<BloqueMemoria> listaBloques = bloquesMemoria;
            listaBloques.sort(Comparator.comparingInt(BloqueMemoria::gettamanno).reversed()); 
            
            for (BloqueMemoria bloque : listaBloques) {
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
                    }
                    else {
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

    private void asignarMemoriaPeriodicamente() {
        if (!Thread.currentThread().isInterrupted() && isVisible()) {
            liberarMemoria();
            SwingUtilities.invokeLater(() -> {
            	actualizarEstadoMemoria();
            });
            try {
            	Thread.sleep(2000);
            } catch (InterruptedException e) {
            	// TODO Auto-generated catch block
            	e.printStackTrace();
            }
            asignarMemoriaPeorAjuste();
        }
    }

    private void actualizarEstadoMemoria() {
        memoriaPanel.removeAll();

        int anchoTotal = 0;

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

            memoriaPanel.add(contenedorBloque);

            anchoTotal += bloquePanel.getPreferredSize().width;
        }

        // Agregar botón "Reiniciar"
        reiniciarButton = new JButton("Reiniciar");
        reiniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarVentana();
                VentanaPeorAjuste.this.dispose();  
                new VentanaPeorAjuste();  
            }
        });
        memoriaPanel.add(reiniciarButton);

        memoriaPanel.setPreferredSize(new Dimension(anchoTotal, memoriaPanel.getPreferredSize().height));

        memoriaPanel.revalidate();
        memoriaPanel.repaint();
    }

    private void liberarMemoria() {

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
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }

        inicializarMemoria();
        asignarMemoriaPeorAjuste();
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
        new VentanaPeorAjuste();
    }
}
