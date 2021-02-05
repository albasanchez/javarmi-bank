import java.sql.*;

// Implementing the remote interface 
public class ImplExample implements RemoteInterface {
  DataStorage dataStorage;
  static Connection con;

  public ImplExample() {
    dataStorage = new DataStorage();
    con = dataStorage.connectDatabase("127.0.0.1", "5432", "JRMI", "postgres", "131619131619");
  }

  // Implementing the interface method
  public void printMsg() {
    System.out.println("This is an example RMI program");
  }

  // Apertura de cuenta - Apartado 2.a
  public boolean checkDocumentID(String documentID) {
    return dataStorage.checkUserDocument(con, documentID);
  }

  public boolean registerClient(String documentID, String name, String username, String password) {
    return dataStorage.registerUser(con, documentID, name, username, password);
  }

  public boolean checkMaxAccounts(String documentID) {
    return dataStorage.getUserAccounts(con, documentID);
  }

  public boolean verifyUser(String username, String password) {
    return dataStorage.loginUser(con, username, password);
  }

  public Number intialDeposit(String documentID, double deposit) {
    return dataStorage.createAccount(con, deposit, documentID);
  }
}