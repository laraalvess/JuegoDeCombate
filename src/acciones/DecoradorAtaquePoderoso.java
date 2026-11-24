package acciones;

import personajes.Personaje;

//Decorador de ataque poderoso, solo si el personaje tiene 15 o mas de fuerza
//Añade un bonus de 5 de daño

public class DecoradorAtaquePoderoso extends AccionDecorador {

    public static final int UMBRAL_FUERZA = 15;
    public static final int BONUS = 5;

    public DecoradorAtaquePoderoso(Accion accion) {
        super(accion);
    }

    @Override
    public int getDañoBase() {
        return accionDecorada.getDañoBase();
    }

    @Override
    public String getDescripcion() {
        return accionDecorada.getDescripcion() + " + Ataque Poderoso (bonus: " + BONUS + ")";
    }

    @Override
    public void ejecutar(Personaje ejecutor, Personaje objetivo) {
        int daño = getDañoBase();
        if (ejecutor.getFuerza() >= UMBRAL_FUERZA) {
            daño += BONUS;
        }
        objetivo.recibirDaño(daño);
    }
}
