public class App {
  static public void main(String[] args) {
    DataStorage dataStorage = new DataStorage();
    User user = new User("9999", "Alba", "assanchez", "123");
    dataStorage.registerUser(user);
  }
}