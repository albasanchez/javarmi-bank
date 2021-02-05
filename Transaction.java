import java.sql.Date;

public class Transaction implements java.io.Serializable{
  private int id;
  private Double amount;
  private Date date;
  private String description;
  private String type;
  private Number source;
  private Number destination;

  public Transaction(int id, Double amount, Date date, String description, String type, Number source, Number destination){
    this.id = id;
    this.amount = amount;
    this.date = date;
    this.description = description;
    this.type = type;
    this.source = source;
    this.destination = destination;
  }

  public int getTransactionID(){
    return this.id;
  }

  public Double getTransactionAmount(){
    return this.amount;
  }

  public Date getTransactionDate(){
    return this.date;
  }

  public String getTransactionDescription(){
    return this.description;
  }

  public String getTransactionType(){
    return this.type;
  }

  public Number getTransactionSource(){
    return this.source;
  }

  public Number getTransactionDestination(){
    return this.destination;
  }
  
}

