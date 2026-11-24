package juego;

import estado.EstadoPersonaje;
import acciones.Accion;
import acciones.AtacarConEspada;
import acciones.DecoradorAtaquePoderoso;
import acciones.DecoradorAtaqueVenenoso;
import acciones.Defenderse;
import acciones.NoHacerNada;
import estrategias.EstrategiaDefensiva;
import estrategias.EstrategiaEquilibrada;
import personajes.Enemigo;
import personajes.Jugador;

import java.util.ArrayList;
import java.util.List;


//Logica del combate


public class Combate {

    private static final int MAX_LOG   = 6;
    private static final int BOX_INNER = 70;

    private static final String ROJO   = "[31m";
    private static final String BLANCO = "[37m";
    private static final String RESET  = "[0m";

    private final Jugador jugador;
    private final Enemigo enemigo;
    private final ConsolaVentana consola;

     // Singleton: una sola instancia del calculador compartida por todo el sistema
    private final CalculadorCombate calculador = CalculadorCombate.getInstancia();

    private final List<String> log = new ArrayList<>();

    private boolean jugadorDefendiendo = false;
    private boolean enemigoDefendiendo = false;
    private int venenoJugadorTurnos = 0;
    private int venenoEnemigoTurnos = 0;

    public Combate(Jugador jugador, Enemigo enemigo, ConsolaVentana consola) {
        this.jugador = jugador;
        this.enemigo = enemigo;
        this.consola = consola;
    }

    // ejecuta el combate completo, devuelve true si el jugador gana
    public boolean iniciar() {
        boolean turnoJugador;

        if (jugador.getAgilidad() > enemigo.getAgilidad()) {
            registrar("¡" + nJugador() + " es mas ágil, ataca primero!");
            turnoJugador = true;
        } else {
            registrar("¡" + nEnemigo() + " es mas ágil, ataca primero!");
            turnoJugador = false;
        }

        while (jugador.estaVivo() && enemigo.estaVivo()) {
            if (turnoJugador) {
                dibujarUI(true);
                turnoJugador();
            } else {
                dibujarUI(false);
                turnoEnemigo();
                pausa(900);
            }
            turnoJugador = !turnoJugador;
        }

        dibujarUI(false);

        if (jugador.estaVivo()) {
            consola.println("\n¡Has derrotado a " + nEnemigo() + "!");
            return true;
        } else {
            consola.println("\n" + nJugador() + " ha sido derrotado...");
            return false;
        }
    }

    private void dibujarUI(boolean mostrarMenu) {
        consola.limpiar();

        String lineaVacia = "║" + " ".repeat(BOX_INNER + 2);

        consola.println("╔" + "═".repeat(BOX_INNER + 2) + "╗");
        consola.println(fila("JUEGO DE COMBATE"));
        consola.println(fila("Enemigo: " + nEnemigo() + "  [" + enemigo.getEstrategia().getNombre() + "]"));
        consola.println("╠" + "═".repeat(BOX_INNER + 2) + "╣");
        consola.println("║ " + barraVida(jugador.getNombre(), BLANCO, jugador.getVida(), jugador.getVidaMaxima(), jugador.getEstado().getNombre(), venenoJugadorTurnos));
        consola.println("║ " + barraVida(enemigo.getNombre(), ROJO,   enemigo.getVida(), enemigo.getVidaMaxima(), enemigo.getEstado().getNombre(), venenoEnemigoTurnos));
        consola.println("╠" + "═".repeat(BOX_INNER + 2) + "╣");

        List<String> ultimos = log.size() > MAX_LOG ? log.subList(log.size() - MAX_LOG, log.size()) : log;
        for (int i = 0; i < MAX_LOG; i++) {
            consola.println(i < ultimos.size() ? fila(ultimos.get(i)) : lineaVacia);
        }

        consola.println("╠" + "═".repeat(BOX_INNER + 2) + "╣");

        if (mostrarMenu) {
            consola.println(fila("Tu turno. Elige una accion:"));
            consola.println(fila("  1. Atacar con espada"));
            consola.println(fila("  2. Atacar con espada poderosa  (+" + DecoradorAtaquePoderoso.BONUS
                    + " si fuerza ≥ " + DecoradorAtaquePoderoso.UMBRAL_FUERZA + ")"));
            consola.println(fila("  3. Atacar con espada venenosa  (veneno " + DecoradorAtaqueVenenoso.TURNOS
                    + " turnos · " + DecoradorAtaqueVenenoso.DAÑO_VENENO + " daño/turno)"));
            consola.println(fila("  4. Defenderse"));
            consola.println(fila("  5. No hacer nada"));
            consola.println("╚" + "═".repeat(BOX_INNER + 2) + "╝");
        } else {
            consola.println(fila("Turno del enemigo..."));
            consola.println("╚" + "═".repeat(BOX_INNER + 2) + "╝");
        }
    }

    // formatea una fila del marco calculando el padding sobre la longitud visible (sin ANSI)
    private String fila(String contenido) {
        String sinAnsi = contenido.replaceAll("\\[\\d+m", "");
        if (sinAnsi.length() > BOX_INNER) {
            contenido = sinAnsi.substring(0, BOX_INNER);
            sinAnsi   = contenido;
        }
        return "║ " + contenido + " ".repeat(BOX_INNER - sinAnsi.length());
    }

    // barra de vida con nombre coloreado, estado actual e indicador de veneno si activo
    private String barraVida(String nombre, String color, int vida, int vidaMaxima, String estado, int venenoTurnos) {
        int ancho  = 12;
        int llenos = (int) Math.round((double) vida / vidaMaxima * ancho);
        String barra = "█".repeat(llenos) + "░".repeat(ancho - llenos);
        int relleno = Math.max(0, 10 - nombre.length());
        String veneno = venenoTurnos > 0 ? " · ☠ " + venenoTurnos : "";
        return color + nombre + RESET + " ".repeat(relleno) + " [" + barra + "] " + vida + "/" + vidaMaxima + " · " + estado + veneno;
    }

    private String nJugador() { return BLANCO + jugador.getNombre() + RESET; }
    private String nEnemigo() { return ROJO + enemigo.getNombre() + RESET; }

    private void turnoJugador() {
        if (aplicarVenenoJugador()) return;

        if (!jugador.puedeActuar()) {
            registrar(nJugador() + " está paralizado y no puede actuar.");
            return;
        }

        int opcion = leerOpcion(1, 5);
        jugadorDefendiendo = false;

        Accion accion;
        switch (opcion) {
            case 1:  accion = new AtacarConEspada(); break;
            case 2:  accion = new DecoradorAtaquePoderoso(new AtacarConEspada()); break;
            case 3:  accion = new DecoradorAtaqueVenenoso(new AtacarConEspada()); break;
            case 4:  accion = new Defenderse(); jugadorDefendiendo = true; break;
            default: accion = new NoHacerNada(); break;
        }

        if (accion.getDañoBase() > 0) {
            int daño = calculador.calcularDaño(accion.getDañoBase(), jugador.getFuerza(), enemigo.getResistencia(),
                                    enemigoDefendiendo, jugador.getEstado(), enemigo.getEstado());

            if (accion instanceof DecoradorAtaquePoderoso
                    && jugador.getFuerza() >= DecoradorAtaquePoderoso.UMBRAL_FUERZA) {
                daño += DecoradorAtaquePoderoso.BONUS;
            }

            enemigo.recibirDaño(daño);
            enemigoDefendiendo = false;

            if (accion instanceof DecoradorAtaqueVenenoso) {
                venenoEnemigoTurnos = DecoradorAtaqueVenenoso.TURNOS;
                registrar(nEnemigo() + " ha sido envenenado.");
            }

            registrar(nJugador() + " usa " + accion.getDescripcion() + " → " + daño + " daño");
        } else if (opcion == 4) {
            registrar(nJugador() + " se pone en guardia.");
        } else {
            registrar(nJugador() + " no hace nada.");
        }
    }

    private void turnoEnemigo() {
        if (aplicarVenenoEnemigo()) return;

        double porcentajeVida = enemigo.porcentajeVida();
        if (porcentajeVida < 0.60 && porcentajeVida >= 0.40) {
            enemigo.setEstrategia(new EstrategiaEquilibrada());
        } else if (porcentajeVida < 0.40) {
            enemigo.setEstrategia(new EstrategiaDefensiva());
        }

        if (!enemigo.puedeActuar()) {
            registrar(nEnemigo() + " está paralizado y no puede actuar.");
            return;
        }

        // Template Method: decidirAccion() sigue el algoritmo definido en Enemigo
        Accion accion = enemigo.decidirAccion(jugador);
        enemigoDefendiendo = false;

        if (accion.getDañoBase() > 0) {
             // Usamos el SINGLETON CalculadorCombate para calcular el daño
            int daño = calculador.calcularDaño(accion.getDañoBase(), enemigo.getFuerza(), jugador.getResistencia(),
                                    jugadorDefendiendo, enemigo.getEstado(), jugador.getEstado());
            jugador.recibirDaño(daño);
            jugadorDefendiendo = false;
            registrar(nEnemigo() + " usa " + accion.getDescripcion() + " → " + daño + " daño (" + enemigo.getEstrategia().getNombre() + ")");
        } else if (accion instanceof Defenderse) {
            enemigoDefendiendo = true;
            registrar(nEnemigo() + " se pone en guardia. (" + enemigo.getEstrategia().getNombre() + ")");
        } else {
            registrar(nEnemigo() + " no hace nada. (" + enemigo.getEstrategia().getNombre() + ")");
        }
    }

    // Aplica el daño de veneno al jugador al inicio de su turno. Devuelve true si el jugador muere por el veneno.
    private boolean aplicarVenenoJugador() {
        if (venenoJugadorTurnos > 0) {
            jugador.recibirDaño(DecoradorAtaqueVenenoso.DAÑO_VENENO);
            venenoJugadorTurnos--;
            registrar(nJugador() + " sufre " + DecoradorAtaqueVenenoso.DAÑO_VENENO + " de veneno (quedan " + venenoJugadorTurnos + ").");
            return !jugador.estaVivo();
        }
        return false;
    }

    // Aplica el daño de veneno al enemigo al inicio de su turno. Devuelve true si el enemigo muere por el veneno.
    private boolean aplicarVenenoEnemigo() {
        if (venenoEnemigoTurnos > 0) {
            enemigo.recibirDaño(DecoradorAtaqueVenenoso.DAÑO_VENENO);
            venenoEnemigoTurnos--;
            registrar(nEnemigo() + " sufre " + DecoradorAtaqueVenenoso.DAÑO_VENENO + " de veneno (quedan " + venenoEnemigoTurnos + ").");
            return !enemigo.estaVivo();
        }
        return false;
    }

    private void registrar(String mensaje) {
        log.add(mensaje);
    }

    private int leerOpcion(int min, int max) {
        while (true) {
            try {
                int op = Integer.parseInt(consola.readLine().trim());
                if (op >= min && op <= max) return op;
            } catch (NumberFormatException ignorado) {}
            consola.print("Opcion no valida. Elige entre " + min + " y " + max + ": ");
        }
    }

    private void pausa(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignorado) {}
    }
}
