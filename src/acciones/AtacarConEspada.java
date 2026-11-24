package acciones;

import personajes.Personaje;

// ataque basico cuerpo a cuerpo con espada
public class AtacarConEspada implements Accion {

    private static final int DAÑO_BASE = 10;

    @Override
    public String getDescripcion() {
        return "ataque con espada";
    }

    @Override
    public int getDañoBase() {
        return DAÑO_BASE;
    }

    @Override
    public void ejecutar(Personaje ejecutor, Personaje objetivo) {
        objetivo.recibirDaño(DAÑO_BASE);
    }
}
