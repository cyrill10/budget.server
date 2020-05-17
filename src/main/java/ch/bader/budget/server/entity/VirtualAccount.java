package ch.bader.budget.server.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class VirtualAccount implements Account<VirtualAccount> {

	@Id
	@SequenceGenerator(name = "virtual_account_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "virtual_account_seq")
	private Integer id;

	@NonNull
	private String name;

	@NonNull
	private float balance;

	@NonNull
	@ManyToOne
	@JoinColumn(name = "underlyingAccount_id")
	private RealAccount underlyingAccount;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "creditedAccount")
	@JsonIgnore
	private List<Transaction> creditedTransactions;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "debitedAccount")
	@JsonIgnore
	private List<Transaction> debitedTransactions;

	private LocalDateTime creationDate;

	private LocalDateTime updateDate;

	@PrePersist
	void fillPersist() {
		creationDate = LocalDateTime.now();
	}

	@PreUpdate
	void fillUpdate() {
		updateDate = LocalDateTime.now();
	}

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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public List<Transaction> getCreditedTransactions() {
		return creditedTransactions.stream().sorted().collect(Collectors.toList());
	}

	public List<Transaction> getDebitedTransactions() {
		return debitedTransactions.stream().sorted().collect(Collectors.toList());
	}

	@Override
	public int compareTo(VirtualAccount o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String toString() {
		return this.getName() + " (" + this.getUnderlyingAccount().getName() + ")";
	}
}
