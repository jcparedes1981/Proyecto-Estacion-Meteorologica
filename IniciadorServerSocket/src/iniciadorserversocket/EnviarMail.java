
package iniciadorserversocket;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EnviarMail {
    
    private String userMail;
    private String pass;
    private String serverSMTP;
    private Properties props;
    
    public EnviarMail(String server, String user, String password){
        this.serverSMTP = server;
        this.userMail = user;
        this.pass = password;
        this.props = System.getProperties();
        
        props.put("mail.smtp.host", serverSMTP); 
        props.put("mail.smtp.user", userMail);
        props.put("mail.smtp.clave", pass);
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");        
    }
    
    public void enviarCorreo(String destinatario, String asunto, String cuerpo){
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(userMail));
            message.addRecipients(Message.RecipientType.TO, destinatario);             
            message.setSubject(asunto);
            message.setText(cuerpo);
            Transport sesionTransporte = session.getTransport("smtp");
            sesionTransporte.connect(serverSMTP, userMail, pass);
            sesionTransporte.sendMessage(message, message.getAllRecipients());
            sesionTransporte.close();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    
    
}
