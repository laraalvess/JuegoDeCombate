package personajes;

import acciones.Accion;
import estrategias.Estrategia;


 //Enemigo controlado por el ordenador
 //Aplica el patron Strategy: cada enemigo mantiene una referencia a una
 //Estrategia que decide la siguiente accion
 //La estrategia se puede sustituir en cualquier momento del combate

public class Enemigo extends Personaje {

    private Estrategia estrategia;

    public Enemigo(String nombre, int vidaMaxima, int fuerza, int resistencia, int agilidad, Estrategia estrategia) {
        super(nombre, vidaMaxima, fuerza, resistencia, agilidad);
        this.estrategia = estrategia;
    }

    public Estrategia getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(Estrategia estrategia) {
        this.estrategia = estrategia;
    }

    
     //Pide a la estrategia que decida la proxima accion contra el oponente indicado
     //Este metodo sera el punto de entrada del Template Method del apartado 6
     //que envolvera esta llamada con pasos comunes a todos los enemigos


  // ---------------------------------------------------------------
    // TEMPLATE METHOD
    // ---------------------------------------------------------------

    /**
     * Algoritmo completo para decidir la siguiente acción del enemigo.
     * Este es el método plantilla: define los pasos en orden y no debe
     * sobreescribirse en las subclases.
     *
     * Pasos:
     *   1. Comprobar si el enemigo puede actuar (estado no paralizado)
     *   2. Elegir la acción según la estrategia  ← paso variable (hook)
     *   3. Registrar la decisión por consola para depuración
     *
     * param oponente el jugador contra el que combate este enemigo
     * return la acción elegida; nunca null
     */
     
    public Accion decidirAccion(Personaje oponente) {
         // Paso 1: si está paralizado no puede hacer nada
        if (!puedeActuar()) {
            return accionParalizado();
        }

        // Paso 2 (hook): cada subclase puede cambiar cómo elige la acción
        Accion accion = elegirAccionSegunEstrategia(oponente);

        // Paso 3: registro de la decisión (común a todos los enemigos)
        registrarDecision(accion);

        return accion;
    }

    /**
     * Paso variable del Template Method.
     * Implementación por defecto: delega en la estrategia actual.
     * Las subclases pueden sobreescribir este método para añadir
     * lógica propia antes o después de consultar la estrategia.
     *
     * param oponente el jugador contra el que combate
     * return acción elegida por la estrategia
     */
    protected Accion elegirAccionSegunEstrategia(Personaje oponente) {
        return estrategia.decidirAccion(this, oponente);
    }

    /**
     * Hook: acción que se realiza cuando el enemigo está paralizado.
     * Por defecto devuelve NoHacerNada. Las subclases pueden sobreescribir.
     */
    protected Accion accionParalizado() {
        return new acciones.NoHacerNada();
    }

    /**
     * Paso común: registra en consola la decisión tomada.
     * No se sobreescribe en subclases.
     */
    private void registrarDecision(Accion accion) {
        // Registro interno (puede conectarse a un logger si se desea)
        // Se deja como hook silencioso para no romper la UI de Combate.java
    }
}

