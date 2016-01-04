package utils;

import drools.Purchase;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class Mailer {

    private Mailer() {}

    public static void sendMessage(Purchase purchase) throws MessagingException, IOException {
        final Properties props = new Properties();
        props.load(Mailer.class.getResourceAsStream("/properties/mail.properties"));
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(props.getProperty("mail.username"), props.getProperty("mail.password"));
                    }
                });
        Message message = new MimeMessage(session);
        message.setSubject("Purchase confirmation");
        message.setText("Your purchase has been realized. Next time try to use paypal or credit card to get 10% discount!");
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(purchase.getClient().getEmail()));
        Transport.send(message);
    }
}
