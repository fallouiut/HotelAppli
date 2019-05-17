package code.model.DAOJDBC;

import code.Client;
import code.Reservation;
import code.model.ConnexionUnique;
import code.model.DAOInterfaces.DAOClient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOClientJDBC implements DAOClient {

    private static Connection connection = ConnexionUnique.getInstance().getConnection();

    @Override
    public boolean delete(Client obj) {
        if(!(obj == null)) {
            String query = "DELETE FROM Client where num_cl = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, obj.getNum());

                return (statement.executeUpdate() == 1);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Client> findAll() {
        String query = "SELECT * FROM Client";
        try {
            ResultSet resultSet = ConnexionUnique.getInstance().getConnection().createStatement().executeQuery(query);

            List<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                clients.add(new Client (
                    resultSet.getInt("num_cl"),
                    resultSet.getString("nom_cl"),
                    resultSet.getString("prenom_cl"),
                    resultSet.getString("nomEntreprise"),
                    resultSet.getString("telephone_cl"),
                    resultSet.getString("mail_cl"),
                    resultSet.getString("pseudo"),
                    resultSet.getString("motdepasse")
                ));
            }
            return clients;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public Client getById(Integer integer) {
        if(!integer.equals("") && !integer.equals(null)) {
            String query = "SELECT * FROM Client where num_cl = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, integer);
                ResultSet resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    return new Client (
                        resultSet.getInt("num_cl"),
                        resultSet.getString("nom_cl"),
                        resultSet.getString("prenom_cl"),
                        resultSet.getString("nomEntreprise"),
                        resultSet.getString("telephone_cl"),
                        resultSet.getString("mail_cl"),
                        resultSet.getString("pseudo"),
                        resultSet.getString("motdepasse")
                    );
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Client insert(Client obj) {
        if(!(obj== null)){
            String query = "INSERT INTO Client (nom_cl, prenom_cl, nomEntreprise, telephone_cl, mail_cl, pseudo, motdepasse) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try{
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, obj.getNom());
                statement.setString(2, obj.getPrenom());
                statement.setString(3, obj.getNomEnteprise());
                statement.setString(4, obj.getTelephone());
                statement.setString(5, obj.getMail());
                statement.setString(6, obj.getPseudo());
                statement.setString(7, obj.getMotDePasse());

                return (statement.executeUpdate() == 1) ? obj: null;

            } catch (SQLException sqle) {
                System.out.println("DAOClientJDBC.insert()");
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean update(Client obj) {
        if(!(obj== null)){
            try{
                String query = "UPDATE Client SET nom_cl = ?, prenom_cl = ?, nomEntreprise = ?, telephone_cl = ?, mail_cl = ?, pseudo =  ?, motdepasse = ? WHERE num_cl = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, obj.getNom());
                statement.setString(2, obj.getPrenom());
                statement.setString(3, obj.getNomEnteprise());
                statement.setString(4, obj.getTelephone());
                statement.setString(5, obj.getMail());
                statement.setString(6, obj.getPseudo());
                statement.setString(7, obj.getMotDePasse());
                statement.setInt(8, obj.getNum());

                return (statement.executeUpdate() == 1);

            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Map<Client, Reservation> findByHotel(Integer numHotel) {
        if (numHotel != null) {
            try {
                String getInfosClientsQuery = "SELECT DISTINCT C.*, R.* FROM Client C JOIN Reservation R ON C.num_cl = R.num_cl";
                getInfosClientsQuery += " JOIN ReservationChambre RC ON R.num_r = RC.num_r WHERE num_h = ?";
                getInfosClientsQuery += " AND R.dateAr_r < CURDATE() AND R.dateDep_r > CURDATE()" /*AND R.etat_r = 'CONFIRMATION' ??? */;
                PreparedStatement ps = connection.prepareStatement(getInfosClientsQuery);
                ps.setInt(1, numHotel);
                ResultSet resultSet = ps.executeQuery();

                Map<Client, Reservation> result = new HashMap<>();
                while (resultSet.next()) {
                    Client client = new Client();
                    client.setNum(resultSet.getInt("num_cl"));
                    client.setNom(resultSet.getString("nom_cl"));
                    client.setPrenom(resultSet.getString("prenom_cl"));
                    client.setNomEnteprise(resultSet.getString("nomEntreprise"));

                    Reservation reservation = new Reservation(
                            resultSet.getInt("num_r"),
                            resultSet.getDate("dateAr_r").toLocalDate(),
                            resultSet.getDate("dateDep_r").toLocalDate(),
                            resultSet.getInt("nbPersonnes_r"),
                            resultSet.getString("etat_r"),
                            resultSet.getFloat("prixTotal_r"),
                            resultSet.getFloat("reduction_r"),
                            null,
                            null,
                            null
                    );
                    result.put(client, reservation);
                }
                return result;

            } catch (SQLException sqle) {
                System.err.println("DAOClientJDBC.findByHotel");
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public float getTotlalDepenses(Integer numClient, LocalDate deb, LocalDate fin) {
        if(numClient != null) {
            try {
                String query = "SELECT SUM(prixTotal_r) FROM Reservation WHERE num_cl = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, numClient);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return resultSet.getInt(1);
                }

            } catch (SQLException sqle) {
                System.err.println("DAOClientJDBC.getTotalDepenses");
                sqle.printStackTrace();
            }
        }
        return -1;
    }
}
