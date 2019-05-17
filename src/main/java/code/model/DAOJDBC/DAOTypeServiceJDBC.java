package code.model.DAOJDBC;

import code.Reservation;
import code.TypeService;
import code.model.ConnexionUnique;
import code.model.DAOInterfaces.DAOTypeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOTypeServiceJDBC implements DAOTypeService {

    private static Connection connection = ConnexionUnique.getInstance().getConnection();

    @Override
    public boolean delete(TypeService obj) {
        if(!(obj == null)) {
            String query = "DELETE FROM TypeService where nom_s = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, obj.getNom());

                return (statement.executeUpdate() == 1);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<TypeService> findAll() {
        String query = "SELECT * FROM TypeService";
        try {
            ResultSet resultSet = ConnexionUnique.getInstance().getConnection().createStatement().executeQuery(query);

            List<TypeService> services = new ArrayList<>();
            while (resultSet.next()) {
                services.add(new TypeService (
                        resultSet.getString("nom_s"),
                        resultSet.getDouble("prix_s")
                    )
                );
            }
            return services;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public TypeService getById(String s) {
        if(!s.equals("") && !s.equals(null)) {
            String query = "SELECT * FROM TypeService where nom_s = ?";

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, s);
                ResultSet resultSet = statement.executeQuery();

                TypeService service = null;

                while (resultSet.next()) {
                    service = new TypeService(
                            resultSet.getString("nom_s"),
                            resultSet.getDouble("prix_s")
                    );
                }
                return service;
            } catch (SQLException sqle) {
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public TypeService insert(TypeService obj) {
        if(!(obj== null)){
            try{
                String query = "INSERT INTO TypeService (nom_s, prix_s)"
                        + "VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, obj.getNom());
                statement.setDouble(2, obj.getPrix());

                return (statement.executeUpdate() == 1) ? obj: null;

            } catch (SQLException sqle) {
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean update(TypeService obj) {
        if(!(obj== null)){
            String query = "UPDATE TypeService SET nom_s = ?, prix_s = ? WHERE nom_s = ?";
            try{
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, obj.getNom());
                statement.setDouble(2, obj.getPrix());
                statement.setString(3, obj.getNom());

                return (statement.executeUpdate() == 1);

            } catch (SQLException sqle) {
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return false;
    }
}
