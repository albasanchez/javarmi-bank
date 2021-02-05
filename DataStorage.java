import java.sql.*;

public class DataStorage {

    public Connection connectDatabase(String host, String port, String database, String user, String password) {
        Connection connection = null;
        String url = "";
        try {
            // We register PostgreSQL driver
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }

            url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            // Database connect
            connection = DriverManager.getConnection(url, user, password);
            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "Conexión exitosa a la BD" : "Error en la conexión a la BD");
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error al conectar con la base de datos de PostgreSQL (" + url + "): " + sqle);
        }
        return connection;
    }

    public boolean checkUserDocument(Connection con, String documentID) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM JRMI_USER WHERE document_id = '" + documentID + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                rs.close();
                return true;
            } else {
                rs.close();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error al realizar el login del usuario " + documentID);
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean getUserAccounts(Connection con, String user_id) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT document_id, COUNT(JRMI_ACCOUNT.number) AS accounts FROM JRMI_USER LEFT OUTER JOIN JRMI_ACCOUNT ON document_id=fk_user WHERE document_id = '"
                    + user_id + "' GROUP BY document_id;";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                int accounts = rs.getInt("accounts");
                rs.close();
                if (accounts < 3) {
                    return true;
                } else {
                    return false;
                }
            } else {
                rs.close();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar las cuentas del usuario " + user_id);
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean registerUser(Connection con, String documentID, String name, String username, String password) {
        try {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO JRMI_USER(document_id, name, username, password) VALUES ('" + documentID + "', '"
                    + name + "', '" + username + "', '" + password + "') RETURNING document_id";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                rs.close();
                return true;
            } else {
                rs.close();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error al registrar el usuario " + username);
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean loginUser(Connection con, String username, String password) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM JRMI_USER WHERE username = '" + username + "' AND password='" + password + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                rs.close();
                return true;
            } else {
                rs.close();
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error al realizar el login del usuario " + username);
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public Number createAccount(Connection con, Number current_balance, String document_id) {
        try {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO JRMI_ACCOUNT(current_balance, fk_user) VALUES (" + current_balance + ", '"
                    + document_id + "') RETURNING number";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int number = rs.getInt("number");
                return number;
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error al registrar la cuenta del usuario " + document_id);
            System.out.println(ex.getMessage());
            return 0;
        }
    }
}