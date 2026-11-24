package personajes;

//Personaje controlado por el usuario.
//acciones elegidas por consola, no por una estrategia
 
public class Jugador extends Personaje {

    public Jugador(String nombre, int vidaMaxima, int fuerza, int resistencia, int agilidad) {
        super(nombre, vidaMaxima, fuerza, resistencia, agilidad);
    }
}
