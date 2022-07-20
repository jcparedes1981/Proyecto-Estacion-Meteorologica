
package iniciadorserversocket;

import java.util.Date;

public class Fecha {
    private String fecha;
       
    Fecha(){
        Date fecha = new Date(System.currentTimeMillis());   
        String anio = "" + (fecha.getYear()+1900);
        String mes = fecha.getMonth() > 8 ? "" + (fecha.getMonth()+1) : "0" + (fecha.getMonth()+1);
        String dia = fecha.getDate() > 9 ? "" + fecha.getDate() : "0" + fecha.getDate();
        String horas = fecha.getHours() > 9 ? "" + fecha.getHours() : "0" + fecha.getHours();
        String minutos = fecha.getMinutes() > 9 ? "" + fecha.getMinutes() : "0" + fecha.getMinutes();
        String segundos = fecha.getSeconds() > 9 ? "" + fecha.getSeconds() : "0" + fecha.getSeconds();
        this.fecha = anio + "-" + mes + "-" + dia + " " + horas + ":" + minutos + ":" + segundos;        
    }

    public String getFecha() {
        Date fecha = new Date(System.currentTimeMillis());   
        String anio = "" + (fecha.getYear()+1900);
        String mes = fecha.getMonth() > 8 ? "" + (fecha.getMonth()+1) : "0" + (fecha.getMonth()+1);
        String dia = fecha.getDate() > 9 ? "" + fecha.getDate() : "0" + fecha.getDate();
        String horas = fecha.getHours() > 9 ? "" + fecha.getHours() : "0" + fecha.getHours();
        String minutos = fecha.getMinutes() > 9 ? "" + fecha.getMinutes() : "0" + fecha.getMinutes();
        String segundos = fecha.getSeconds() > 9 ? "" + fecha.getSeconds() : "0" + fecha.getSeconds();
        this.fecha = anio + "-" + mes + "-" + dia + " " + horas + ":" + minutos + ":" + segundos;  
        return this.fecha;
    }

}
