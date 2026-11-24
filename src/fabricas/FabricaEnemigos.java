package fabricas;

import personajes.Enemigo;

// Abstract Factory: define la familia de enemigos que un bioma puede producir
// Cada implementacion concreta encapsula la "ambientacion" del bioma (nombres,
// stats y estrategia inicial)
public interface FabricaEnemigos {

    Enemigo crearGuerrero(int ronda);

    Enemigo crearMago(int ronda);

    Enemigo crearBestia(int ronda);

    // Nombre del bioma para mostrar en la UI antes de cada combate
    String getNombreBioma();
}
