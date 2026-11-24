package estado;

//Estado paralizado
//El personaje no puede actuar y se salta su turno

public class EstadoParalizado implements EstadoPersonaje {
    
    public String getNombre(){
        return "Paralizado";
    }

    public int modificarDaño(int d){
        return 0;
    }

    public int modificarDefensa(int d){
        return 0;
    }

    public boolean puedeActuar(){
        return false;
    }

}
