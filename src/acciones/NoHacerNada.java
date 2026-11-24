package acciones;

import personajes.Personaje;

// pasar turno
public class NoHacerNada implements Accion {

    @Override
    public String getDescripcion() {
        return "no hacer nada";
    }

    @Override
    public int getDañoBase() {
        return 0;
    }

    @Override
    public void ejecutar(Personaje ejecutor, Personaje objetivo) {
        //no hace nada
    }
}
