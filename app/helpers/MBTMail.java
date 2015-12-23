package helpers;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import play.Play;

import javax.mail.internet.MimeMessage;


public class MBTMail {


	public static void send(String username, String useremail, String message){
		if(!ConfigHelper.getEmailEnabled()){
			return;
		}
		try{
            String configUsername = Play.application().configuration().getString("email.username");
            String configPassword = Play.application().configuration().getString("email.password");
            Email email = new Email();
			email.setFromAddress(username, useremail);
			email.setSubject("Morgantown Bus Tracker Support: " + username);
			email.addRecipient("Sam", "morgantownbustracker@gmail.com", MimeMessage.RecipientType.TO);
			email.setText(username + " : " + useremail + " \n\n" + message);
			Mailer mailer = new Mailer("smtp.gmail.com", 465, configUsername, configPassword, TransportStrategy.SMTP_SSL);
			//mailer.setDebug(true);
			mailer.sendMail(email);
		}catch(Exception e){
            e.printStackTrace();
		}
	}
	
	
	
}
