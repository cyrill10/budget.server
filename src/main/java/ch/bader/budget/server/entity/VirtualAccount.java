package ch.bader.budget.server.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.NonNull;

import ch.bader.budget.server.type.AccountType;

@Entity
public class VirtualAccount implements Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	@Enumerated(EnumType.STRING)
	private AccountType accountType;

	@NonNull
	private String name;

	@NonNull
	private float balance;

	@NonNull
	private RealAccount underlyingAccount;

	private LocalDateTime balanceDate;

	public Integer getId() {
		return id;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(LocalDateTime balanceDate) {
		this.balanceDate = balanceDate;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public RealAccount getUnderlyingAccount() {
		return underlyingAccount;
	}

	public void setUnderlyingAccount(RealAccount underlyingAccount) {
		this.underlyingAccount = underlyingAccount;
	}

	@Override
	public float getBalance(LocalDateTime date) {
		// TODO Auto-generated method stub
		return 0;
	}
}
