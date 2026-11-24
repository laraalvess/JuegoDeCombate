package estado;

//Estado herido
//Reduce el daño entre 2 y reduce la defensa entre 2, puede seguir usando su turno

public class EstadoHerido implements EstadoPersonaje{
    
    public String getNombre(){
        return "Herido";
    }

    public int modificarDaño(int d){
        return d / 2;
    }

    public int modificarDefensa(int d){
        return d / 2;
    }

    public boolean puedeActuar(){
        return true;
    }
}
