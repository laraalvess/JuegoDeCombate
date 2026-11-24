package acciones;

import personajes.Personaje;

 //Contrato de cualquier accion que un personaje pueda usar en su turno
 //atacar con un arma, defenderse, no hacer nada, etc
 
 //interfaz para que el patron Decorator del apartado 2 pueda
 //envolver acciones basicas con modificadores ("ataque con espada",
 //"ataque poderoso con espada") si la fuerza es alta, etc

public interface Accion {

    //Texto descriptivo de la accion para usar en los mensajes por consola
    String getDescripcion();

    //Daño base de la accion antes de aplicar modificadores ni calculos finales
    int getDañoBase();


    //solo aplica daño base
    void ejecutar(Personaje ejecutor, Personaje objetivo);
}
