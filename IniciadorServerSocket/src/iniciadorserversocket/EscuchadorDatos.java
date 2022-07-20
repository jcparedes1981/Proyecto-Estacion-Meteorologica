
package iniciadorserversocket;

import javax.swing.event.*;
import java.net.*;
import java.io.*;

public class EscuchadorDatos extends Thread {
    
    private Socket sCliente;
    private EventListenerList listaEscuchadores = null;
    private DataInputStream in = null;
    private int id;
    private String idDispositivo = "Pending";
    private final int TIEMPO_ESPERA = 5000;

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }
    
    public EscuchadorDatos(Socket s, int id) {
        this.sCliente = s;
        this.id = id;
        listaEscuchadores = new EventListenerList();
    }
    
    void addEscuchadorMensajes(SocketListener sl){
        listaEscuchadores.add(SocketListener.class, sl);
    }
    
    void removeEscuchadorMensajes(SocketListener sl){
        listaEscuchadores.remove(SocketListener.class, sl);
    }
    
    public Socket getSocketCliente(){
        return sCliente;
    }
        
    @Override
    public void run(){        
        try {
            while( true ){                
                BufferedReader in = new BufferedReader(new InputStreamReader(this.sCliente.getInputStream()));
                String mensaje = null;
                
                // Bloque if para controlar si el socket se cerró (aún no se ha probado)
                /*if(this.sCliente.isClosed()){
                    System.out.println("Socket cerrado desde el cliente");
                }*/
                
                if (in.ready()){
                    mensaje = in.readLine();
                }                
                if( !this.sCliente.isClosed() && (mensaje != null) && (!"".equals(mensaje)) ){                    
                    EventoMensaje em = new EventoMensaje(this, mensaje, id);
                    despacharEventoMsjRecibido(em);
                } else{                    
                    if (!this.sCliente.getInetAddress().isReachable(TIEMPO_ESPERA)){
                        EventoConexion ec = new EventoConexion(this, this.sCliente, this.id);
                        despacharEventoCDesconectado(ec);
                    }                    
                }
            }            
        } catch (IOException e) {
            System.err.println("Error en el Escuchador de Datos: " + e.getMessage());
            EventoConexion ec = new EventoConexion(this, this.sCliente, this.id);
            despacharEventoCDesconectado(ec);
        } 
    }
    
    protected void despacharEventoMsjRecibido(EventoMensaje em){
        SocketListener[] slLista = listaEscuchadores.getListeners(SocketListener.class);
        for (SocketListener sl : slLista){
            sl.mensajeRecibido(em);
        }
    }
    
    protected void despacharEventoCDesconectado(EventoConexion ec){
        SocketListener[] slLista = listaEscuchadores.getListeners(SocketListener.class);
        for (SocketListener sl : slLista){
            sl.clienteDesconectado(ec);
        }
    }
}
