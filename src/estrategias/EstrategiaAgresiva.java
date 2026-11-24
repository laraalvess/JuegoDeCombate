package estrategias;

import acciones.Accion;
import acciones.AtacarConEspada;
import personajes.Enemigo;
import personajes.Personaje;

//Estrategia agresiva: ataca siempre, sin tener en cuenta su propia vida. Busca hacer el maximo daño posible
public class EstrategiaAgresiva implements Estrategia {

    @Override
    public Accion decidirAccion(Enemigo enemigo, Personaje oponente) {
        return new AtacarConEspada();
    }

    @Override
    public String getNombre() {
        return "agresiva";
    }
}
