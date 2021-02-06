import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.*;

// Creating Remote interface for our application 
public interface RemoteInterface extends Remote {  
   void printMsg() throws RemoteException;

   //Básico
   boolean verifyUser(String documentID, String username, String password) throws RemoteException;
   List<String> getUserAccounts(String documentID) throws RemoteException;
   
   //Apertura de cuenta - Apartado 2.a
   boolean checkDocumentID(String documentID) throws RemoteException;
   boolean registerClient(String documentID, String name, String username, String password) throws RemoteException;
   boolean checkMaxAccounts(String documentID) throws RemoteException;
   Number intialDeposit(String documentID, double deposit) throws RemoteException;
   
   //Consulta de cuenta
   double getAccountBalance(String documentID, Number account) throws RemoteException;
   List<Transaction> getAccountLastTransactions(String documentID, Number account) throws RemoteException;
   
   //Confirmación cuenta de terceros
   String getAccountUser(String documentID, Number account) throws RemoteException;
   
   //Depósito a cuenta
   double deposit(String documentID, Number account, String description, double amount) throws RemoteException;
} 