package Test;

import code.Client;
import code.Reservation;
import code.model.DAOJDBC.DAOClientJDBC;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent on 13/05/2019.
 */
public class ClientTest {

    private static DAOClientJDBC daoClientJDBC = new DAOClientJDBC();

    @Test
    public void testFindByHotel() {
        Map<Client, Reservation> clients = daoClientJDBC.findByHotel(13);
        System.out.println(clients);
    }

}
