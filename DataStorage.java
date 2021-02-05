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

    public void getUserAccounts(Connection con, String user_id) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT document_id, COUNT(JRMI_ACCOUNT.number) AS accounts FROM JRMI_USER LEFT OUTER JOIN JRMI_ACCOUNT ON document_id=fk_user WHERE document_id = '"
                    + user_id + "' GROUP BY document_id;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Retrieve by column name
                int id = rs.getInt("document_id");
                int accounts = rs.getInt("accounts");
                System.out.println("El usuario " + id + " tiene " + accounts + " cuentas");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al consultar las cuentas del usuario " + user_id);
            System.out.println(ex.getMessage());
        }
    }

    public void registerUser(Connection con, User user) {
        try {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO JRMI_USER(document_id, name, username, password) VALUES ('" + user.document_id
                    + "', '" + user.name + "', '" + user.username + "', '" + user.password + "') RETURNING document_id";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println("Se registró el usuario " + user.username);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al registrar el usuario " + user.username);
            System.out.println(ex.getMessage());
        }
    }

    public void loginUser(Connection con, String username, String password) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM JRMI_USER WHERE username = '" + username + "' AND password='" + password + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                // Retrieve by column name
                int id = rs.getInt("document_id");
                System.out.println("El usuario " + id + " inició sesión correctamente");
            } else {
                System.out.println("El usuario " + username + " no inició sesión correctamente");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al realizar el login del usuario " + username);
            System.out.println(ex.getMessage());
        }
    }

    public void createAccount(Connection con, Number current_balance, String document_id) {
        try {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO JRMI_ACCOUNT(current_balance, fk_user) VALUES (" + current_balance
                    + ", '" + document_id  + "') RETURNING number";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int number = rs.getInt("number");
                System.out.println("Se registró la cuenta " + number);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al registrar la cuenta del usuario " + document_id);
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        DataStorage dataStorage = new DataStorage();
        Connection con = dataStorage.connectDatabase("127.0.0.1", "5432", "JRMI", "postgres", "131619131619");
        User user = new User("5678", "miguel", "mpena", "124");
        // dataStorage.getUserAccounts(con, user.document_id);
        // dataStorage.registerUser(con, user);
        // dataStorage.loginUser(con, user.username, user.password);
        // dataStorage.createAccount(con, 15, user.document_id);
    }
}