package Test;

import code.*;
import code.model.DAOJDBC.DAOChambreJDBC;
import code.model.DAOJDBC.DAOClientJDBC;
import code.model.DAOJDBC.DAOReservationJDBC;
import code.model.DAOJDBC.DAOTypeServiceJDBC;
import com.sun.org.apache.regexp.internal.RE;
import javafx.util.Pair;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationTest {
    public static void main(String args[]) {
        //testInsert();
        //testUpdate();
        //getById();
        //testFindAll();
        //testDelete();
        //getChambres();
        ////deleteLiens();
        //insertLiens();
        //updateLiens();
        //testFindHistorique();
        //etstGetServices();
        //insertService();
        //deleteService();
        //testFindByEtatAndUpdate();
        //testUpdateLiensTypeService();
        //testFindHistoriqueClient();
        //testVisitor();
        //testEmail();
        count();
    }

    public static void deleteLiens() {
        Reservation r = new DAOReservationJDBC().getById(2);
        //Chambre c = new DAOChambreJDBC().getById(new Pair<>(1, 101));
        System.out.println(new DAOReservationJDBC().deleteLiensChambre(r, null) ? "true" : "false");
    }

    public static void getChambres() {
        List<Chambre> chambres = new DAOReservationJDBC().getChambres(1);
        for (Chambre c : chambres)
            System.out.println(c.toString());
    }

    public static void testInsert() {
        Client c = new Client();
        c.setNum(1);
        Reservation r = new Reservation(6, LocalDate.now(), LocalDate.now(), 15, "ATTENTR_CONFIRMATION2", 15, 0, c, null, null);
        r.setChambres(new DAOChambreJDBC().findAll());
        Reservation n = new DAOReservationJDBC().insert(r);
        System.out.println(n == null ? "null" : n.toString());
    }

    public static void testUpdate() {
        Client c = new Client();
        c.setNum(1);
        Reservation r = new Reservation(1, LocalDate.now(), LocalDate.now(), 18, "ATTN", 0, 100, c, null, null);
        boolean n = new DAOReservationJDBC().update(r);
        System.out.println(n ? "true" : false);
    }

    public static void getById() {
        Reservation r = new DAOReservationJDBC().getById(1);

        System.out.println(r);
        System.out.println(r.getChambres().size());
    }

    public static void testFindAll() {
        List<Reservation> reservations = new DAOReservationJDBC().findAll();

        for (Reservation r : reservations) {
            System.out.println(r.toString());
        }
    }

    public static void testDelete() {
        Reservation reservation = new DAOReservationJDBC().getById(3);

        boolean isDel = new DAOReservationJDBC().delete(reservation);
        System.out.println(isDel ? "true" : "false");
    }

    public static void insertLiens() {
        System.out.println("test");
        Reservation reservation = new DAOReservationJDBC().getById(2);
        Chambre c = new DAOChambreJDBC().getById(new Pair<>(1, 101));

        System.out.println(new DAOReservationJDBC().insertLiensChambre(reservation, c) ? "true" : "false");
    }

    public static void updateLiens() {
        Reservation r = new DAOReservationJDBC().getById(2);
        r.setChambres(new ArrayList<>());
        r.getChambres().add(new DAOChambreJDBC().getById(new Pair<>(1, 301)));

        System.out.println(new DAOReservationJDBC().updateLiensChambre(r) ? "true" : "false");
    }

    public static void testFindHistorique() {
        List<Reservation> historique = new DAOReservationJDBC().findHistoriqueClient(37);
        System.out.println(historique);
    }

    public static void etstGetServices() {
        for (TypeService t : new DAOReservationJDBC().getTypeServices(19)) {
            System.out.println(t.toString());
        }
    }

    public static void insertService() {/*
        TypeService s1 = new DAOTypeServiceJDBC().getById("Piscine");

        Reservation r = new DAOReservationJDBC().getById(2);

        System.out.println(new DAOReservationJDBC().insertLiensTypeService(r, s1) ? "true": "false");*/
        Reservation r = new DAOReservationJDBC().getById(18);
        r.setNumReservation(20);
        r.getServices().addAll(new DAOTypeServiceJDBC().findAll());

        System.out.println(r.toString());
        System.out.println(r.getServices().size());

        System.out.println(new DAOReservationJDBC().insert(r) != null ? "true" : "false");

    }

    public static void deleteService() {/*
        TypeService s1 = new DAOTypeServiceJDBC().getById("Piscine");

        Reservation r = new DAOReservationJDBC().getById(18);

        System.out.println(new DAOReservationJDBC().deleteLiensTypeService(r, null) ? "true": "false");*/
        Reservation r = new DAOReservationJDBC().getById(18);
        System.out.println(new DAOReservationJDBC().delete(r) ? "true" : "false");

    }

    public static void testFindByEtatAndUpdate() {
        List<Reservation> reservations = new DAOReservationJDBC().findByEtat(13, "CONFIRMATION");
        System.out.println(reservations);
        /*System.out.println(new DAOReservationJDBC().updateEtatReservation(reservations.get(0).getNumReservation(), "CONFIRMATION"));
        reservations = new DAOReservationJDBC().findByEtat(13, "CONFIRMATION");
        System.out.println(reservations);*/
        EtatChambre etatChambre = new EtatChambre("RESERVATION", reservations.get(0).getDateDepart(), reservations.get(0).getDateArrivee());
        System.out.println(new DAOReservationJDBC().insertHistoriqueReservation(reservations.get(0).getNumReservation(), etatChambre));
    }

    public static void testUpdateLiensTypeService() {
        List<Reservation> reservations = new DAOReservationJDBC().findByEtat(13, "CONFIRMATION");
        System.out.println(reservations);
        List<TypeService> services = new DAOTypeServiceJDBC().findAll();
        //System.out.println(new DAOReservationJDBC().updateLiensTypeService(reservations.get(0).getNumReservation(), services));
    }

    public static void testFindHistoriqueClient() {
        System.out.println(new DAOReservationJDBC().findHistoriqueClient(1));
    }

    public static void testVisitor() {
        Reservation r = new DAOReservationJDBC().getById(9);

        r.getDetailFacture(new SimpleFacturationVisitor(), new ReductionAnciennete());

        for (Map.Entry<String, Float> entry : r.getFacture().entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void testEmail() {
        try {
            Email email = new Email("Test", new DAOClientJDBC().getById(38));
            Reservation r = new DAOReservationJDBC().getById(9);
            r.getDetailFacture(new SimpleFacturationVisitor(), new ReductionAnciennete());
            email.facture(r);
        }  catch (NullPointerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }catch (AddressException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void count() {
        System.out.println(new DAOReservationJDBC().count(34));
    }

}
