package Test;

import code.Chambre;
import code.EtatChambre;
import code.model.DAOJDBC.DAOChambreJDBC;
import javafx.util.Pair;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vincent on 03/05/2019.
 */
public class ChambreTest {

    private DAOChambreJDBC daoChambreJDBC = new DAOChambreJDBC();

    @Test
    public void testInsertChambre() {

        Chambre chambre = new Chambre();
        chambre.setNumHotel(12);
        chambre.setNumChambre(213);
        EtatChambre etatChambre = new EtatChambre("TRAVAUX", LocalDate.of(2015,12,20), LocalDate.of(2020,2,2));
        chambre.setType("Standard");
        chambre.setEtat(etatChambre);
        Chambre chambreInsert = daoChambreJDBC.insert(chambre);
        assertTrue(chambre == chambreInsert);
    }

    @Test
    public void testUpdateChambre() {

        Chambre chambre = daoChambreJDBC.getById(new Pair<>(12,213));
        System.out.println(chambre);
        chambre.setEtat(new EtatChambre("RESERVEE", LocalDate.of(2025,12,20), LocalDate.of(2026,2,2)));
        assertTrue(daoChambreJDBC.update(chambre));
    }

    @Test
    public void testFindAllChambres() {
        List<Chambre> chambres = daoChambreJDBC.findAll();
        assertTrue(chambres.size() == daoChambreJDBC.getNbChambres());
        System.out.println(chambres);
    }

    @Test
    public void testDeleteChambre() {
        Chambre chambre = daoChambreJDBC.getById(new Pair<>(12,213));
        assertTrue(daoChambreJDBC.delete(chambre));
    }


    public static void main(String args[]) {
        Chambre chambre = new DAOChambreJDBC().getById(new Pair<>(12, 104));
        System.out.println(new DAOChambreJDBC().deleteHistoriqueChambre(chambre) ?  "true": "false");
    }

    @Test
    public void testGetMaxNumChambre() {
        System.out.println(daoChambreJDBC.getMaxNumChambre(12, 1));
    }
}
