public class User {
  public String document_id;
  public String name;
  public String username;
  public String password;

  public User(String document, String name, String username, String password) {
    this.document_id = document;
    this.name = name;
    this.username = username;
    this.password = password;
  }
}
