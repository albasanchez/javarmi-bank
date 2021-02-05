// Implementing the remote interface 
public class ImplExample implements RemoteInterface {  
   
  // Implementing the interface method 
  public void printMsg() {  
    System.out.println("This is an example RMI program");  
  }  

  //Apertura de cuenta - Apartado 2.a
  public boolean checkDocumentID(String documentID) { 
    System.out.println("Documento de identidad: " + documentID); 
    return true;  
  } 

  public boolean registerClient(String documentID, String name, String username, String password) {
    System.out.println("Documento de identidad: " + documentID);
    System.out.println("Nombre: " + name); 
    System.out.println("Username: " + username); 
    System.out.println("Password: " + password);    
    return true;  
  } 

  public boolean checkMaxAccounts(String documentID) {  
    System.out.println("Documento de identidad: " + documentID);
    return true;  
  } 

  public boolean verifyUser(String username, String password) {  
    System.out.println("Username: " + username); 
    System.out.println("Password: " + password); 
    return true;  
  } 

  public String intialDeposit(double deposit) {  
    System.out.println("Depósito inicial: " + deposit); 
    return "4455-ALPO-555-44-22252";  
  } 
} 