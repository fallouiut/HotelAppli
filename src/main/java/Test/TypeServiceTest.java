package Test;

import code.Client;
import code.Reservation;
import code.TypeService;
import code.model.DAOJDBC.DAOReservationJDBC;
import code.model.DAOJDBC.DAOTypeServiceJDBC;

import java.time.LocalDate;
import java.util.List;

public class TypeServiceTest {
    public static void main(String args[]) {
        //testInsert();
        //testUpdate();
        //getById();
        //testFindAll();
        //testDelete();
    }

    public static void testInsert() {
        TypeService typeService = new TypeService("petit dejeuner", 5);
        TypeService T = new DAOTypeServiceJDBC().insert(typeService);
        System.out.println(T == null ? "null": T.toString());
    }


    public static void testUpdate() {
        TypeService typeService = new TypeService("petit dejeuner", 6);
        boolean n = new DAOTypeServiceJDBC().update(typeService);
        System.out.println(n ? "true": false);
    }

    public static void getById() {
        TypeService r = new DAOTypeServiceJDBC().getById("petit dejeuner");
        System.out.println(r);
    }

    public static void testFindAll() {
        List<TypeService> typeServices = new DAOTypeServiceJDBC().findAll();

        for(TypeService r: typeServices) {
            System.out.println(r.toString());
        }
    }

    public static void testDelete() {
        TypeService typeService = new TypeService("petit dejeuner", 5);

        boolean isDel = new DAOTypeServiceJDBC().delete(typeService);
        System.out.println(isDel ? "true": "false");
    }

}
