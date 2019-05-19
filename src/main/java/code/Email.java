package code;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
    private static final String from = "chaine-hotel@reponds_pas.fr";
    private String message;
    private String title;
    private Client client;

    private static final String port = "587";

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public Email(String title, Client client) {
        this.client = client;
        this.title = title;

        try {
            setMailServerProperties();
        }
        catch (Exception ae) {
            System.out.println(ae.getMessage());
            ae.printStackTrace();
        }
    }

    private void setMailServerProperties() {

        String emailPort = "587";//gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
    }

    private void createEmailMessage() throws MessagingException {
        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(client.getMail()));


        emailMessage.setSubject(this.title);
        emailMessage.setContent(this.message, "text/html");//for a html email
        //emailMessage.setText(this.message);// for a text email

    }

    private void sendEmail() throws MessagingException {

        String emailHost = "smtp.gmail.com";
        String fromUser = "chaine.hotel@gmail.com";//just the id alone without @gmail.com
        String fromUserEmailPassword = "chaine-hotel1";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }

    public void facture(Reservation reservation) throws MessagingException{
        this.message += ("Bonjour " + this.client.getPrenom() + " " + this.client.getNom().toUpperCase());
        this.message += ("<br/>Voici la facture de votre sejour du " + reservation.getDateArrivee().toString() + " au " + reservation.getDateDepart().toString());

        this.message += ("<br/><div style=\"color: blue\">*------------------------------ FACTURE ------------------------------*</div>");
        for (Map.Entry<String, Float> entry : reservation.getFacture().entrySet()) {
            if(entry.getKey().equals("Prix total")) {
                this.message += ("<br/><div style=\"color: red\">" + (entry.getKey() + " ------------------- " + String.valueOf(entry.getValue())) + "</div>");
            } else {
                this.message += ("<br/>" + entry.getKey() + " ------------------- " + String.valueOf(entry.getValue()));
            }
        }
        this.message += ("<br/><div style=\"color: blue\">*------------------------------ FACTURE ------------------------------*</div>");
        this.message += "<br/>Vous vous connecterez sur votre compte afin de payer le montant";
        this.message += "<br/>En vous souhaitant une bonne journee. Merci.";
        this.message += "<br/>Cordialement. Toute l'equipe de la chaîne d'hotel";

        System.out.println(message);

        createEmailMessage();
        sendEmail();

    }
}
