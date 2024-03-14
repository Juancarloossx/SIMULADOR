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

public class VentanaPrimerAjuste extends JFrame {

	private static final int MAX_MEMORIA = 500;
	private static final int LIMITE_PROCESOS = 100;
	private List<BloqueMemoria> bloquesMemoria = new ArrayList<>();
	private List<Proceso> procesosEspera = new ArrayList<Proceso>();
	private int procesosCreados = 0;
	private char letraActual = 'A';
	private int memoriaOcupada = 0;
	private int memoriaLibre = MAX_MEMORIA;

	private JPanel memoriaPanel;
	private JScrollPane scrollPane;
	private ScheduledExecutorService scheduler;
	private JButton reiniciarButton, pararButton;
	private JTextField ocupadaTextField;
	private JTextField libreTextField;

	public VentanaPrimerAjuste() {
		inicializarMemoria();
		inicializarInterfaz();
		asignarMemoriaPrimerAjuste();
		actualizarEstadoMemoria();

		setSize(1500, 400);
		setTitle("Mejor Ajuste");
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

	private void asignarMemoriaPrimerAjuste() {
		Random random = new Random();

		if (procesosCreados < LIMITE_PROCESOS) {

			List<BloqueMemoria> listaBloques = bloquesMemoria;
			listaBloques.sort(Comparator.comparingInt(BloqueMemoria::getInicio));

			for (BloqueMemoria bloque : listaBloques) {
				if (!bloque.isAsignado()) {
					if (!procesosEspera.isEmpty()) {
						Proceso proceso = procesosEspera.get(0);
						int tamannoProceso = proceso.getTamano();

						if (bloque.gettamanno() > tamannoProceso) {
							BloqueMemoria nuevoBloque = new BloqueMemoria(bloque.getInicio() + tamannoProceso,
									bloque.gettamanno() - tamannoProceso, false);
							BloqueMemoria bloqueAsignado = new BloqueMemoria(bloque.getInicio(), tamannoProceso, true);

							bloqueAsignado.setProceso(proceso);

							System.out.println(
									"proceso de la lista " + proceso.getNombre() + " tamanno " + proceso.getTamano());
							procesosCreados++;
							memoriaOcupada += tamannoProceso;
							memoriaLibre -= tamannoProceso;

							procesosEspera.remove(0);
							bloquesMemoria.remove(bloque);
							bloquesMemoria.add(bloqueAsignado);
							bloquesMemoria.add(nuevoBloque);
							break;
						} else if (bloque.gettamanno() == tamannoProceso) {
							bloque.setProceso(proceso);
							bloque.setAsignado(true);
							memoriaOcupada += tamannoProceso;
							memoriaLibre -= tamannoProceso;

							procesosCreados++;
							break;
						}
					} else {
						int tamanoProceso = random.nextInt(128) + 1;
						if (bloque.gettamanno() == tamanoProceso) {

							Proceso proceso = new Proceso("Proceso:" + letraActual, tamanoProceso,
									random.nextInt(7) + 4);
							bloque.setProceso(proceso);
							bloque.setAsignado(true);
							memoriaOcupada += tamanoProceso;
							memoriaLibre -= tamanoProceso;

							letraActual++;
							if (letraActual > 'Z') {
								letraActual = 'A';
							}
							procesosCreados++;
							break;
						} else if (tamanoProceso < bloque.gettamanno()) {
							BloqueMemoria nuevoBloque = new BloqueMemoria(bloque.getInicio() + tamanoProceso,
									bloque.gettamanno() - tamanoProceso, false);
							BloqueMemoria bloqueAsignado = new BloqueMemoria(bloque.getInicio(), tamanoProceso, true);

							Proceso proceso = new Proceso("Proceso:" + letraActual, tamanoProceso,
									random.nextInt(7) + 4);
							bloqueAsignado.setProceso(proceso);
							letraActual++;
							if (letraActual > 'Z') {
								letraActual = 'A';
							}
							procesosCreados++;
							memoriaOcupada += tamanoProceso;
							memoriaLibre -= tamanoProceso;

							bloquesMemoria.remove(bloque);
							bloquesMemoria.add(bloqueAsignado);
							bloquesMemoria.add(nuevoBloque);
							break;
						} else {
							Proceso proceso = new Proceso("Proceso:" + letraActual, tamanoProceso,
									random.nextInt(7) + 4);
							procesosEspera.add(proceso);
							
							System.out.println("Proceso " + proceso.getNombre() + " annadido a lista de espera de tamanno " + proceso.getTamano());

							letraActual++;
							if (letraActual > 'Z') {
								letraActual = 'A';
							}
							procesosCreados++;
							break;
						}
					}
				}
			}
		}
	}

	private void asignarMemoriaPeriodicamente() {
		if (!Thread.currentThread().isInterrupted() && isVisible()) {
			liberarMemoria();
			SwingUtilities.invokeLater(this::actualizarEstadoMemoria);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			asignarMemoriaPrimerAjuste();
		}
	}

	private void actualizarEstadoMemoria() {
		memoriaPanel.removeAll();

		int anchoTotal = 0;

		for (BloqueMemoria bloque : bloquesMemoria) {
			JPanel bloquePanel = new JPanel();
			JPanel contenedorBloque = new JPanel();
			JPanel instanciasPanel = new JPanel();

			bloquePanel.setPreferredSize(new Dimension(75, 75));

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
				ocupadoPanel.setPreferredSize(new Dimension(75, (tamanoOcupado * 75) / bloque.gettamanno()));
				ocupadoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				ocupadoPanel.add(ocupadoLabel);

				JPanel librePanel = new JPanel();
				librePanel.setBackground(Color.GREEN);
				librePanel.setPreferredSize(new Dimension(75, (tamanoLibre * 75) / bloque.gettamanno()));
				librePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				librePanel.add(libreLabel);

				instanciasPanel = new JPanel();
				instanciasPanel.setLayout(new BorderLayout());
				instanciasPanel.setPreferredSize(new Dimension(75, 30));
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

		// Actualizar los cuadros de texto de la memoria ocupada y libre
		ocupadaTextField.setText("Memoria Ocupada: " + memoriaOcupada);
		libreTextField.setText("Memoria Libre: " + memoriaLibre);

		memoriaPanel.setPreferredSize(new Dimension(anchoTotal, memoriaPanel.getPreferredSize().height));

		memoriaPanel.revalidate();
		memoriaPanel.repaint();
	}

	private void liberarMemoria() {
		int memoriaOcupadaAnterior = memoriaOcupada; // Guarda el valor anterior de memoria ocupada
		int memoriaLibreAnterior = memoriaLibre; // Guarda el valor anterior de memoria libre

		for (BloqueMemoria bloque : bloquesMemoria) {
			if (bloque.isAsignado() && bloque.getProceso().getTiempoRestante() == 0) {
				int tamanoProceso = bloque.getProceso().getTamano();
				bloque.getProceso().setTamano(0);
				bloque.setAsignado(false);
				memoriaOcupada -= tamanoProceso;
				memoriaLibre += tamanoProceso;
			}
		}

		// Verifica si ha habido cambios en la memoria ocupada y libre
		if (memoriaOcupadaAnterior != memoriaOcupada || memoriaLibreAnterior != memoriaLibre) {
			System.out.println("Memoria ocupada anterior: " + memoriaOcupadaAnterior);
			System.out.println("Memoria libre anterior: " + memoriaLibreAnterior);
			System.out.println("Memoria ocupada actual: " + memoriaOcupada);
			System.out.println("Memoria libre actual: " + memoriaLibre);
		}

		bloquesMemoria.sort(Comparator.comparingInt(BloqueMemoria::getInicio));
		List<BloqueMemoria> nuevosBloques = new ArrayList<>();
		BloqueMemoria bloqueAnterior = bloquesMemoria.get(0);

		for (int i = 1; i < bloquesMemoria.size(); i++) {
			BloqueMemoria bloqueActual = bloquesMemoria.get(i);

			if (!bloqueAnterior.isAsignado() && !bloqueActual.isAsignado()) {
				BloqueMemoria nuevoBloque = new BloqueMemoria(bloqueAnterior.getInicio(),
						bloqueAnterior.gettamanno() + bloqueActual.gettamanno(), false);
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
		asignarMemoriaPrimerAjuste();
		actualizarEstadoMemoria();
		procesosCreados = 0;
		letraActual = 'A';
		memoriaOcupada = 0;
		memoriaLibre = MAX_MEMORIA;
	}
	
	private void pararSimulador() {
		pararButton.setEnabled(false);
		try {
			Thread.sleep(4000);;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pararButton.setEnabled(true);
	}

	private void inicializarInterfaz() {
		memoriaPanel = new JPanel();
		memoriaPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		scrollPane = new JScrollPane(memoriaPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ocupadaTextField = new JTextField("Memoria Ocupada: " + memoriaOcupada, 20);
		ocupadaTextField.setEditable(false);
		libreTextField = new JTextField("Memoria Libre: " + memoriaLibre, 20);
		libreTextField.setEditable(false);
		pararButton = new JButton("Esperar");
		pararButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pararSimulador();
			}
		});
		
		bottomPanel.add(ocupadaTextField);
		bottomPanel.add(libreTextField);
		bottomPanel.add(pararButton);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		new VentanaPrimerAjuste();
	}
}
