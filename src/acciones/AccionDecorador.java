package acciones;

import personajes.Personaje;

// Clase base del patron Decorator para acciones de combate
// Envuelve otra Accion y delega todo a ella; las subclases solo
// sobreescriben los metodos que quieran modificar

public class AccionDecorador implements Accion {

    // La accion que este decorador envuelve (puede ser basica u otro decorador)
    protected Accion accionDecorada;

    public AccionDecorador(Accion accion) {
        this.accionDecorada = accion;
    }

    public int getDañoBase() {
        return accionDecorada.getDañoBase();
    }

    public String getDescripcion() {
        return accionDecorada.getDescripcion();
    }

    public void ejecutar(Personaje ejecutor, Personaje objetivo) {
        accionDecorada.ejecutar(ejecutor, objetivo);
    }
}
