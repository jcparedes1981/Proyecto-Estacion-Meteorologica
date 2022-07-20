
package iniciadorserversocket;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.management.*;

public class ServidorSkt implements SocketListener {
    private ServerSocket sSocket = null;
    private HashMap<Integer, EscuchadorDatos> cConectados;
    private EscuchadorClientes escuchador = null;
    private SocketListener monitor = null;
    
    public ServidorSkt(SocketListener obj, int puerto){
        try {
            monitor = obj;
            sSocket = new ServerSocket(puerto);
            cConectados = new HashMap<Integer, EscuchadorDatos>();
            escuchador = new EscuchadorClientes(sSocket);
            escuchador.addEscuchadorConexion(this);
        } catch (IOException e) {
            System.out.println("No puede escuchar en el puerto: " + puerto);
        }
    }
    
    @Override
    public void clienteConectado(EventoConexion e){
        EscuchadorDatos ed = new EscuchadorDatos(e.getSocketCliente(), e.getID());
        ed.addEscuchadorMensajes(this);
        ed.addEscuchadorMensajes(monitor);
        ed.start();
        this.cConectados.put(e.getID(), ed );
        System.out.println("Se ha unido el cliente: " + e.getID());        
    }

    public HashMap<Integer, EscuchadorDatos> getcConectados() {
        return cConectados;
    }
    
    @Override
    public void clienteDesconectado(EventoConexion e){        
        //
    }
    
    @Override
    public void mensajeRecibido(EventoMensaje em){
        //System.out.println(em.getID() + ": " + em.getMensaje());
    }

    public EscuchadorClientes getEscuchador() {
        return escuchador;
    }
    
    public HashMap<Integer, EscuchadorDatos> getListaEscuchadoresDatos(){
        return cConectados;
    }
    
    public void envairMensajeCliente(int id, String mensaje){
        Socket sk = cConectados.get(id).getSocketCliente();
        
        Thread hilo = new Thread(new Runnable(){            
            @Override
            public void run(){
                try {
                    PrintWriter salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sk.getOutputStream())), true);
                    salida.println(mensaje);
                    salida.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServidorSkt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        hilo.start();
    }
    
}
