package Test;

import code.Client;
import code.model.DAOJDBC.DAOClientJDBC;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Vincent on 01/05/2019.
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        //testGetById();
        //testFindAll();
        //testDelete();
        //testInsert();
        testUpdate();

    }

    public static void testGetById() {
        Client client1 = new DAOClientJDBC().getById(1);
        Client client3 = new DAOClientJDBC().getById(6);

        System.out.println(client1.toString());
        System.out.println(client3.toString());
    }

    public static void testFindAll() {
        List<Client> clients = new DAOClientJDBC().findAll();

        for(Client c: clients) {
            System.out.println(c.toString());
        }
    }

    public static void testDelete() {
        Client client = new Client();
        client.setNum(6);

        boolean isDel = new DAOClientJDBC().delete(client);
        System.out.println(isDel ? "true": "false");
    }

    public static void testInsert() {
        Client client = new Client(7, "seye", "fallou", "chomage", "0638611135", "fourchette@cuillere.couteau", "fourchette_cuillere", "abcd");

        Client inserted = new DAOClientJDBC().insert(client);
        System.out.println(inserted.toString());
    }

    public static void testUpdate() {
        Client client = new Client(7, "seyleu", "fallouu", "chomageu", "0638611135", "fourchette@cuillere.couteauu", "fourchette_cuillereu", "abcdu");
        boolean updated = new DAOClientJDBC().update(client);
        System.out.println(updated ? "true": "false");

    }
}
