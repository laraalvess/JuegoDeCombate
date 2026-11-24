package fabricas;

import estrategias.EstrategiaAgresiva;
import personajes.Enemigo;

// ultimo bioma: enemigos mas fuertes y siempre agresivos
public class FabricaEnemigosVolcan implements FabricaEnemigos {

    @Override
    public Enemigo crearGuerrero(int ronda) {
        return new Enemigo("Berserker de fuego",
                75 + ronda * 10,
                14 + ronda * 2,
                7 + ronda,
                11,
                new EstrategiaAgresiva());
    }

    @Override
    public Enemigo crearMago(int ronda) {
        return new Enemigo("Pirómago",
                50 + ronda * 8,
                18 + ronda * 2,
                3 + ronda,
                11,
                new EstrategiaAgresiva());
    }

    @Override
    public Enemigo crearBestia(int ronda) {
        return new Enemigo("Dragon de magma",
                80 + ronda * 10,
                15 + ronda * 2,
                6 + ronda,
                14,
                new EstrategiaAgresiva());
    }

    @Override
    public String getNombreBioma() {
        return "Volcán";
    }
}
