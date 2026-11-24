package estrategias;

import acciones.Accion;
import personajes.Enemigo;
import personajes.Personaje;

//el enemigo ejecuta una accion u otra dependiendo de la estrategia usada,
public interface Estrategia {


     //parametro enemigo que aplica la estrategia
     //parametro oponente, personaje contra el que combate (el jugador)
     //devuelve la accion elegida; nunca {@code null}
    Accion decidirAccion(Enemigo enemigo, Personaje oponente);

    //Nombre de la estrategia, para mostrarlo por consola
    String getNombre();
}
