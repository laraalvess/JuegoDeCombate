 package juego;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        // La ventana Swing debe crearse en el Event Dispatch Thread
        ConsolaVentana[] ref = {null};
        SwingUtilities.invokeAndWait(() -> ref[0] = new ConsolaVentana("Juego de Combate"));
        ConsolaVentana consola = ref[0];

        // Facade: un único punto de entrada para todo el juego
        GameController controller = new GameController(consola);
        controller.iniciarJuego();
        controller.ejecutarBucleJuego();
    }
}
