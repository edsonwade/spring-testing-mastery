package code.with.vanilson.springtx.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String accountType; // savings or checking

  @Column(nullable = false)
  private LocalDate transactionDate;

  private String description;

  @Column(nullable = false)
  private BigDecimal amount; // debit or credit

  @Column(nullable = false)
  private BigDecimal balance; // running total

  public Account() {}

  public Account(Long id, String accountType, LocalDate transactionDate, String description, BigDecimal amount, BigDecimal balance) {
      this.id = id;
      this.accountType = accountType;
      this.transactionDate = transactionDate;
      this.description = description;
      this.amount = amount;
      this.balance = balance;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public LocalDate getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(LocalDate transactionDate) {
    this.transactionDate = transactionDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }
  
}
