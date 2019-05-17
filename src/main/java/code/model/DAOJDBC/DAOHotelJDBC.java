package code.model.DAOJDBC;

import code.Chambre;
import code.Hotel;
import code.TypeService;
import code.model.ConnexionUnique;
import code.model.DAOInterfaces.DAOChambre;
import code.model.DAOInterfaces.DAOHotel;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Vincent on 03/05/2019.
 */
public class DAOHotelJDBC implements DAOHotel {

    private static Connection connection = ConnexionUnique.getInstance().getConnection();
    private DAOChambreJDBC daoChambreJDBC = new DAOChambreJDBC();

    @Override
    public boolean deleteServices(Hotel obj) {
        if (obj != null) {
            if (obj.getServices().size() != 0) {
                try {
                    String deleteServicesQuery = "DELETE FROM Proposer WHERE num_h = ?";

                    PreparedStatement ps = connection.prepareStatement(deleteServicesQuery);
                    ps.setInt(1, obj.getNumHotel());

                    int deleteProposerResult = ps.executeUpdate();

                    if (deleteProposerResult == 0) {
                        throw new SQLException("Delete Proposer echouee");
                    }
                    return true;
                } catch(SQLException sqle) {
                    System.err.println("DAOHotelJDBC.deleteServices");
                    sqle.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteChambres(Hotel obj) {
        for (Chambre chambre : obj.getChambres()) {
            if(!daoChambreJDBC.delete(chambre)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean delete(Hotel obj) {
        if(obj != null) {
            if (deleteServices(obj) && deleteChambres(obj)) {
                String deleteQuery = "DELETE FROM Hotel WHERE num_h=?";
                try {
                    PreparedStatement ps = connection.prepareStatement(deleteQuery);
                    ps.setInt(1, obj.getNumHotel());
                    int nb = ps.executeUpdate();

                    return true;

                } catch (SQLException sqle) {
                    System.err.println("DAOHotelJDBC.delete");
                    sqle.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public Set<Chambre> getChambresById(int numHotel) {
        String getChambresQuery = "SELECT * FROM Chambre WHERE num_h = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(getChambresQuery);
            ps.setInt(1, numHotel);
            ResultSet resultGetChambres = ps.executeQuery();

            Set<Chambre> chambres = new HashSet<>();
            while(resultGetChambres.next()) {
                chambres.add(daoChambreJDBC.getById(new Pair<>(numHotel, resultGetChambres.getInt("num_c"))));
            }

            return chambres;
        } catch (SQLException sqle) {
            System.err.println("DAOHotelJDBC.getChambresById");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<TypeService> getServicesById(int numHotel) {

        String queryGetServices = "SELECT T.* FROM TypeService AS T JOIN Proposer AS P ON T.nom_s = P.nom_s";
        queryGetServices += " WHERE num_h = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(queryGetServices);
            ps.setInt(1, numHotel);
            ResultSet resultGetServices = ps.executeQuery();

            Set<TypeService> services = new HashSet<>();
            while(resultGetServices.next()) {
                services.add(new TypeService(resultGetServices.getString("nom_s"), resultGetServices.getFloat("prix_s")));
            }

            return services;
        } catch (SQLException sqle) {
            System.err.println("DAOHotelJDBC.getServicesById");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Hotel> findAll() {
        String query = "SELECT * FROM Hotel";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);

            List<Hotel> hotels = new ArrayList<>();
            while (resultSet.next()) {

                Set<Chambre> chambres = getChambresById(resultSet.getInt("num_h"));
                Set<TypeService> services = getServicesById(resultSet.getInt("num_h"));

                hotels.add(new Hotel (
                        resultSet.getInt("num_h"),
                        resultSet.getString("nom_h"),
                        resultSet.getString("ville_h"),
                        resultSet.getString("adresse_h"),
                        resultSet.getFloat("latitude_h"),
                        resultSet.getFloat("longitude_h"),
                        services,
                        chambres,
                        new DAOReservationJDBC().getByHotel(resultSet.getInt("num_h"))
                ));
            }
            return hotels;
        } catch(SQLException sqle) {
            System.err.println("DAOHotelJDBC.findAll");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Hotel> findAllLight() {
        String query = "SELECT * FROM Hotel";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);

            List<Hotel> hotels = new ArrayList<>();
            while (resultSet.next()) {

                hotels.add(new Hotel (
                        resultSet.getInt("num_h"),
                        resultSet.getString("nom_h"),
                        resultSet.getString("ville_h"),
                        resultSet.getString("adresse_h"),
                        resultSet.getFloat("latitude_h"),
                        resultSet.getFloat("longitude_h"),
                        null,
                        null,
                        null
                ));
            }
            return hotels;
        } catch(SQLException sqle) {
            System.err.println("DAOHotelJDBC.findAll");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public Hotel getById(Integer id) {
        if (id != null) {
            String getByIdQuery = "SELECT * FROM Hotel where num_h = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(getByIdQuery);
                ps.setInt(1, id);
                ResultSet resultSet = ps.executeQuery();

                if(resultSet.next()) {

                    Set<Chambre> chambres = getChambresById(resultSet.getInt("num_h"));
                    Set<TypeService> services = getServicesById(id);

                    return new Hotel (
                            resultSet.getInt("num_h"),
                            resultSet.getString("nom_h"),
                            resultSet.getString("ville_h"),
                            resultSet.getString("adresse_h"),
                            resultSet.getFloat("latitude_h"),
                            resultSet.getFloat("longitude_h"),
                            services,
                            chambres,
                            new DAOReservationJDBC().getByHotel(resultSet.getInt("num_h"))
                    );
                }
            } catch (SQLException sqle) {
                System.err.println("DAOHotelJDBC.getById");
                sqle.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public Hotel insert(Hotel obj) {
        if(obj != null){
            String insertHotelQuery = "INSERT INTO Hotel(nom_h, ville_h, adresse_h, latitude_h, longitude_h) VALUES(?,?,?,?,?)";
            try{
                PreparedStatement ps = connection.prepareStatement(insertHotelQuery, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, obj.getNom());
                ps.setString(2, obj.getVille());
                ps.setString(3, obj.getAdresse());
                ps.setFloat(4, obj.getLatitude());
                ps.setFloat(5, obj.getLongitude());
                int insertHotelResult = ps.executeUpdate();

                if (insertHotelResult == 0) {
                    throw new SQLException("Insertion hotel echouee");
                }

                //pas besoin de vérifier que le type service existe bien car lors de la création d'un hotel
                //on fera un findAll de TypeService qu'on affichera sur l'interface admin
                ResultSet generatedKeys = ps.getGeneratedKeys();
                ps.close();
                if (generatedKeys.next()) {
                    int lastInsertedId = generatedKeys.getInt(1);
                    insertServices(lastInsertedId, obj.getServices());
                    for(Chambre chambre : obj.getChambres()) {
                        //TODO : remplacer par numHotel dans Chambre
                        chambre.setNumHotel(lastInsertedId);
                        daoChambreJDBC.insert(chambre);
                    }
                    return obj;
                }

            } catch (SQLException sqle) {
                System.out.println("DAOHotelJDBC.insert()");
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean update(Hotel obj) {
        if(obj != null){
            String updateQuery = "UPDATE Hotel SET nom_h=?, ville_h=?, adresse_h=?, latitude_h=?, longitude_h=? WHERE num_h=?";
            try {
                PreparedStatement ps = connection.prepareStatement(updateQuery);
                ps.setString(1, obj.getNom());
                ps.setString(2, obj.getVille());
                ps.setString(3, obj.getAdresse());
                ps.setFloat(4, obj.getLatitude());
                ps.setFloat(5, obj.getLongitude());
                ps.setInt(6, obj.getNumHotel());

                int updateHotelResult = ps.executeUpdate();

                if (updateHotelResult == 0) {
                    throw new SQLException("Update Hotel echouee");
                }
                ps.close();

                if (!updateChambres(obj)) {
                    return false;
                }

                return true;
            } catch (SQLException sqle) {
                System.err.println("DAOHotelJDBC.update");
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean updateChambres(Hotel obj) {
        for (Chambre chambre : obj.getChambres()) {
            if (!daoChambreJDBC.update(chambre)) {
                return false;
            }
        }
        return true;
    }

    public void insertServices(int numHotel, Set<TypeService> services) {

        if (services.size() != 0) {
            String insertServicesQuery = "INSERT INTO Proposer(nom_s, num_h) VALUES (?,?)";
            try {
                PreparedStatement ps2 = connection.prepareStatement(insertServicesQuery);
                for (TypeService service : services) {
                    ps2.setString(1, service.getNom());
                    ps2.setInt(2, numHotel);
                    int insertProposerResult = ps2.executeUpdate();

                    if (insertProposerResult == 0) {
                        throw new SQLException("Insertion Proposer echouee");
                    }
                }
            } catch (SQLException sqle) {
                System.err.println("DAOHotelJDBC.insertServices");
                sqle.printStackTrace();
            }
        }
    }

    public boolean updateServices(Hotel obj) {
        if (obj != null) {
            try {
                if (obj.getServices().size() != 0) {
                    String deleteServicesQuery = "DELETE FROM Proposer WHERE num_h = ?";

                    PreparedStatement ps = connection.prepareStatement(deleteServicesQuery);
                    ps.setInt(1, obj.getNumHotel());

                    int deleteProposerResult = ps.executeUpdate();

                    if (deleteProposerResult == 0) {
                        throw new SQLException("Delete Proposer echouee");
                    }
                }
                insertServices(obj.getNumHotel(), obj.getServices());
                return true;
            } catch (SQLException sqle) {
                System.err.println("DAOHotelJDBC.updateServices");
                sqle.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public int getNbHotels() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM Hotel");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException sqle) {
            System.err.println("DAOHotelJDBC.getNbHotels");
            sqle.printStackTrace();
        }
        return -1;
    }
}
