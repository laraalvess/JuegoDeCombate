package estado;

//Estado por defecto, sin modificadores añadidos

public class EstadoActivo implements EstadoPersonaje {

    public String getNombre(){
        return "Activo";
    }

    public int modificarDaño(int d){
        return d;
    }

    public int modificarDefensa(int d){
        return d;
    }

    public boolean puedeActuar(){
        return true;
    }
    
}
