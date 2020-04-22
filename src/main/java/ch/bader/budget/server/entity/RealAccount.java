package ch.bader.budget.server.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.lang.NonNull;

import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.EnumUtil;

@Entity
public class RealAccount implements Account<RealAccount> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Basic
	private int accountTypeValue;

	@Transient
	private AccountType accountType;

	@PostLoad
	void fillTransient() {
		if (accountTypeValue > 0) {
			this.accountType = EnumUtil.getEnumForValue(AccountType.class, accountTypeValue);
		}
	}

	@PrePersist
	void fillPersistent() {
		if (accountType != null) {
			this.accountTypeValue = accountType.getValue();
		}
	}

	@NonNull
	private String name;

	@OneToMany
	private List<VirtualAccount> virtualAccounts;

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

	@Override
	public int compareTo(RealAccount o) {
		int compareType = this.getAccountType().getValue().compareTo(o.getAccountType().getValue());
		if (compareType == 0) {
			return this.getName().compareTo(o.getName());
		}
		return compareType;
	}

	public String toString() {
		return this.getName();
	}

}
