package ch.bader.budget.server.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.EnumUtil;

@Entity
public class RealAccount implements Account<RealAccount> {

	@Id
	@SequenceGenerator(name = "real_account_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "real_account_seq")
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
		creationDate = LocalDateTime.now();
	}

	@PreUpdate
	void fillUpdate() {
		updateDate = LocalDateTime.now();
	}

	@NonNull
	private String name;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "underlyingAccount")
	@JsonIgnore
	private List<VirtualAccount> virtualAccounts;

	private LocalDateTime creationDate;

	private LocalDateTime updateDate;

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

	public List<VirtualAccount> getVirtualAccounts() {
		return virtualAccounts.stream().sorted().collect(Collectors.toList());
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

	public void updateEnums() {
		fillPersistent();
	}

}
