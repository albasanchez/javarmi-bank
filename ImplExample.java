import java.sql.*;
import java.util.*;
import java.io.InputStream;
import java.io.FileInputStream;

enum TransactionType {
  deposit, withdrawal, transference;
}

// Implementing the remote interface
public class ImplExample implements RemoteInterface {
  DataStorage dataStorage;
  static Connection con;
  Properties config = new Properties();
  InputStream configInput = null;

  public ImplExample() {
    dataStorage = new DataStorage();
    try {
      configInput = new FileInputStream("config.properties");
      config.load(configInput);
    } catch (Exception e) {
      System.out.println("Error cargando configuración\n" + e.getMessage());
    }
    con = dataStorage.connectDatabase(config.getProperty("DB_HOST"), config.getProperty("DB_PORT"),
        config.getProperty("DB_NAME"), config.getProperty("DB_USER"), config.getProperty("DB_PASSWORD"));
  }

  // Implementing the interface method
  public void printMsg() {
    System.out.println("This is an example RMI program");
  }

  // Básico
  public boolean verifyUser(String documentID, String username, String password) {
    return dataStorage.loginUser(con, documentID, username, password);
  }

  public List<String> getUserAccounts(String documentID) {
    return dataStorage.getUserAccounts(con, documentID);
  }

  // Apertura de cuenta - Apartado 2.a
  public boolean checkDocumentID(String documentID) {
    return dataStorage.checkUserDocument(con, documentID);
  }

  public boolean registerClient(String documentID, String name, String username, String password) {
    return dataStorage.registerUser(con, documentID, name, username, password);
  }

  public boolean checkMaxAccounts(String documentID) {
    return dataStorage.checkUserAccountLimit(con, documentID);
  }

  public Number intialDeposit(String documentID, double deposit) {
    Number acc = dataStorage.createAccount(con, deposit, documentID);
    dataStorage.transaction(con, null, acc, TransactionType.deposit.toString(), "Depósito inicial", deposit);
    return acc;
  }

  // Consulta de cuenta
  public double getAccountBalance(String documentID, Number account) {
    return dataStorage.getAccountBalance(con, documentID, account);
  }

  public List<Transaction> getAccountLastTransactions(String documentID, Number account) {
    return dataStorage.getAccountLastTransactions(con, documentID, account, 5);
  }

  // Confirmación cuenta de terceros
  public String getAccountUser(String documentID, Number account) {
    return dataStorage.getAccountUser(con, documentID, account);
  }

  // Depósito a cuenta
  public double deposit(String documentID, Number account, String description, double amount) {
    boolean update = false;
    boolean deposit = false;
    double balance = -1;
    try{
      con.setAutoCommit(false);
      balance = dataStorage.getAccountBalance(con, documentID, account);
      balance = balance + amount;
      update = dataStorage.updateAccountBalance(con, documentID, account, balance);
      deposit = dataStorage.transaction(con, null, account, TransactionType.deposit.toString(), description, amount);
      con.commit();
    } catch (Exception e) {
      try {
        con.rollback();
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    } finally {
      try {
        con.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (update && deposit) {
      return balance;
    } else {
      return -1;
    }
  }

  // Retiro de cuenta
  public double withdrawal(String documentID, Number account, double amount) {
    boolean update = false;
    boolean withdrawal = false;
    double balance = -1;
    try {
      con.setAutoCommit(false);
      balance = dataStorage.getAccountBalance(con, documentID, account);
      balance = balance - amount;
      update = dataStorage.updateAccountBalance(con, documentID, account, balance);
      withdrawal = dataStorage.transaction(con, null, account, TransactionType.withdrawal.toString(), "Retiro", amount);
      con.commit();
    } catch (Exception e) {
      try {
        con.rollback();
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    } finally {
      try {
        con.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (update && withdrawal) {
      return balance;
    } else {
      return -1;
    }
  }

  // Transferencia entre cuentas
  public double transference(String sourceDocumentID, String destinationDocumentID, Number sourceAccount, Number destinationAccount, String description, double amount) {
    boolean sourceUpdate = false;
    double sourceBalance = -1;
    boolean destinationUpdate = false;
    boolean transference = false;
    try {
      con.setAutoCommit(false);
      sourceBalance = dataStorage.getAccountBalance(con, sourceDocumentID, sourceAccount);
      sourceBalance = sourceBalance - amount;
      sourceUpdate = dataStorage.updateAccountBalance(con, sourceDocumentID, sourceAccount, sourceBalance);
      double destinationBalance = dataStorage.getAccountBalance(con, destinationDocumentID, destinationAccount);
      destinationBalance = destinationBalance + amount;
      destinationUpdate = dataStorage.updateAccountBalance(con, destinationDocumentID, destinationAccount, destinationBalance);
      transference = dataStorage.transaction(con, sourceAccount, destinationAccount, TransactionType.transference.toString(), description, amount);
      con.commit();
    } catch (Exception e) {
      try {
        con.rollback();
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    } finally {
      try {
        con.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (sourceUpdate && destinationUpdate && transference) {
      return sourceBalance;
    } else {
      return -1;
    }
  }
}