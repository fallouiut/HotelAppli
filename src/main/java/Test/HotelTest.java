package Test;

import code.Chambre;
import code.Hotel;
import code.Reservation;
import code.TypeService;
import code.model.DAOJDBC.DAOHotelJDBC;

import code.model.DAOJDBC.DAOTypeServiceJDBC;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vincent on 03/05/2019.
 */
public class HotelTest {

    private static DAOHotelJDBC daoHotelJDBC = new DAOHotelJDBC();

    @Test
    public void testInsertHotel() {

        Hotel hotel = new Hotel();
        hotel.setNom("Hotel random");
        hotel.setAdresse("Quelque part sur la planete terre");
        hotel.setVille("UneVille");
        hotel.setLatitude(23.5465f);
        hotel.setLongitude(33.54654f);

        Set<TypeService> services = new HashSet<>();
        services.add(new DAOTypeServiceJDBC().getById("Restaurant"));
        services.add(new DAOTypeServiceJDBC().getById("Piscine"));
        hotel.setServices(services);
        Set<Chambre> chambres = new HashSet<Chambre>();/*
        chambres.add(new Chambre(101, "OPEN", "Standard"));
        chambres.add(new Chambre(102, "OPEN", "Confort"));*/
        hotel.setChambres(chambres);

        Hotel hotelInsert = daoHotelJDBC.insert(hotel);
        assertTrue(hotel == hotelInsert);
    }

    @Test
    public void testUpdateHotel() {

        Hotel hotel = daoHotelJDBC.getById(12);
        hotel.setAdresse("quelque part");
        //hotel.getChambres().add(new Chambre(103, hotel, "OPEN", "Luxe"));
        assertTrue(daoHotelJDBC.update(hotel));
    }

    @Test
    public void testUpdateServicesHotel() {
        Hotel hotel = daoHotelJDBC.getById(12);
        System.out.println(hotel);
        Set<TypeService> services = new HashSet<>();
        services.add(new DAOTypeServiceJDBC().getById("Petit-dejeuner"));
        services.add(new DAOTypeServiceJDBC().getById("Restaurant"));
        hotel.setServices(services);
        daoHotelJDBC.updateServices(hotel);
    }

    @Test
    public void testFindAllHotel() {
        List<Hotel> hotels = daoHotelJDBC.findAll();
        for(Hotel hotel : hotels) System.out.println(hotel);
        assertTrue(hotels.size() == daoHotelJDBC.getNbHotels());
    }

    //TODO : ne marche pas pour le moment à cause de ReservationChambre, voir plus tard
    @Test
    public void testDeleteHotel() {
        Hotel hotel = daoHotelJDBC.getById(12);
        assertTrue(daoHotelJDBC.delete(hotel));
    }

    public static void main(String args[]) {
        Hotel h = daoHotelJDBC.getById(1);

        List<Reservation> reservations = h.getReservations();

        for(Reservation r: reservations) {
            System.out.println(r.toString());
        }
    }

}
