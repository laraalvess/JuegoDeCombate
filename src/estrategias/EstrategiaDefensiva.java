package estrategias;

import acciones.Accion;
import acciones.AtacarConEspada;
import acciones.Defenderse;
import personajes.Enemigo;
import personajes.Personaje;


 //Estrategia defensiva: si el enemigo esta debajo de cierto % de vida
 //se defiende para recibit menos daño, en caso contrario ataca

public class EstrategiaDefensiva implements Estrategia {

    private static final double UMBRAL_VIDA_BAJA = 0.20;

    @Override
    public Accion decidirAccion(Enemigo enemigo, Personaje oponente) {
        if (enemigo.porcentajeVida() < UMBRAL_VIDA_BAJA) {
            return new Defenderse();
        }
        return new AtacarConEspada();
    }

    @Override
    public String getNombre() {
        return "defensiva";
    }
}
