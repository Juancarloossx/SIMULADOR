package uptc.edu.co.controller;

public class BloqueMemoria {
    private int inicio;
    private int tamaño;
    private boolean asignado;
    private Proceso proceso;

    public BloqueMemoria(int inicio, int tamaño, boolean asignado) {
        this.inicio = inicio;
        this.tamaño = tamaño;
        this.asignado = asignado;
    }

    public int getInicio() {
        return inicio;
    }

    public int getTamaño() {
        return tamaño;
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
}
