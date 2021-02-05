import java.rmi.Remote; 
import java.rmi.RemoteException;  

// Creating Remote interface for our application 
public interface RemoteInterface extends Remote {  
   void printMsg() throws RemoteException;

   //Básico
   boolean verifyUser(String username, String password) throws RemoteException;
   String[] getUserAccounts(String documentID);
   
   //Apertura de cuenta - Apartado 2.a
   boolean checkDocumentID(String documentID) throws RemoteException;
   boolean registerClient(String documentID, String name, String username, String password) throws RemoteException;
   boolean checkMaxAccounts(String documentID) throws RemoteException;
   Number intialDeposit(String documentID, double deposit) throws RemoteException;
   
   //Consulta de cuenta
   double getAccountBalance(String documentID, Number account);
   int[] getAccountLastTransactions(String documentID, Number account);
   
   //Confirmación cuenta de terceros
   String getAccountUser(String documentID, Number account);
   
   //Depósito a cuenta
   boolean deposit(String documentID, Number account, String description, double amount);
} 