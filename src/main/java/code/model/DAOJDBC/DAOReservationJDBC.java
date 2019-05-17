package code.model.DAOJDBC;

import code.*;
import code.model.ConnexionUnique;
import code.model.DAOInterfaces.DAOChambre;
import code.model.DAOInterfaces.DAOReservation;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOReservationJDBC implements DAOReservation {

    private static Connection connection = ConnexionUnique.getInstance().getConnection();

    @Override
    public boolean delete(Reservation obj) {
        if(obj != null) {
            System.out.println('1');
            String query = "DELETE FROM Reservation where num_r = ?";
            try {
                System.out.println("2");
                List<Chambre> chambres = obj.getChambres();
                if (obj.getChambres() != null) {
                    for (Chambre chambre : obj.getChambres()) {
                        this.deleteLiensChambre(obj, chambre);
                    }
                }

                List<TypeService> services = obj.getServices();
                if (services != null) {
                    for (TypeService service : obj.getServices()) {
                        this.deleteLiensTypeService(obj.getNumReservation(), service);
                    }
                }

                System.out.println("3");
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, obj.getNumReservation());

                System.out.println("4");
                return (statement.executeUpdate() == 1);
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Reservation> findAll() {

        String query = "SELECT * FROM Reservation";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);

            List<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(new Reservation (
                    resultSet.getInt("num_r"),
                    resultSet.getDate("dateAr_r").toLocalDate(),
                    resultSet.getDate("dateDep_r").toLocalDate(),
                    resultSet.getInt("nbPersonnes_r"),
                    resultSet.getString("etat_r"),
                    resultSet.getFloat("prixTotal_r"),
                    resultSet.getFloat("reduction_r"),
                    new DAOClientJDBC().getById(resultSet.getInt("num_cl")),
                    this.getChambres(resultSet.getInt("num_r")),
                    this.getTypeServices(resultSet.getInt("num_r"))
                ));
            }
            return reservations;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public Reservation getById(Integer integer) {
        if(integer != null) {
            String query = "SELECT * FROM Reservation where num_r = ?";

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, integer);
                ResultSet resultSet = statement.executeQuery();

                Reservation reservation = new Reservation();
                reservation.setNumReservation(integer);

                while(resultSet.next()) {
                    reservation = new  Reservation (
                        resultSet.getInt("num_r"),
                        resultSet.getDate("dateAr_r").toLocalDate(),
                        resultSet.getDate("dateDep_r").toLocalDate(),
                        resultSet.getInt("nbPersonnes_r"),
                        resultSet.getString("etat_r"),
                        resultSet.getFloat("prixTotal_r"),
                        resultSet.getFloat("reduction_r"),
                        new DAOClientJDBC().getById(resultSet.getInt("num_cl")),
                        this.getChambres(resultSet.getInt("num_r")),
                        this.getTypeServices(resultSet.getInt("num_r"))
                    );
                }
                return reservation;
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Reservation insert(Reservation obj) {
        if(!(obj== null)){
            try{
                String query = "INSERT INTO Reservation (num_r, dateAr_r, dateDep_r, nbPersonnes_r, etat_r, reduction_r, num_cl)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, obj.getNumReservation());
                statement.setString(2, obj.getDateArrivee().toString());
                statement.setString(3, obj.getDateDepart().toString());
                statement.setInt(4, obj.getNbPersonnes());
                statement.setString(5, obj.getEtat());
                statement.setFloat(6, obj.getReduction());
                statement.setInt(7, obj.getClient().getNum());

                int nb = statement.executeUpdate();

                if(obj.getChambres() != null) {
                    for(Chambre chambre: obj.getChambres()) {
                        if(!this.insertLiensChambre(obj, chambre)){
                            throw new SQLException("inserting links reservation_chambre failed");
                        }
                    }
                }

                if(obj.getServices() != null) {
                    for(TypeService typeService: obj.getServices()) {
                        if(!this.insertLiensTypeService(obj.getNumReservation(), typeService)){
                            throw new SQLException("inserting links reservation_chambre failed");
                        }
                    }
                }
                return (nb == 1) ? obj: null;

            } catch (SQLException sqle) {
                System.out.println("DAOClientJDBC.insert()");
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean update(Reservation obj) {
        if(!(obj== null)){
            String query = "UPDATE Reservation SET dateAr_r = ?, dateDep_r = ?, nbPersonnes_r = ?, etat_r = ?, prixTotal_r, reduction_r = ?, num_cl = ? WHERE num_r = ?";
            try{
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, obj.getDateArrivee().toString());
                statement.setString(2, obj.getDateDepart().toString());
                statement.setInt(3, obj.getNbPersonnes());
                statement.setString(4, obj.getEtat());
                statement.setFloat(5, obj.getPrixTotal());
                statement.setFloat(6, obj.getReduction());
                statement.setInt(7, obj.getNumReservation());
                statement.setInt(8, obj.getClient().getNum());

                return (statement.executeUpdate() == 1);

            } catch (SQLException sqle) {
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Chambre> getChambres(Integer integer) {
        try {
            if(integer != null) {
                String query = "SELECT num_c, num_h FROM ReservationChambre WHERE num_r = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, integer);
                ResultSet rset = preparedStatement.executeQuery();

                List<Chambre> chambres = new ArrayList<>();

                while (rset.next()) {
                    chambres.add(new DAOChambreJDBC().getById(new Pair<Integer, Integer>(rset.getInt("num_h"), rset.getInt("num_c"))));
                }
                return chambres;
            }
            throw new SQLException("No reservation");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteLiensChambre(Reservation reservation, Chambre chambre) {
        if(reservation != null) {
            try {
                PreparedStatement statement;
                if(chambre != null) {
                    String query = "DELETE FROM ReservationChambre where num_c = ? AND num_h = ? AND num_r = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, chambre.getNumChambre());
                    statement.setInt(2, chambre.getNumHotel());
                    statement.setInt(3, reservation.getNumReservation());
                } else {
                    String query = "DELETE FROM ReservationChambre where num_r = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, reservation.getNumReservation());
                }
                int nb = statement.executeUpdate();

                if(nb < 1) {
                    throw new SQLException("Erreur suppressions");
                }
                return (nb >= 1);

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean insertLiensChambre(Reservation reservation, Chambre chambre) {
        if(chambre != null && reservation != null){
            String query = "INSERT INTO ReservationChambre (num_c, num_h, num_r) VALUES (?, ?, ?)";
            try{
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, chambre.getNumChambre());
                statement.setInt(2, chambre.getNumHotel());
                statement.setInt(3, reservation.getNumReservation());

                return (statement.executeUpdate() == 1);

            } catch (SQLException sqle) {
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean updateLiensChambre(Reservation reservation) {
        if (reservation != null) {
            try {
                if (!deleteLiensChambre(reservation, null)) {
                    throw new SQLException("deleteing links reservation_chambre failed");
                }

                if (reservation.getChambres() != null) {
                    for (Chambre chambre : reservation.getChambres()) {
                        if (!this.insertLiensChambre(reservation, chambre)) {
                            throw new SQLException("inserting links reservation_chambre failed");
                        }
                    }
                }
                return true;
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Reservation> findHistoriqueClient(Integer numClient) {
        if(numClient != null) {
            String query = "SELECT DISTINCT nom_cl, prenom_cl, nomEntreprise, R.*, RC.num_h, H.nom_h FROM Reservation R";
            query += " JOIN Client CL on R.num_cl = CL.num_cl";
            query += " JOIN ReservationChambre RC ON R.num_r = RC.num_r";
            query += " JOIN Chambre C ON RC.num_h = C.num_h";
            query += " JOIN Hotel H ON C.num_h = H.num_h WHERE R.num_cl = ?";

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, numClient);
                ResultSet resultSet = statement.executeQuery();
                List<Reservation> historique = new ArrayList<>();
                while(resultSet.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setNumReservation(resultSet.getInt("num_r"));
                    reservation.setDateArrivee(resultSet.getDate("dateAr_r").toLocalDate());
                    reservation.setDateDepart(resultSet.getDate("dateDep_r").toLocalDate());
                    reservation.setNbPersonnes(resultSet.getInt("nbPersonnes_r"));
                    reservation.setEtat(resultSet.getString("etat_r"));
                    reservation.setPrixTotal(resultSet.getFloat("prixTotal_r"));
                    reservation.setReduction(resultSet.getFloat("reduction_r"));
                    Client client = new Client();
                    client.setNomEnteprise(resultSet.getString("nomEntreprise"));
                    client.setNom(resultSet.getString("nom_cl"));
                    client.setPrenom(resultSet.getString("prenom_cl"));
                    reservation.setClient(client);
                    Hotel hotel = new Hotel();
                    hotel.setNom(resultSet.getString("nom_h"));
                    reservation.setHotel(hotel);
                    historique.add(reservation);
                }
                return historique;
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return null;
    }

    //TODO : PEUT ETRE MIEUX DE FAIRE UNE SEULE REQUETE QUE D'APPELER GETBYID
    @Override
    public List<Reservation> getByHotel(Integer numHotel) {
        try {
            if(numHotel != null) {
                String query = "SELECT * FROM Reservation R JOIN ReservationChambre RC " +
                               "ON R.num_r = RC.num_r WHERE RC.num_h = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, numHotel);
                ResultSet resultSet = statement.executeQuery();

                List<Reservation> reservationsByHotel = new ArrayList<>();
                while (resultSet.next()) {
                    reservationsByHotel.add(new Reservation (
                            resultSet.getInt("num_r"),
                            resultSet.getDate("dateAr_r").toLocalDate(),
                            resultSet.getDate("dateDep_r").toLocalDate(),
                            resultSet.getInt("nbPersonnes_r"),
                            resultSet.getString("etat_r"),
                            resultSet.getFloat("prixTotal_r"),
                            resultSet.getFloat("reduction_r"),
                            new DAOClientJDBC().getById(resultSet.getInt("num_cl")),
                            this.getChambres(resultSet.getInt("num_r")),
                            this.getTypeServices(resultSet.getInt("num_r"))));
                }
                return reservationsByHotel;
            }
            return null;
        } catch (SQLException sqle) {
            System.out.println("DAOReservationJDBC.getByHotel()");
            System.out.println(sqle.getMessage());
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TypeService> getTypeServices(Integer integer) {
        try {
            if(integer != null) {
                String query = "SELECT nom_s FROM Demander WHERE num_r = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, integer);
                ResultSet rset = preparedStatement.executeQuery();
                List<TypeService> services = new ArrayList<>();

                while (rset.next()) {
                    services.add(new DAOTypeServiceJDBC().getById(rset.getString("nom_s")));
                }
                System.out.println(services.size());
                return services;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteLiensTypeService(Integer numReservation, TypeService typeService) {
        if(numReservation != null) {
            try {
                PreparedStatement statement;
                if(typeService != null) {
                    String query = "DELETE FROM Demander WHERE nom_s = ? AND num_r = ?";
                    statement = connection.prepareStatement(query);
                    statement.setString(1, typeService.getNom());
                    statement.setInt(2, numReservation);

                } else {
                    String query = "DELETE FROM Demander WHERE num_r = ?";
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, numReservation);
                }

                int nb = statement.executeUpdate();
                return true;
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean insertLiensTypeService(Integer numReservation, TypeService typeService) {
        if(typeService != null && numReservation != null){
            String query = "INSERT INTO Demander (nom_s, num_r) VALUES (?, ?)";
            try{

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, typeService.getNom());
                statement.setInt(2, numReservation);

                return (statement.executeUpdate() == 1);

            } catch (SQLException sqle) {
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Reservation> findByEtat(Integer numHotel, String etat) {
        try {
            String queryFindByEtat = "SELECT DISTINCT RC.num_h, R.*, nom_cl, prenom_cl, nomEntreprise";
            queryFindByEtat += " FROM Reservation R JOIN Client C ON R.num_cl = C.num_cl";
            queryFindByEtat += " JOIN ReservationChambre RC ON R.num_r = RC.num_r";
            queryFindByEtat += " WHERE num_h = ? AND etat_r = ?";

            PreparedStatement ps = connection.prepareStatement(queryFindByEtat);
            ps.setInt(1, numHotel);
            ps.setString(2, etat);
            ResultSet resultSet = ps.executeQuery();

            List<Reservation> reservations = new ArrayList<>();
            while(resultSet.next()) {
                Hotel hotel = new Hotel();
                hotel.setNumHotel(resultSet.getInt("num_h"));
                Client client = new Client();
                client.setPrenom(resultSet.getString("prenom_cl"));
                client.setNom(resultSet.getString("nom_cl"));
                client.setNomEnteprise(resultSet.getString("nomEntreprise"));
                Reservation reservation = new Reservation();
                reservation.setHotel(hotel);
                reservation.setClient(client);
                reservation.setNumReservation(resultSet.getInt("num_r"));
                reservation.setDateArrivee(resultSet.getDate("dateAr_r").toLocalDate());
                reservation.setDateDepart(resultSet.getDate("dateDep_r").toLocalDate());
                reservation.setNbPersonnes(resultSet.getInt("nbPersonnes_r"));
                reservations.add(reservation);
            }
            return reservations;
        } catch (SQLException sqle) {
            System.err.println("DAOReservationJDBC.findByEtat");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateEtatReservation(Integer numReservation, String etat) {
        if (numReservation != null) {
            try {
                String queryUpdateEtat = "UPDATE Reservation SET etat_r = ? WHERE num_r = ?";
                PreparedStatement ps = connection.prepareStatement(queryUpdateEtat);
                ps.setString(1, etat);
                ps.setInt(2, numReservation);
                int resultUpdate = ps.executeUpdate();
                return resultUpdate == 1;

            } catch (SQLException sqle) {
                System.err.println("DAOReservationJDBC.updateEtatReservation");
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean insertHistoriqueReservation(Integer numReservation, EtatChambre etatChambre) {
        if (numReservation != null) {
            String queryGetNumChambres = "SELECT num_h, num_c FROM ReservationChambre WHERE num_r = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(queryGetNumChambres);
                ps.setInt(1, numReservation);
                ResultSet resultSet = ps.executeQuery();

                DAOChambre daoChambre = new DAOChambreJDBC();
                while (resultSet.next()) {
                    Chambre chambre = new Chambre();
                    chambre.setNumHotel(resultSet.getInt("num_h"));
                    chambre.setNumChambre(resultSet.getInt("num_c"));
                    chambre.setEtat(etatChambre);
                    daoChambre.insertEntreeHistorique(chambre);
                }
            } catch (SQLException sqle) {
                System.err.println("DAOReservationJDBC.insertHistoriqueReservation");
                sqle.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean updateLiensTypeService(Integer numReservation, List<TypeService> services) {
        deleteLiensTypeService(numReservation, null);
        for(TypeService service : services) {
            if (!insertLiensTypeService(numReservation, service)) {
                return false;
            }
        }
        return true;
    }
}
