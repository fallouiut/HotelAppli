package code.model.DAOJDBC;

import code.TypeAcces;
import code.model.ConnexionUnique;
import code.model.DAOInterfaces.DAOTypeAcces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 05/05/2019.
 */
public class DAOTypeAccesJDBC implements DAOTypeAcces {

    private static Connection connection = ConnexionUnique.getInstance().getConnection();

    @Override
    public boolean delete(TypeAcces obj) {
        return false;
    }

    @Override
    public List<TypeAcces> findAll() {
        String getTypeAccesQuery = "SELECT * FROM TypeAcces";
        List<TypeAcces> result = new ArrayList<>();
        try {

            ResultSet rs = connection.createStatement().executeQuery(getTypeAccesQuery);
            while(rs.next()) {
                result.add(new TypeAcces(rs.getString(1)));
            }

            return result;

        } catch (SQLException sqle) {
            System.err.println("DAOTypeAccesJDBC.findAll");
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public TypeAcces getById(String s) {
        return null;
    }

    @Override
    public TypeAcces insert(TypeAcces obj) {
        return null;
    }

    @Override
    public boolean update(TypeAcces obj) {
        return false;
    }
}
