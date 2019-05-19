package code;
import code.model.DAOInterfaces.DAOChambre;
import code.model.DAOInterfaces.DAOHotel;
import code.model.DAOJDBC.DAOChambreJDBC;
import code.model.DAOJDBC.DAOHotelJDBC;

import java.util.List;
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
        System.out.println(reservation.getFacture() == null);
        this.message += ("Bonjour " + this.client.getPrenom() + " " + this.client.getNom().toUpperCase());
        this.message += ("<br/>Voici la facture de votre séjour du " + reservation.getDateArrivee().toString() + " au " + reservation.getDateDepart().toString());

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
        this.message += "<br/>En vous souhaitant une bonne journée. Merci.";
        this.message += "<br/>Cordialement. Toute l'equipe de la chaîne d'hotel";

        createEmailMessage();
        sendEmail();

    }

    public void publicite(Hotel hotelPub) throws MessagingException {
        DAOHotel daoHotel = new DAOHotelJDBC();
        DAOChambre daoChambre = new DAOChambreJDBC();
        this.message += ("Bonjour " + this.client.getPrenom() + " " + this.client.getNom().toUpperCase());
        this.message += ("<br/>Merci d'avoir réservé dans l'un de nos hôtels.");
        this.message += ("<br/>Découvrez l'hôtel " + hotelPub.getNom() + ", situé dans la ville de " + hotelPub.getVille());
        this.message += ("<br/><div style=\"color: blue\">*------------------------------ HOTEL " + hotelPub.getNom() + " ------------------------------*</div>");

        List<TypeService> services = daoHotel.getServicesById(hotelPub.getNumHotel());
        if (services.size() > 0) {
            this.message += "<br/><br/>Proposant ces services : <br/>";
            for (TypeService service : services) {
                this.message += " - " + service.getNom() + " au prix de : " + service.getPrix() + " euros.<br/>";
            }
            this.message += "<br/>";
        }

        List<String> typesChambres = daoChambre.getTypesChambres();
        this.message += "Un hôtel de " + daoHotel.getNbChambres(hotelPub.getNumHotel()) + " chambres :";

        this.message += "<br/><br/>";
        for (String type : typesChambres) {
            this.message += daoChambre.getNbChambresParType(hotelPub.getNumHotel(), type) + " chambres de type " + type + " :<br/> ";
            for (Chambre chambre : hotelPub.getChambres()) {
                if (chambre.getType().equals(type)) {
                    this.message += " - Chambre à " + chambre.getPrix() + " euros, avec " + chambre.getNombreLits() + " lits";
                    if (chambre.getOptions().size() > 0) {
                        this.message += ", profitez des options suivantes :<br/>";
                        for (Option option : chambre.getOptions()) {
                            this.message += "   - " + option.getNom() + " à " + option.getPrix() + " euros<br/>";
                        }
                        this.message += "<br/>";
                        break;
                    } else {
                        this.message += ".<br/><br/>";
                    }
                }
            }
            this.message += "<br/>";
        }
        this.message += "Réservez dès à présent sur le site grouphotel.alwaysdata.net !";
        createEmailMessage();
        sendEmail();

    }
}
