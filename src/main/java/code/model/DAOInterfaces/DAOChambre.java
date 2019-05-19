package code.model.DAOInterfaces;

import code.Chambre;
import code.EtatChambre;
import code.Hotel;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Vincent on 03/05/2019.
 */

public interface DAOChambre extends DAO<Chambre, Pair<Integer, Integer>> {

    public int getNbChambres();
    public List<String> findTypesChambres();
    public List<String> getTypesChambres();
    public Chambre createChambre(ResultSet resultSet);
    public EtatChambre getEtatChambre(Pair<Integer, Integer> idChambre);
    public void insertEntreeHistorique(Chambre obj);
    public boolean deleteHistoriqueChambre(Chambre chambre);
    public boolean deleteReservationChambre(Chambre chambre);

    public Integer getMaxNumChambre(Integer numHotel, Integer etage);
    public Integer getNbChambresParType(Integer numHotel, String type);

}
