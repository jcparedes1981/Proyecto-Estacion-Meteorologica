
package iniciadorserversocket;

import java.util.*;

interface SocketListener extends EventListener{
    public void clienteConectado(EventoConexion ec);
    public void clienteDesconectado(EventoConexion ec);
    public void mensajeRecibido(EventoMensaje em);
}
