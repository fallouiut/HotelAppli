package code.model.DAOInterfaces;

import code.Client;
import code.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DAOClient extends DAO<Client, Integer> {

    public Map<Client, Reservation> findByHotel(Integer numHotel);

}