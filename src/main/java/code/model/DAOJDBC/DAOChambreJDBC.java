package code.model.DAOJDBC;

import code.*;
import code.model.ConnexionUnique;
import code.model.DAOInterfaces.DAOChambre;
import javafx.util.Pair;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Vincent on 03/05/2019.
 */
public class DAOChambreJDBC implements DAOChambre {

    private static Connection connection = ConnexionUnique.getInstance().getConnection();
    private List<String> typesChambres = findTypesChambres();

    @Override
    public List<String> getTypesChambres() {
        return typesChambres;
    }

    @Override
    public boolean deleteHistoriqueChambre(Chambre chambre) {
        try {
            if(chambre != null) {
                String query = "DELETE FROM Historique WHERE num_c = ? AND num_h = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, chambre.getNumChambre());
                statement.setInt(2, chambre.getNumHotel());
                int nb = statement.executeUpdate();
                if(nb < 0) throw new SQLException("delete Historique didnt work");

                return nb > 0;

            } else throw new NullPointerException("Delete historique from null chambre ?");
        } catch (Exception e) {
            System.err.println("DAOChambreJDBC.deleteHistoriqueChambre");;
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteReservationChambre(Chambre chambre) {
        try {
            if(chambre != null) {
                String query = "DELETE FROM ReservationChambre WHERE num_c = ? AND num_h = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, chambre.getNumChambre());
                statement.setInt(2, chambre.getNumHotel());
                int nb = statement.executeUpdate();
                if(nb < 0) throw new SQLException("delete ReservationChambre didnt work");

                return nb > 0;

            } else throw new NullPointerException("Delete ReservationChambre from null chambre ?");
        } catch (Exception e) {
            System.err.println("DAOChambreJDBC.deleteReservationChambre");;
            e.printStackTrace();
        }
        return false;
    }

    @Override
    //TODO : D'ABORD DELETE LES OCCURENCES DE CHAMBRE DANS LES ASSOCIATIONS (HISTORIQUE, RESERVATIONCHAMBRE)
    public boolean delete(Chambre obj) {
        if(!(obj == null)) {
            System.out.println(deleteHistoriqueChambre(obj));
            System.out.println(deleteReservationChambre(obj));
            String deleteQuery = "DELETE FROM Chambre WHERE num_c=?";
            try {
                PreparedStatement ps = connection.prepareStatement(deleteQuery);
                ps.setInt(1, obj.getNumChambre());
                int nb = ps.executeUpdate();

                ps.close();
                return (nb == 1);

            } catch (SQLException sqle) {
                System.err.println("DAOChambreJDBC.delete");
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public EtatChambre getEtatChambre(Pair<Integer, Integer> idChambre) {
        String getEtatChambreQuery = "SELECT * FROM Historique WHERE num_h = ? AND num_c = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(getEtatChambreQuery);
            ps.setInt(1, idChambre.getKey());
            ps.setInt(2, idChambre.getValue());
            ResultSet rs = ps.executeQuery();

            EtatChambre etatChambre = new EtatChambre();
            boolean estIndisponible = false;
            while (rs.next()) {
                Date currentDate = new Date();
                if (currentDate.after(rs.getDate("date_deb")) && currentDate.before(rs.getDate("date_fin"))) {
                    estIndisponible = true;
                    etatChambre.setNomEtat(rs.getString("type_hist"));
                    etatChambre.setDateDebut(rs.getDate("date_deb").toLocalDate());
                    etatChambre.setDateFin(rs.getDate("date_fin").toLocalDate());
                }
            }
            if (!estIndisponible) {
                etatChambre.setNomEtat("OPEN");
            }

            return etatChambre;

        } catch (SQLException sqle) {
            System.err.println("DAOChambreJDBC.getEtatChambre");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public Chambre createChambre(ResultSet resultSetChambres) {
        try {
            String query2 = "SELECT * FROM TypeChambre WHERE nom_t = ?";
            String typeChambre = resultSetChambres.getString("nom_t");
            PreparedStatement ps = connection.prepareStatement(query2);
            ps.setString(1, typeChambre);
            ResultSet rsTypeChambre = ps.executeQuery();

            if (rsTypeChambre.next()) {
                int numChambre = resultSetChambres.getInt("num_c");
                //TODO : VOIR SI LE NUMERO D HOTEL EST SUFFISANT DANS CHAMBRE POUR EVITER PB DE BOUCLE INFINIE
                int numHotel = resultSetChambres.getInt("num_h");

                EtatChambre etat = getEtatChambre(new Pair<>(numHotel, numChambre));

                Chambre chambre = new Chambre(numChambre, numHotel, etat, typeChambre, rsTypeChambre.getInt("nbLits_t"), rsTypeChambre.getFloat("prix_t"));

                if (rsTypeChambre.getBoolean("telephone_t")) {
                    chambre.addOption(new OptionTelephone());
                }
                if (rsTypeChambre.getBoolean("tv_t")) {
                    chambre.addOption(new OptionTV());
                }
                ps.close();
                return chambre;
            }
        } catch (SQLException sqle) {
            System.err.println("DAOChambreJDBC.createChambre");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Chambre> findAll() {

        String query1 = "SELECT * FROM Chambre";
        try {
            ResultSet rsChambre = connection.createStatement().executeQuery(query1);
            List<Chambre> chambres = new ArrayList<>();

            while (rsChambre.next()) {
                chambres.add(createChambre(rsChambre));
            }
            return chambres;
        } catch(SQLException sqle) {
            System.err.println("DAOChambreJDBC.findAll");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public Chambre getById(Pair<Integer, Integer> id) {
        if (id.getKey() != null && id.getValue() != null) {
            String getByIdQuery = "SELECT * FROM Chambre WHERE num_h = ? AND num_c = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(getByIdQuery);
                ps.setInt(1, id.getKey());
                ps.setInt(2, id.getValue());
                ResultSet rsChambre = ps.executeQuery();

                if(rsChambre.next()) {
                    return createChambre(rsChambre);
                }
            } catch (SQLException sqle) {
                System.err.println("DAOChambreJDBC.getById");
                sqle.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public void insertEntreeHistorique(Chambre obj) {

        if (obj != null) {
            String insertHistoriqueQuery = "INSERT INTO Historique(num_h, num_c, type_hist, date_deb, date_fin) VALUES(?,?,?,?,?)";
            try {
                PreparedStatement ps = connection.prepareStatement(insertHistoriqueQuery);
                ps.setInt(1, obj.getNumHotel());
                ps.setInt(2, obj.getNumChambre());
                ps.setString(3, obj.getEtat().getNomEtat());
                ps.setDate(4, java.sql.Date.valueOf(obj.getEtat().getDateDebut()));
                ps.setDate(5, java.sql.Date.valueOf(obj.getEtat().getDateFin()));

                int resultInsertHistorique = ps.executeUpdate();

                if (resultInsertHistorique == 0) {
                    throw new SQLException("Insert Historique echouee");
                }

            } catch (SQLException sqle) {
                System.err.println("DAOChambreJDBC.insertEntreeHistorique");
                sqle.printStackTrace();
            }
        }
    }

    @Override
    public Chambre insert(Chambre obj) {
        if(obj != null && getTypesChambres().contains(obj.getType())){
            String insertQuery = "INSERT INTO Chambre(num_h, num_c, nom_t) VALUES(?,?,?)";
            try{
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setInt(1, obj.getNumHotel());
                ps.setInt(2, obj.getNumChambre());
                ps.setString(3, obj.getType());
                int nb = ps.executeUpdate();
                ps.close();

                if (obj.getEtat().getDateDebut() != null) {
                    insertEntreeHistorique(obj);
                }

                return (nb == 1) ? obj : null;

            } catch (SQLException sqle) {
                System.out.println("DAOChambreJDBC.insert()");
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean update(Chambre obj) {
        if(obj != null && typesChambres.contains(obj.getType())){
            //si la chambre existe déjà
            if (getById(new Pair<>(obj.getNumHotel(), obj.getNumChambre())) != null) {
                String updateQuery = "UPDATE Chambre SET num_h=?, num_c=?, nom_t=? WHERE num_h=? AND num_c=?";
                try {
                    PreparedStatement ps = connection.prepareStatement(updateQuery);
                    ps.setInt(1, obj.getNumHotel());
                    ps.setInt(2, obj.getNumChambre());
                    ps.setString(3, obj.getType());
                    ps.setInt(4,obj.getNumHotel());
                    ps.setInt(5, obj.getNumChambre());
                    int nb = ps.executeUpdate();
                    ps.close();

                    if (obj.getEtat().getDateDebut() != null) {
                        insertEntreeHistorique(obj);
                    }
                } catch (SQLException sqle) {
                    System.err.println("DAOChambreJDBC.update");
                    sqle.printStackTrace();
                }
            } else {
                insert(obj);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getNbChambres() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM Chambre");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException sqle) {
            System.err.println("DAOChambreJDBC.getNbChambres");
            sqle.printStackTrace();
        }
        return -1;
    }

    public List<String> findTypesChambres() {
        List<String> types = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT nom_t FROM TypeChambre");
            while(rs.next()) {
                types.add(rs.getString("nom_t"));
            }
            return types;
        } catch (SQLException sqle) {
            System.err.println("DAOChambreJDBC.findTypesChambres");
            sqle.printStackTrace();
        }
       return null;
    }

    @Override
    public Integer getMaxNumChambre(Integer numHotel, Integer etage) {
        if (numHotel != null && etage != null) {
            String queryGetMaxNumChambre = "SELECT * FROM `Chambre` WHERE num_c IN (SELECT MAX(num_c) FROM Chambre WHERE num_h = ? AND num_c LIKE ?)";
            try {
                PreparedStatement ps = connection.prepareStatement(queryGetMaxNumChambre);
                ps.setInt(1, numHotel);
                ps.setString(2, etage + "%");

                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("num_c");
                }
            } catch (SQLException sqle) {
                System.err.println("DAOChambreJDBC.getMaxNumChambre");
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Integer getNbChambresParType(Integer numHotel, String type) {
        if (numHotel != null && type != null) {
            String queryGetNbChambresParType = "SELECT COUNT(*) FROM Chambre WHERE num_h = ? AND nom_t = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(queryGetNbChambresParType);
                ps.setInt(1, numHotel);
                ps.setString(2, type);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException sqle) {
                System.err.println("DAOChambreJDBC.getNbChambresParType");
                sqle.printStackTrace();
            }
        }
        return null;
    }
}
