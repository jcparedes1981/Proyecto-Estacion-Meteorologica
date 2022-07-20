
package iniciadorserversocket;

import java.util.Date;

public class IniciadorServerSocket{

        
    public static void main(String[] args) throws Exception {
        
        if ( args.length>0 ){            
            int puerto = Integer.parseInt(args[0]);     // toma el primer argumento como valor de puerto
            Monitor monitorMet = new Monitor(puerto);
            monitorMet.iniciarServicio();
        } else{
            System.out.println("Parametro incorrecto!!!");
            System.exit(0);
        }
        
    }
}

 