import java.sql.*;
import java.util.*;

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

    // USER

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
            System.out.println("Error al revisar el documento del usuario " + documentID);
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

    public boolean loginUser(Connection con, String document_id, String username, String password) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM JRMI_USER WHERE document_id = '" + document_id
                    + "' AND username = '" + username + "' AND password='" + password + "'";
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

    // ACCOUNTS

    public boolean checkUserAccountLimit(Connection con, String user_id) {
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

    public List<String> getUserAccounts(Connection con, String document_id) {
        List<String> accounts = new ArrayList<String>();
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM JRMI_ACCOUNT WHERE FK_USER = '" + document_id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String account_number = rs.getString("number");
                accounts.add(account_number);
            }
            return accounts;
        } catch (SQLException ex) {
            System.out.println("Error solicitar las cuentas del usuario " + document_id);
            System.out.println(ex.getMessage());
            return new ArrayList<String>();
        }
    }

    public double getAccountBalance(Connection con, String document_id, Number account) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT current_balance FROM JRMI_ACCOUNT WHERE NUMBER = " + account + " AND FK_USER = '"
                    + document_id + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                double current_balance = rs.getFloat("current_balance");
                rs.close();
                return current_balance;
            } else {
                rs.close();
                return -1;
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar el balance de la cuenta " + account);
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    public boolean updateAccountBalance(Connection con, String document_id, Number account, double balance) {
        try {
            Statement stmt = con.createStatement();
            String sql = "UPDATE JRMI_ACCOUNT SET current_balance = " + balance + " WHERE FK_USER = '" + document_id
                    + "' AND NUMBER = " + account + "";
            stmt.execute(sql);

            return true;
        } catch (SQLException ex) {
            System.out.println("Error al modificar el balance de la cuenta " + account);
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public List<Transaction> getAccountLastTransactions(Connection con, String document_id, Number account,
            int transactions) {
        List<Transaction> trans = new ArrayList<Transaction>();
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT JRMI_TRANSACTION.* FROM JRMI_TRANSACTION, JRMI_ACCOUNT WHERE NUMBER=" + account
                    + " AND FK_USER='" + document_id + "' AND (FK_ACCOUNT_SOURCE = " + account
                    + " OR FK_ACCOUNT_DESTINATION = " + account + ") ORDER BY date desc LIMIT " + transactions + "";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Transaction new_transaction = new Transaction(rs.getInt("id"), rs.getDouble("amount"),
                        rs.getDate("date"), rs.getString("description"), rs.getString("type"), rs.getInt("FK_ACCOUNT_SOURCE"),
                        rs.getInt("FK_ACCOUNT_DESTINATION"));
                trans.add(new_transaction);
            }
            return trans;
        } catch (SQLException ex) {
            System.out.println("Error solicitar las transacciones de la cuenta " + account);
            System.out.println(ex.getMessage());
            return new ArrayList<Transaction>();
        }
    }

    public String getAccountUser(Connection con, String document_id, Number account) {
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT NAME FROM JRMI_ACCOUNT JOIN JRMI_USER ON FK_USER = DOCUMENT_ID WHERE NUMBER = "
                    + account + " AND DOCUMENT_ID = '" + document_id + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                rs.close();
                return name;
            } else {
                rs.close();
                return "";
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar el propietario de la cuenta " + account);
            System.out.println(ex.getMessage());
            return "";
        }
    }

    // TRANSACTIONS

    public boolean transaction(Connection con, Number sourceAccount, Number destinationAccount, String transactionType, String description, Number amount) {
        try {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO JRMI_TRANSACTION(amount, description, type, fk_account_source, fk_account_destination) VALUES ("
                    + amount + ", '" + description + "', '" + transactionType + "', " + sourceAccount + ", " + destinationAccount + ") RETURNING id";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error en la transacción de tipo: " + transactionType + ", Cuenta origen: " + sourceAccount + ", Cuenta destino: " + destinationAccount);
            System.out.println(ex.getMessage());
            return false;
        }
    }
}