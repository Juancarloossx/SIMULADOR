package uptc.edu.co.controller;

public class Proceso {
    private int tamano;
    private int tiempoRestante = 0;

    private String nombre;
    
    public Proceso(String nombre, int tamano, int tiempoRestante) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.tiempoRestante = tiempoRestante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
