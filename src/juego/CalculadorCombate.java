package juego;

import estado.EstadoPersonaje;

public class CalculadorCombate {

    // La única instancia de la clase (se crea al cargar la clase, thread-safe)
    private static final CalculadorCombate INSTANCIA = new CalculadorCombate();

    // Constructor privado: nadie puede crear instancias desde fuera
    private CalculadorCombate() {}

    /**
     * Devuelve la única instancia del calculador.
     * return instancia única de CalculadorCombate
     */
    public static CalculadorCombate getInstancia() {
        return INSTANCIA;
    }

    /**
     * Calcula el daño final de un ataque aplicando fuerza, resistencia,
     * modificadores de estado y reducción por defensa.
     *
     * Fórmula: daño = base + fuerza * 0.5 - resistenciaEfectiva * 0.3
     * Si el objetivo se está defendiendo, el daño se reduce un 50%.
     * Los estados del atacante y defensor modifican el daño y la resistencia.
     * El daño mínimo siempre es 1.
     *
     * param base               daño base de la acción
     * param fuerza             fuerza del atacante
     * param resistencia        resistencia del defensor
     * param objetivoDefendiendo true si el defensor eligió defenderse este turno
     * param estadoAtacante     estado actual del atacante (activo, herido, paralizado...)
     * param estadoDefensor     estado actual del defensor
     * return daño final (mínimo 1)
     */
    public int calcularDaño(int base, int fuerza, int resistencia,
                             boolean objetivoDefendiendo,
                             EstadoPersonaje estadoAtacante,
                             EstadoPersonaje estadoDefensor) {

        // La resistencia del defensor se ve afectada por su propio estado
        int resEfectiva = estadoDefensor.modificarDefensa(resistencia);

        // Fórmula principal de daño
        double daño = base + fuerza * 0.5 - resEfectiva * 0.3;

        // El estado del atacante puede reducir su propio daño (herido → mitad, paralizado → 0)
        daño = estadoAtacante.modificarDaño((int) daño);

        // Si el objetivo se defendió, recibe la mitad del daño
        if (objetivoDefendiendo) {
            daño *= 0.5;
        }

        // El daño nunca puede ser 0 ni negativo
        return Math.max(1, (int) Math.round(daño));
    }
}
