
package iniciadorserversocket;

import java.util.*;
import java.net.*;

public class EventoConexion extends EventObject {
    
    private Socket sCliente;
    private int id;

    public EventoConexion(Object origen, Socket sCliente, int id){
        super(origen);
        this.sCliente = sCliente;
        this.id = id;
    }
    
    public Socket getSocketCliente() {
        return sCliente;
    }
    
    public int getID(){
        return id;
    }
        
    
}
