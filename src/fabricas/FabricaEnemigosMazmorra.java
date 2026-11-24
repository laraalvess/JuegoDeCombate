package fabricas;

import estrategias.EstrategiaDefensiva;
import estrategias.EstrategiaEquilibrada;
import personajes.Enemigo;

// Bioma intermedio: enemigos mas resistentes y con estrategias defensivas
public class FabricaEnemigosMazmorra implements FabricaEnemigos {

    @Override
    public Enemigo crearGuerrero(int ronda) {
        return new Enemigo("Caballero oscuro",
                90 + ronda * 10,
                10 + ronda * 2,
                11 + ronda,
                9,
                new EstrategiaDefensiva());
    }

    @Override
    public Enemigo crearMago(int ronda) {
        return new Enemigo("Nigromante",
                60 + ronda * 8,
                14 + ronda * 2,
                5 + ronda,
                10,
                new EstrategiaEquilibrada());
    }

    @Override
    public Enemigo crearBestia(int ronda) {
        return new Enemigo("Gárgola",
                75 + ronda * 10,
                11 + ronda * 2,
                8 + ronda,
                12,
                new EstrategiaDefensiva());
    }

    @Override
    public String getNombreBioma() {
        return "Mazmorra";
    }
}
