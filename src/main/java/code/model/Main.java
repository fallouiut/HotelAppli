package code.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vincent on 01/05/2019.
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        String req = "SELECT nom_cl, prenom_cl FROM Client";

        System.out.println("Connexion en cours.");
        try (Connection conn = ConnexionUnique.getInstance().getConnection()){
            System.out.println("Connexion réussie.");
            Statement stmt = conn.createStatement();
            // Execution de la requete
            System.out.println("Execution de la requete : " + req );
            ResultSet rset = stmt.executeQuery(req);

            while (rset.next()){
                System.out.println(rset.getString("nom_cl") + " ");
                System.out.println(rset.getString("prenom_cl"));
            }
            // Fermeture de l'instruction (liberation des ressources)
            stmt.close();
            System.out.println("\nOk.\n");
        }

        catch (SQLException e) {
            e.printStackTrace();// Arggg!!!
            System.out.println(e.getMessage() + "\n");
        }
    }
}
