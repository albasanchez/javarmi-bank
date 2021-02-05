import java.rmi.Remote; 
import java.rmi.RemoteException;  

// Creating Remote interface for our application 
public interface RemoteInterface extends Remote {  
   void printMsg() throws RemoteException;  

   //Apertura de cuenta - Apartado 2.a
   boolean checkDocumentID(String documentID) throws RemoteException;
   boolean registerClient(String documentID, String name, String username, String password) throws RemoteException;
   boolean checkMaxAccounts(String documentID) throws RemoteException;
   boolean verifyUser(String username, String password) throws RemoteException;
   Number intialDeposit(String documentID, double deposit) throws RemoteException;
} 