package iniciadorserversocket;

import java.util.Date;

public class ParseInfo {
    
    private double temp;    
    private double humed;
    private String fechaHora;
    private String idSensor;
    
    public ParseInfo(String cadenaInfoSensor){
        //Anteiror protocolo: idSensor;fechaHora;valor_temp;valor_humed 
        //Nuevo protocolo: 'ID=12345|Temp=19.50|Hum=24.57|Tiempo=765'
        String[] datos = cadenaInfoSensor.split("\\|");   
        if ( datos.length >= 4 ){
            this.idSensor = datos[0].split("=")[1];            
            this.temp = (Double.parseDouble(datos[1].split("=")[1]) > 0)?Double.parseDouble(datos[1].split("=")[1]):0.00;
            this.humed = (Double.parseDouble(datos[2].split("=")[1]) > 0)?Double.parseDouble(datos[2].split("=")[1]):0.00;
            this.fechaHora = (datos[3].split("=")[1]);
        }else{
            this.temp = 0.00;
            this.humed = 0.00;
            this.idSensor = "none";
            this.fechaHora = "00000000000";
        }
    }
    
    public double getTemp() {
        return temp;
    }

    public double getHumed() {
        return humed;
    }

    public String getFechaHora() {
        long fechaMilli;
        String result;
        
        if( fechaHora.length() > 10 ){
            fechaMilli = Long.parseLong(fechaHora);
        } else {
            fechaMilli = (Long.parseLong(fechaHora))*1000;
        }        
        Date fecha = new Date(fechaMilli);
        String anio = "" + (fecha.getYear()+1900);
        String mes = fecha.getMonth() > 8 ? "" + (fecha.getMonth()+1) : "0" + (fecha.getMonth()+1);
        String dia = fecha.getDate() > 9 ? "" + fecha.getDate() : "0" + fecha.getDate();
        String horas = fecha.getHours() > 9 ? "" + fecha.getHours() : "0" + fecha.getHours();
        String minutos = fecha.getMinutes() > 9 ? "" + fecha.getMinutes() : "0" + fecha.getMinutes();
        String segundos = fecha.getSeconds() > 9 ? "" + fecha.getSeconds() : "0" + fecha.getSeconds();
        result = anio + "-" + mes + "-" + dia + " " + horas + ":" + minutos + ":" + segundos; 
        return result;
    }

    public String getIdSensor() {
        return idSensor;
    }
}
