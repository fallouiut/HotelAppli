package code.model.DAOInterfaces;

import code.Chambre;
import code.EtatChambre;
import code.Reservation;
import code.TypeService;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.List;

public interface DAOReservation extends DAO<Reservation, Integer> {
    public List<Chambre> getChambres(Integer integer);
    public boolean deleteLiensChambre(Reservation reservation, Chambre chambre);
    public boolean insertLiensChambre(Reservation reservation, Chambre chambre);
    public boolean updateLiensChambre(Reservation reservation);

    public List<Reservation> findHistoriqueClient(Integer numClient);
    public List<Reservation> getByHotel(Integer numHotel);

    public List<TypeService> getTypeServices(Integer integer);
    public boolean deleteLiensTypeService(Integer numReservation, TypeService typeService);
    public boolean insertLiensTypeService(Integer numReservation, TypeService typeService);
    public boolean updateLiensTypeService(Integer numReservation, List<TypeService> services);

    public List<Reservation> findByEtat(Integer numHotel, String etat);
    public boolean updateEtatReservation(Integer numReservation, String etat);
    public boolean insertHistoriqueReservation(Integer numReservation, EtatChambre etatChambre);
}
