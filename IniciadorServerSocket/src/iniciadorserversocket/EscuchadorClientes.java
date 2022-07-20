
package iniciadorserversocket;

import java.io.*;
import javax.swing.event.*;
import java.net.*;
import java.lang.*;

public class EscuchadorClientes extends Thread {
    private ServerSocket sServidor ;
    private EventListenerList listaEscuchadores = null;
    private int genID = 100;
    
    public EscuchadorClientes(ServerSocket sSocket){
        this.sServidor = sSocket;
        listaEscuchadores = new EventListenerList();
    }
    
    void addEscuchadorConexion(SocketListener sl){
        listaEscuchadores.add(SocketListener.class, sl);
    }
    
    void removeEscuchadorConexion(SocketListener sl){
        listaEscuchadores.remove(SocketListener.class, sl);
    }
    
    @Override
    public void run(){
        System.out.println("Servidor Conectado...");
        while (true){
            try {
                Socket sCliente = sServidor.accept();
                EventoConexion ec = new EventoConexion(this, sCliente, genID);
                this.genID++;
                despacharEventoCConectado(ec);
            } catch (IOException e) {
                
            }
        }
    }
    
    protected void despacharEventoCConectado(EventoConexion ec){
        SocketListener[] slLista = listaEscuchadores.getListeners(SocketListener.class);
        for (SocketListener sl : slLista){
            sl.clienteConectado(ec);
        }
    }
    
    protected void despacharEventoCDesconectado(EventoConexion ec){
        SocketListener[] slLista = listaEscuchadores.getListeners(SocketListener.class);
        for (SocketListener sl : slLista){
            sl.clienteDesconectado(ec);
        }
    }
}
