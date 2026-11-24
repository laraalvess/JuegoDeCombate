package juego;

import fabricas.FabricaEnemigos;
import fabricas.FabricaEnemigosBosque;
import fabricas.FabricaEnemigosMazmorra;
import fabricas.FabricaEnemigosVolcan;
import personajes.Enemigo;
import personajes.Jugador;

public class GameController {

    private final ConsolaVentana consola;
    private Jugador jugador;
    private int puntos;
    private int ronda;

    /**
     * Crea el controlador con la ventana de consola ya inicializada.
     * param consola ventana Swing donde se muestra el juego
     */
    public GameController(ConsolaVentana consola) {
        this.consola = consola;
        this.puntos  = 0;
        this.ronda   = 1;
    }

    // ---------------------------------------------------------------
    // API pública de la Facade
    // ---------------------------------------------------------------

    /**
     * Pide el nombre al jugador y crea su personaje con atributos iniciales.
     * Oculta la construcción del objeto Jugador y la interacción con la consola.
     */
    public void iniciarJuego() {
        consola.println("========================================");
        consola.println("          JUEGO DE COMBATE");
        consola.println("========================================");
        consola.print("Nombre de tu personaje: ");
        String nombre = consola.readLine().trim();
        if (nombre.isEmpty()) nombre = "Heroe";

        jugador = new Jugador(nombre, 100, 15, 10, 12);
    }

    /**
     * Ejecuta el bucle principal del juego: ronda tras ronda hasta que
     * el jugador pierde o cierra la aplicación.
     * Oculta toda la lógica de progresión, selección de fábricas y combates.
     */
    public void ejecutarBucleJuego() {
        while (jugador.estaVivo()) {
            boolean gano = ejecutarRonda();
            if (!gano) break;
        }
        mostrarPantallaFinal();
    }

    /**
     * Devuelve los puntos acumulados por el jugador.
     */
    public int getPuntos() {
        return puntos;
    }

    // ---------------------------------------------------------------
    // Métodos privados: subsistema oculto por la Facade
    // ---------------------------------------------------------------

    /**
     * Ejecuta una ronda completa: elige bioma, genera enemigo, lanza combate
     * y gestiona el resultado (curación, puntos, avance de ronda).
     * return true si el jugador ganó el combate, false si perdió
     */
    private boolean ejecutarRonda() {
        FabricaEnemigos fabrica = seleccionarFabrica(ronda);
        Enemigo enemigo = generarEnemigo(fabrica, ronda);

        consola.println("\n=== RONDA " + ronda + " — " + fabrica.getNombreBioma() + " ===");
        consola.println("Aparece: " + enemigo.getNombre());

        Combate combate = new Combate(jugador, enemigo, consola);
        boolean victoria = combate.iniciar();

        if (victoria) {
            puntos += 100 * ronda;
            consola.println("Puntos: " + puntos);
            recuperarVidaEntreRondas();
            consola.println("\nPulsa Enter para continuar...");
            consola.readLine();
            ronda++;
        }

        return victoria;
    }

    /**
     * Aplica la curación entre rondas y lo informa por pantalla.
     * Encapsula la lógica de recuperación para que no esté dispersa en Main.
     */
    private void recuperarVidaEntreRondas() {
        int curacion = 30;
        jugador.curar(curacion);
        consola.println(jugador.getNombre() + " recupera " + curacion + " puntos de vida.");
    }

    /**
     * Muestra la pantalla de fin de juego con los puntos obtenidos.
     */
    private void mostrarPantallaFinal() {
        consola.println("\n========================================");
        consola.println("           FIN DEL JUEGO");
        consola.println("   Puntos totales: " + puntos);
        consola.println("========================================");
    }

    /**
     * Devuelve la fábrica de enemigos correspondiente a la ronda actual.
     * Progresión: Bosque (rondas 1-3) → Mazmorra (4-6) → Volcán (7+).
     * param ronda número de ronda actual
     * return fábrica del bioma correspondiente
     */
    private FabricaEnemigos seleccionarFabrica(int ronda) {
        if (ronda <= 3) return new FabricaEnemigosBosque();
        if (ronda <= 6) return new FabricaEnemigosMazmorra();
        return new FabricaEnemigosVolcan();
    }

    /**
     * Genera el enemigo de la ronda rotando entre los tres tipos disponibles:
     * Guerrero → Mago → Bestia → Guerrero...
     * param fabrica fábrica del bioma actual
     * param ronda   número de ronda (determina el tipo y los stats)
     * return enemigo generado
     */
    private Enemigo generarEnemigo(FabricaEnemigos fabrica, int ronda) {
        int tipo = (ronda - 1) % 3;
        switch (tipo) {
            case 0:  return fabrica.crearGuerrero(ronda);
            case 1:  return fabrica.crearMago(ronda);
            default: return fabrica.crearBestia(ronda);
        }
    }
}
