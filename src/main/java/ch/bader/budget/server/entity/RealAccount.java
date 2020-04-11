package ch.bader.budget.server.entity;

import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.NonNull;

import ch.bader.budget.server.type.AccountType;

public class RealAccount implements Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	@Enumerated(EnumType.STRING)
	private AccountType accountType;

	@NonNull
	private String name;

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

	@Override
	public float getBalance(LocalDateTime date) {
		// TODO Auto-generated method stub
		return 0;
	}

}
