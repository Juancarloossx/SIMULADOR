package uptc.edu.co.controller;

public class Proceso {
    private int tamano;
    private int tiempoRestante = 0;
    
    public Proceso(int tamano, int tiempoRestante) {
        this.tamano = tamano;
        this.tiempoRestante = tiempoRestante;
    }

    public void setTamano(int tamano) {
		this.tamano = tamano;
	}

	public int getTiempoRestante() {
		return tiempoRestante;
	}

	public void setTiempoRestante(int tiempoRestante) {
		this.tiempoRestante = tiempoRestante;
	}

	public int getTamano() {
        return tamano;
    }
}
