package uptc.edu.co.controller;

public class BloqueMemoria {
    private int inicio;
    private int tamanno;
    private boolean asignado;
    private Proceso proceso;
    private int tiempoRestante;

    public BloqueMemoria(int inicio, int tamanno, boolean asignado, int tiempoRestante) {
        this.inicio = inicio;
        this.tamanno = tamanno;
        this.asignado = asignado;
        this.tiempoRestante = tiempoRestante;
    }

    public int getInicio() {
        return inicio;
    }

    public int gettamanno() {
        return tamanno;
    }

    public boolean isAsignado() {
        return asignado;
    }

    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

	public int getTiempoRestante() {
		return tiempoRestante;
	}

	public void setTiempoRestante(int tiempoRestante) {
		this.tiempoRestante = tiempoRestante;
	}
    
}
