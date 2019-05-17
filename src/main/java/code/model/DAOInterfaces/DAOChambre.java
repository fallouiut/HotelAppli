package code.model.DAOInterfaces;

import code.Chambre;
import code.EtatChambre;
import code.Hotel;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.util.Set;


/**
 * Created by Vincent on 03/05/2019.
 */

public interface DAOChambre extends DAO<Chambre, Pair<Integer, Integer>> {

    public int getNbChambres();
    public Set<String> getTypeChambres();
    public Chambre createChambre(ResultSet resultSet);
    public EtatChambre getEtatChambre(Pair<Integer, Integer> idChambre);
    public void insertEntreeHistorique(Chambre obj);
    public boolean deleteHistoriqueChambre(Chambre chambre);
    public boolean deleteReservationChambre(Chambre chambre);

}
