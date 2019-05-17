package code.model.DAOJDBC;

import code.Admin;
import code.Hotel;
import code.TypeAcces;
import code.model.ConnexionUnique;
import code.model.DAOInterfaces.DAOAdmin;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

/**
 * Created by Vincent on 01/05/2019.
 */
public class DAOAdminJDBC implements DAOAdmin {

    private static Connection connection = ConnexionUnique.getInstance().getConnection();
    private static List<TypeAcces> typesAcces = new DAOTypeAccesJDBC().findAll();

    public static List<TypeAcces> getTypesAcces() {
        return typesAcces;
    }

    @Override
    public boolean delete(Admin obj) {
        if(obj != null) {
            if (deleteDroits(obj) && deleteHotelsGeres(obj)) {
                String deleteQuery = "DELETE FROM Admin WHERE num_a=?";
                try {
                    PreparedStatement ps = connection.prepareStatement(deleteQuery);
                    ps.setInt(1, obj.getNumAdmin());
                    int deleteAdminResult = ps.executeUpdate();

                    if (deleteAdminResult == 0) {
                        throw new SQLException("Insertion admin echouee");
                    }

                    return (deleteAdminResult == 1);

                } catch (SQLException sqle) {
                    System.err.println("DAOAdminJDBC.delete");
                    sqle.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public List<Admin> findAll() {
        String query = "SELECT * FROM Admin";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);

            List<Admin> admins = new ArrayList<>();
            while (resultSet.next()) {

                Map<String, Boolean> droits = getDroits(resultSet.getInt("num_a"));

                admins.add(new Admin (
                        resultSet.getInt("num_a"),
                        resultSet.getString("identifiant"),
                        resultSet.getString("motdepasse"),
                        getNumHotels(resultSet.getInt("num_a")),
                        droits));
            }
            return admins;
        } catch(SQLException sqle) {
            System.err.println("DAOAdminJDBC.findAll");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public Admin getById(Integer id) {
        if (id != null) {
            String getByIdQuery = "SELECT * FROM Admin where num_a = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(getByIdQuery);
                ps.setInt(1, id);
                ResultSet resultSet = ps.executeQuery();

                if(resultSet.next()) {

                    Map<String, Boolean> droits = getDroits(resultSet.getInt("num_a"));

                    return new Admin(
                            resultSet.getInt("num_a"),
                            resultSet.getString("identifiant"),
                            resultSet.getString("motdepasse"),
                            getNumHotels(id),
                            droits);
                }
            } catch (SQLException sqle) {
                System.err.println("DAOAdminJDBC.getById");
                sqle.printStackTrace();
            }

        }
        return null;
    }



    @Override
    public Admin insert(Admin obj) {
        if(obj != null){
            String insertAdminQuery = "INSERT INTO Admin (identifiant, motdepasse) VALUES (?, ?)";

            try{
                PreparedStatement statement = connection.prepareStatement(insertAdminQuery, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, obj.getIdentifiant());
                statement.setString(2, md5(obj.getMdp()));

                int insertAdminResult = statement.executeUpdate();

                if (insertAdminResult == 0) {
                    throw new SQLException("Insertion admin echouee");
                }

                ResultSet generatedKeys = statement.getGeneratedKeys();
                statement.close();
                if (generatedKeys.next()) {
                    int lastInsertedId = generatedKeys.getInt(1);
                    insertDroits(lastInsertedId, obj.getDroits());
                    insertHotelsGeres(lastInsertedId, obj.getHotelsGeres());
                    return obj;
                }

                return obj;

            } catch (SQLException sqle) {
                System.out.println("DAOAdminJDBC.insert()");
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean update(Admin obj) {
        if(obj != null){
            String updateAdminQuery = "UPDATE Admin SET identifiant=?, motdepasse=? WHERE num_a=?";
            try {
                PreparedStatement ps = connection.prepareStatement(updateAdminQuery);
                ps.setString(1, obj.getIdentifiant());
                ps.setString(2, md5(obj.getMdp()));
                ps.setInt(3, obj.getNumAdmin());

                int updateAdminResult = ps.executeUpdate();

                if (updateAdminResult == 0) {
                    throw new SQLException("Update Admin echouee");
                }
                ps.close();

                return true;
                //return updateDroits && updateHotels ???
            } catch (SQLException sqle) {
                System.err.println("DAOAdminJDBC.update");
                sqle.printStackTrace();
            }
        }
        return false;
    }

    public String md5(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("DAOAdminJDBC.md5");
            nsae.printStackTrace();
        }
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    @Override
    public boolean deleteHotelsGeres(Admin obj) {
        if (obj != null) {
            if (obj.getHotelsGeres().size() != 0) {
                try {
                    String deleteHotelsGeresQuery = "DELETE FROM Gerer WHERE num_a = ?";

                    PreparedStatement ps = connection.prepareStatement(deleteHotelsGeresQuery);
                    ps.setInt(1, obj.getNumAdmin());

                    int deleteGererResult = ps.executeUpdate();

                    if (deleteGererResult == 0) {
                        throw new SQLException("Delete Gerer echouee");
                    }
                    return true;
                } catch(SQLException sqle) {
                    System.err.println("DAOAdminJDBC.deleteHotelsGeres");
                    sqle.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteDroits(Admin obj) {
        if (obj != null) {
            if (obj.getDroits().size() != 0) {
                try {
                    String deleteDroitssQuery = "DELETE FROM Posseder WHERE num_a = ?";

                    PreparedStatement ps = connection.prepareStatement(deleteDroitssQuery);
                    ps.setInt(1, obj.getNumAdmin());

                    int deletePossederResult = ps.executeUpdate();

                    if (deletePossederResult == 0) {
                        throw new SQLException("Delete Posseder echouee");
                    }
                } catch (SQLException sqle) {
                    System.err.println("DAOAdminJDBC.deleteDroits");
                    sqle.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Boolean> initDroits() {
        Map<String, Boolean> droits = new HashMap<>();
        for (TypeAcces typeAcces : typesAcces) {
            droits.put(typeAcces.getTypeAcces(), false);
        }
        return droits;
    }

    @Override
    public Map<String, Boolean> getDroits(int numAdmin) {
        String queryGetDroits = "SELECT T.* FROM TypeAcces AS T JOIN Posseder AS P ON T.nom_a = P.nom_a";
        queryGetDroits += " WHERE num_a = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(queryGetDroits);
            ps.setInt(1, numAdmin);
            ResultSet resultGetDroits = ps.executeQuery();

            Map<String, Boolean> droits = initDroits();
            while(resultGetDroits.next()) {
                droits.put(resultGetDroits.getString("nom_a"), true);
            }

            return droits;
        } catch (SQLException sqle) {
            System.err.println("DAOAdminJDBC.getDroits");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getNumHotels(int numAdmin) {
        String getHotelsQuery = "SELECT * FROM Hotel H JOIN Gerer G ON G.num_h = H.num_h WHERE num_a = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(getHotelsQuery);
            ps.setInt(1, numAdmin);
            ResultSet resultSet = ps.executeQuery();

            List<Integer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getInt("num_h"));
            }
            return result;

        } catch(SQLException sqle) {
            System.err.println("DAOAdminJDBC.getNumHotels");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertDroits(int numAdmin, Map<String, Boolean> droits) {
        if (droits.size() != 0) {
            String insertDroitsQuery = "INSERT INTO Posseder(num_a, nom_a) VALUES (?,?)";
            try {
                PreparedStatement statement = connection.prepareStatement(insertDroitsQuery);
                for (Map.Entry<String,Boolean> droit : droits.entrySet()) {
                    if (droit.getValue()) {
                        statement.setInt(1, numAdmin);
                        statement.setString(2, droit.getKey());

                        int insertPossederResult = statement.executeUpdate();

                        if (insertPossederResult == 0) {
                            throw new SQLException("Insertion Posseder echouee");
                        }
                    }
                }
            } catch (SQLException sqle) {
                System.err.println("DAOAdminJDBC.insertDroits");
                sqle.printStackTrace();
            }
        }
    }

    @Override
    public void insertHotelsGeres(int numAdmin, List<Integer> hotelsGeres) {
        if (hotelsGeres.size() != 0) {
            String insertHotelsGeresQuery = "INSERT INTO Gerer(num_a, num_h) VALUES (?,?)";
            try {
                PreparedStatement statement = connection.prepareStatement(insertHotelsGeresQuery);
                for (Integer idHotel : hotelsGeres) {
                    statement.setInt(1, numAdmin);
                    statement.setInt(2, idHotel);

                    int insertGererResult = statement.executeUpdate();

                    if (insertGererResult == 0) {
                        throw new SQLException("Insertion Gerer echouee");
                    }
                }
            } catch (SQLException sqle) {
                System.err.println("DAOAdminJDBC.insertHotelsGeres");
                sqle.printStackTrace();
            }
        }
    }

    @Override
    public boolean updateDroits(Admin obj) {
        if (obj != null) {
                deleteDroits(obj);
                insertDroits(obj.getNumAdmin(), obj.getDroits());
                return true;
        }
        return false;
    }

    @Override
    public boolean updateHotelsGeres(Admin obj) {
        if (obj != null) {
            deleteHotelsGeres(obj);
            insertHotelsGeres(obj.getNumAdmin(), obj.getHotelsGeres());
            return true;
        }
        return false;
    }

    @Override
    public Admin findByUsernameAndPassword(String username, String password) {
        try {
            Statement stmt = connection.createStatement();
            String req = "SELECT * FROM Admin WHERE identifiant='" +  username + "' AND motdepasse='" + md5(password) + "'";
            ResultSet rs = stmt.executeQuery(req);
            Admin admin = new Admin();
            if (rs.next()) {

                Map<String, Boolean> droits = getDroits(rs.getInt("num_a"));

                admin.setNumAdmin(rs.getInt("num_a"));
                admin.setIdentifiant(rs.getString("identifiant"));
                admin.setMdp(rs.getString("motdepasse"));
                admin.setHotelsGeres(getNumHotels(rs.getInt("num_a")));
                admin.setDroits(droits);
                stmt.close();
                return admin;
            } else {
                return null;
            }

        } catch (SQLException sqle) {
            System.err.println("DAOAdminJDBC.findByUsernameAndPassword");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public int getNbAdmins() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM Admin");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException sqle) {
            System.err.println("DAOAdminJDBC.getNbAdmins");
            sqle.printStackTrace();
        }
        return -1;
    }

    @Override
    //ATTENTION : CETTE FONCTOIN EST TRES LENTE POUR L'ADMIN SUPREME
    public List<Hotel> getHotelsById(Integer id) {
        String getHotelsQuery = "SELECT * FROM Hotel H JOIN Gerer G ON G.num_h = H.num_h WHERE num_a = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(getHotelsQuery);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            List<Hotel> result = new ArrayList<>();
            DAOHotelJDBC daoHotelJDBC = new DAOHotelJDBC();

            while(resultSet.next()) {
                Hotel hotel = new Hotel(
                        resultSet.getInt("num_h"),
                        resultSet.getString("nom_h"),
                        resultSet.getString("ville_h"),
                        resultSet.getString("adresse_h"),
                        resultSet.getFloat("latitude_h"),
                        resultSet.getFloat("longitude_h"),
                        daoHotelJDBC.getServicesById(resultSet.getInt("num_h")),
                        daoHotelJDBC.getChambresById(resultSet.getInt("num_h")),
                        //null
                        new DAOReservationJDBC().getByHotel(resultSet.getInt("num_h")));
                result.add(hotel);
            }
            return result;

        } catch(SQLException sqle) {
            System.err.println(getClass().getName() + "." + getClass().getEnclosingMethod().getName());
        }
        return null;
    }
}
