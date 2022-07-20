package iniciadorserversocket;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

public class Monitor implements SocketListener{
    private ServidorSkt servidor;
    private int puerto;
    
    public Monitor(int puerto){   
            this.puerto = puerto;
            servidor = new ServidorSkt(this, puerto);            
            System.out.println("Servidor conectado en puerto: " + puerto);                           
    }
    
    public void iniciarServicio(){ 
        //servidor.getEscuchador().addEscuchadorConexion(this);
        servidor.getEscuchador().start();
    }

    @Override
    public void clienteConectado(EventoConexion ec) {
        //servidor.getListaEscuchadoresDatos().get(ec.getID()).addEscuchadorMensajes(this);
    }

    @Override
    public void clienteDesconectado(EventoConexion ec) {     
        int id = ec.getID();
        System.out.println("Se ha desconectado el cliente " + id );
        String idEstacion = servidor.getListaEscuchadoresDatos().get(id).getIdDispositivo();
        String query1 = "UPDATE tbl_dispositivos SET estado='false' WHERE idEstacion='" + idEstacion + "'; ";
        NotificacionesPush.sendNotification("Notificacion Desconexión", "Sensor desconecetado: " + idEstacion);
        ConexionSingleton cns = ConexionSingleton.getConexionSingleton();
        cns.ejecutarSentencia_IUD(query1);
        EscuchadorDatos ed = this.servidor.getListaEscuchadoresDatos().get(id);
        try {
            ed.getSocketCliente().close();
            ed.interrupt();
            //this.servidor.getListaEscuchadoresDatos().remove(ec.id);
        } catch (IOException ex) {
            Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mensajeRecibido(EventoMensaje em) {
        System.out.println("Msj: " + em.getMensaje());
        EscuchadorDatos ed = servidor.getListaEscuchadoresDatos().get(em.getID());
        String query1, query2, query3;
        
        ConexionSingleton cns = ConexionSingleton.getConexionSingleton();
        String mensaje = em.getMensaje();       
        ParseInfo pm = new ParseInfo(mensaje); 
        if (ed.getIdDispositivo().equals("Pending")){
            ed.setIdDispositivo(pm.getIdSensor());
            if (cns.existeDispositivo(pm.getIdSensor())){
                query1 = "UPDATE tbl_dispositivos SET estado='true' WHERE idEstacion='" + pm.getIdSensor() + "'; ";
                cns.ejecutarSentencia_IUD(query1);
                NotificacionesPush.sendNotification("Notificacion Reconexión", "Sensor Conectado: " + pm.getIdSensor());
                System.out.println("Query1: " + query1);
            } else {
                query1 = "INSERT INTO tbl_dispositivos (idEstacion, fechaHoraReg, ultimaFechaDatos, ultimaTemp, ultimaHumed, estado) VALUES "
                        + "('" + pm.getIdSensor() + "', '" + new Fecha().getFecha() + "', '" + pm.getFechaHora() + "', '" + pm.getTemp()+ "', '" + pm.getHumed() + "', 'true');";
                cns.ejecutarSentencia_IUD(query1);
                NotificacionesPush.sendNotification("Notificacion Conexión", "Sensor Conectado: " + pm.getIdSensor());
                System.out.println("Query1: " + query1);
            }            
        }
        query2 = "INSERT INTO tbl_datosclima2 (fechaHora, idEstacion, temperatura, humedad) VALUES ('" 
                + pm.getFechaHora()+ "', '" + pm.getIdSensor()+ "', '" + pm.getTemp()+ "', '" + pm.getHumed()+ "'); ";
        cns.ejecutarSentencia_IUD(query2);
        System.out.println("Query2: " + query2);
        query3 = "UPDATE tbl_dispositivos SET ultimaFechaDatos='"+ pm.getFechaHora() + "', ultimaTemp='" 
                + pm.getTemp()+ "', ultimaHumed='" + pm.getHumed()+ "' WHERE idEstacion='" + pm.getIdSensor()+ "';";         
        cns.ejecutarSentencia_IUD(query3);
        System.out.println("Query3: " + query3);        
        
        ResultSet reglas = cns.extraerAlertas();
        try {
            while (reglas.next()) {                
                double ini = reglas.getDouble("valorInicio");
                double fin = reglas.getDouble("valorFin");
                String msj = reglas.getString("mensaje");
                String destinatarios = reglas.getString("destinatarios");
                if ( pm.getTemp() >= ini && pm.getTemp() <= fin ){
                    EnviarMail sender = new EnviarMail("smtp.gmail.com","jcparedes.escobar@gmail.com","eobjyvepaiythpej");  // Utiliza contraseña de aplicaciones para google, pass de 16 caracteres "eobjyvepaiythpej"                    
                    String asunto = pm.getIdSensor() + ": Alerta...!!!";
                    sender.enviarCorreo(destinatarios, asunto, msj);
                }
            }
            reglas.close();            
        } catch (SQLException ex) {
            Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
