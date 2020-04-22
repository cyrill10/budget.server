package ch.bader.budget.server.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.lang.NonNull;

@Entity
public class VirtualAccount implements Account<VirtualAccount> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private String name;

	@NonNull
	private float balance;

	@NonNull
	@ManyToOne
	private RealAccount underlyingAccount;

	@OneToMany
	private List<Transaction> creditedTransactions;

	@OneToMany
	private List<Transaction> debitedTransactions;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int compareTo(VirtualAccount o) {
		return this.getName().compareTo(o.getName());
	}
}
