package estrategias;

import acciones.Accion;
import acciones.AtacarConEspada;
import acciones.Defenderse;
import personajes.Enemigo;
import personajes.Personaje;


 //Estrategia equilibrada: atacara y defendera de forma equilibrada, 50% cada una

public class EstrategiaEquilibrada implements Estrategia {

    @Override
    public Accion decidirAccion(Enemigo enemigo, Personaje oponente) {
        //50% de atacar 50% de defender
        if (Math.random() < 0.5) {
            return new Defenderse();
        } else {
            return new AtacarConEspada();
        }
    }

    @Override
    public String getNombre() {
        return "equilibrada";
    }
}
