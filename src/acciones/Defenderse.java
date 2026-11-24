package acciones;

import personajes.Personaje;

//accion defensiva: el personaje no ataca en ese turno
public class Defenderse implements Accion {

    @Override
    public String getDescripcion() {
        return "defenderse";
    }

    @Override
    public int getDañoBase() {
        return 0;
    }

    @Override
    public void ejecutar(Personaje ejecutor, Personaje objetivo) {
        // no hace daño, el efecto defensivo reduce el daño de proximo ataque recibido
    }
}
