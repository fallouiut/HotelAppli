package code;

import code.model.DAOJDBC.DAOAdminJDBC;

/**
 * Created by Vincent on 15/05/2019.
 */
public class SessionUnique {

    public static String username = null;
    public static String password = null;
    private static Admin admin;

    private static SessionUnique instance = null;

    private SessionUnique(){
        this.admin = new DAOAdminJDBC().findByUsernameAndPassword(username, password);
    }

    public Admin getSession() {
        return admin;
    }

    public static SessionUnique getInstance() {
        if(instance == null)
            instance = new SessionUnique();
        return instance;
    }

}
