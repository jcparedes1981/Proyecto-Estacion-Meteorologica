package iniciadorserversocket;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionSingleton {
    private static ConexionSingleton instancia;
    private Connection cnx;
    
    
    private ConexionSingleton(){ 
        try {
            this.cnx = DriverManager.getConnection("jdbc:mariadb://localhost:3306/bd_estacion_met", "jcparedes", "Julipes81+16");
        } catch (SQLException ex) {
            Logger.getLogger(ConexionSingleton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ConexionSingleton getConexionSingleton(){
        if (instancia == null){
            instancia = new ConexionSingleton();
        }
        return instancia;
    }
    
    public void ejecutarSentencia_IUD(String query){    // Sentencia Insert, Update o Delete
        Statement st;
        try {
            st = cnx.createStatement();
            st.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionSingleton.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Fallo la insercion...");
                    
        }
        
    }
    
    public ResultSet extraerAlertas(){
        ResultSet rs = null;
        Statement st;
        try {
            st = cnx.createStatement();
            rs = st.executeQuery("SELECT * FROM tbl_alertas");
        } catch (SQLException ex) {
            Logger.getLogger(ConexionSingleton.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Fallo la extracción de reglas...");
                    
        }
        return rs;
    }
    
    public boolean existeDispositivo(String idEstacion){
        ResultSet rs = null;
        Statement st;
        boolean result = false;
        try {
            st = cnx.createStatement();
            rs = st.executeQuery("SELECT * FROM tbl_dispositivos WHERE idEstacion='" + idEstacion + "';");
            if( rs.first() ){
                result=true;
            } 
        } catch (SQLException ex) {
            Logger.getLogger(ConexionSingleton.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Fallo verificación de dispositivo...");
                    
        }
        return result;
    }
}
