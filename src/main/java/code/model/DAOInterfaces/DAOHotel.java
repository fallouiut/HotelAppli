package code.model.DAOInterfaces;

import code.Chambre;
import code.Hotel;
import code.TypeService;

import java.util.Set;

/**
 * Created by Vincent on 03/05/2019.
 */
public interface DAOHotel extends DAO<Hotel, Integer> {

    public int getNbHotels();
    public Set<TypeService> getServicesById(int numHotel);
    public Set<Chambre> getChambresById(int numHotel);
    public boolean deleteServices(Hotel obj);
    public boolean updateServices(Hotel obj);
    public void insertServices(int numHotel, Set<TypeService> services);
    public boolean updateChambres(Hotel obj);
    public boolean deleteChambres(Hotel obj);

}
