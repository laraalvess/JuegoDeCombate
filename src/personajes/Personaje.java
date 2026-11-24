package personajes;

import estado.EstadoActivo;
import estado.EstadoHerido;
import estado.EstadoParalizado;
import estado.EstadoPersonaje;

public abstract class Personaje {

    protected String nombre;
    protected int vida;
    protected int vidaMaxima;
    protected int fuerza;
    protected int resistencia;
    protected int agilidad;

    private EstadoPersonaje estadoActual = new EstadoActivo();

    protected Personaje(String nombre, int vidaMaxima, int fuerza, int resistencia, int agilidad) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
        this.fuerza = fuerza;
        this.resistencia = resistencia;
        this.agilidad = agilidad;
    }

    public String getNombre(){
        return nombre;
    }

    public int getVida(){
        return vida;
    }

    public int getVidaMaxima(){
        return vidaMaxima;
    }

    public int getFuerza(){
        return fuerza;
    }

    public int getResistencia(){
        return resistencia;
    }

    public int getAgilidad(){
        return agilidad;
    }

    public EstadoPersonaje getEstado() {
        return estadoActual;
    }

    public boolean puedeActuar() {
        return estadoActual.puedeActuar();
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public void recibirDaño(int cantidad) {
        vida = Math.max(0, vida - cantidad);
        actualizarEstado();
    }

    public void curar(int cantidad) {
        vida = Math.min(vidaMaxima, vida + cantidad);
        actualizarEstado();
    }

    public double porcentajeVida() {
        return vidaMaxima == 0 ? 0.0 : (double) vida / vidaMaxima;
    }

    private void actualizarEstado() {
        double pct = porcentajeVida();
        if (pct >= 0.6) {
            estadoActual = new EstadoActivo();
        } else if (pct >= 0.4) {
            estadoActual = new EstadoHerido();
        } else {
            estadoActual = new EstadoParalizado();
        }
    }
}
