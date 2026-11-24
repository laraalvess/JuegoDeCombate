package acciones;

import personajes.Personaje;

//Ataque especial que aplica estado de veneno
//Dura tres turnos, haciendo 3 de daño por turno

public class DecoradorAtaqueVenenoso extends AccionDecorador {

    public static final int TURNOS = 3;
    public static final int DAÑO_VENENO = 3;
    private int turnosRestantes = 0;

    public DecoradorAtaqueVenenoso(Accion accion) {
        super(accion);
    }

    @Override
    public int getDañoBase() {
        return accionDecorada.getDañoBase();
    }

    @Override
    public String getDescripcion() {
        return accionDecorada.getDescripcion() + " + Veneno (" + TURNOS + " turnos)";
    }

    @Override
    public void ejecutar(Personaje ejecutor, Personaje objetivo) {
        super.ejecutar(ejecutor, objetivo);
        turnosRestantes = TURNOS;
    }

    public void aplicarVenenoSiActivo(Personaje objetivo) {
        if (turnosRestantes > 0) {
            objetivo.recibirDaño(DAÑO_VENENO);
            turnosRestantes--;
        }
    }

    public boolean estaActivo() {
        return turnosRestantes > 0;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }
}
