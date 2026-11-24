package estado;

//Interfaz del patron state

public interface EstadoPersonaje {
    String getNombre();

    int modificarDaño(int d);

    int modificarDefensa(int d);

    boolean puedeActuar();
}
