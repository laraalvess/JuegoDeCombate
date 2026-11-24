package fabricas;

import estrategias.EstrategiaAgresiva;
import estrategias.EstrategiaEquilibrada;
import personajes.Enemigo;

// primer bioma: enemigos rapidos pero poco resistentes, inicio del juego
public class FabricaEnemigosBosque implements FabricaEnemigos {

    @Override
    public Enemigo crearGuerrero(int ronda) {
        return new Enemigo("Bandido del bosque",
                80 + ronda * 10,
                9 + ronda * 2,
                8 + ronda,
                11,
                new EstrategiaAgresiva());
    }

    @Override
    public Enemigo crearMago(int ronda) {
        return new Enemigo("Druida",
                55 + ronda * 8,
                13 + ronda * 2,
                4 + ronda,
                12,
                new EstrategiaEquilibrada());
    }

    @Override
    public Enemigo crearBestia(int ronda) {
        return new Enemigo("Lobo salvaje",
                70 + ronda * 10,
                11 + ronda * 2,
                5 + ronda,
                15,
                new EstrategiaAgresiva());
    }

    @Override
    public String getNombreBioma() {
        return "Bosque";
    }
}
