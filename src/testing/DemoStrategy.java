package testing;

import acciones.Accion; 
import estrategias.EstrategiaAgresiva;
import estrategias.EstrategiaDefensiva;
import estrategias.EstrategiaEquilibrada;
import personajes.Enemigo;
import personajes.Jugador;

//testing

public class DemoStrategy {

    public static void main(String[] args) {
    	
        Jugador jugador = new Jugador("Personaje19", 100, 15, 10, 12);
        Enemigo enemigo = new Enemigo("Enemigo1", 100, 8, 6, 14, new EstrategiaAgresiva());

        mostrarDecision(enemigo, jugador);

        enemigo.recibirDaño(70);
        enemigo.setEstrategia(new EstrategiaDefensiva());
        mostrarDecision(enemigo, jugador);

        enemigo.setEstrategia(new EstrategiaEquilibrada());
        mostrarDecision(enemigo, jugador);
    }
    
    private static void mostrarDecision(Enemigo enemigo, Jugador jugador) {
        Accion accion = enemigo.decidirAccion(jugador);
        System.out.println(
                enemigo.getNombre()
                        + " (vida " + enemigo.getVida() + "/" + enemigo.getVidaMaxima() + ")"
                        + " con estrategia " + enemigo.getEstrategia().getNombre()
                        + " decide: " + accion.getDescripcion());
    }
    
}
