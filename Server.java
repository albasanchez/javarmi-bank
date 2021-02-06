import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.server.UnicastRemoteObject;

public class Server extends ImplExample { 
   public static void main(String args[]) { 
      try { 
         // Instantiating the implementation class 
         ImplExample obj = new ImplExample(); 

         // Exporting the object of implementation class  
         // (here we are exporting the remote object to the stub)
         RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(obj, 0);  
        
         // Binding the remote object (stub) in the registry 
         Registry registry = LocateRegistry.getRegistry(); 
        
         registry.bind("RMI-INTERFACE", stub);  
         System.err.println("Server ready"); 
      } catch (Exception e) { 
         System.err.println("Server exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
} 