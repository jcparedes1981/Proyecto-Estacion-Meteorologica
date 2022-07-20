
package iniciadorserversocket;

import java.util.*;
import java.net.*;

public class EventoMensaje extends EventObject {
    
    private String mensaje;
    private int id;

    public EventoMensaje(Object origen, String msj, int id) {
        super(origen);
        this.mensaje = msj;
        this.id = id;
    }
    
    public String getMensaje(){
        return mensaje;
    }
    
    public int getID(){
        return id;
    }
    
}
