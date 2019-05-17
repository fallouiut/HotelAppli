package code.model.DAOInterfaces;

import code.Admin;
import code.Hotel;

import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 01/05/2019.
 */
public interface DAOAdmin extends DAO <Admin, Integer> {

    /*Ici on met les fonctions de récupération de données depuis la base spécifiques aux admins*/

    /**
     * A la connexion d'un admin, vérifier si celui-ci existe et renvoyer un objet Admin si oui
     * @param username le nom d'utilisateur saisi
     * @param password
     * @return l'objet Admin ou null s'il n'existe pas
     */
    public Admin findByUsernameAndPassword(String username, String password);

    public boolean deleteHotelsGeres(Admin admin);
    public boolean deleteDroits(Admin admin);
    public void insertDroits(int numAdmin, Map<String, Boolean> droits);
    public void insertHotelsGeres(int numAdmin, List<Integer> hotelsGeres);
    public boolean updateDroits(Admin admin);
    public boolean updateHotelsGeres(Admin admin);
    public List<Hotel> getHotelsById(Integer id);
    public Map<String, Boolean> initDroits();
    public Map<String, Boolean> getDroits(int numAdmin);
    public int getNbAdmins();
    public List<Integer> getNumHotels(int numAdmin);
}
