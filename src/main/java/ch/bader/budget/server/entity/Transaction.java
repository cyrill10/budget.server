package ch.bader.budget.server.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.springframework.lang.NonNull;

import ch.bader.budget.server.type.EnumUtil;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;

@Entity
public class Transaction implements Comparable<Transaction> {

	@Id
	@SequenceGenerator(name = "transaction_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
	private Integer id;

	@NonNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "credited_account_id")
	private VirtualAccount creditedAccount;

	@NonNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "debited_account_id")
	private VirtualAccount debitedAccount;

	@NonNull
	private LocalDate date;

	@NonNull
	private String description;

	@Basic
	private int paymentStatusValue;

	@Transient
	private PaymentStatus paymentStatus;

	@Basic
	private int indicationValue;

	@Transient
	private TransactionIndication indication;

	@Basic
	private int paymentTypeValue;

	@Transient
	private PaymentType paymentType;

	private float budgetedAmount;

	private float effectiveAmount;

	private LocalDateTime creationDate;

	private LocalDateTime updateDate;

	@PostLoad
	void fillTransient() {
		if (paymentStatusValue > 0) {
			this.paymentStatus = EnumUtil.getEnumForValue(PaymentStatus.class, paymentStatusValue);
		}
		if (paymentTypeValue > 0) {
			this.paymentType = EnumUtil.getEnumForValue(PaymentType.class, paymentTypeValue);
		}
		if (indicationValue > 0) {
			this.indication = EnumUtil.getEnumForValue(TransactionIndication.class, indicationValue);
		}
	}

	@PrePersist
	void fillPersistent() {
		if (paymentStatus != null) {
			this.paymentStatusValue = paymentStatus.getValue();
		}
		if (paymentType != null) {
			this.paymentTypeValue = paymentType.getValue();
		}
		if (indication != null) {
			this.indicationValue = indication.getValue();
		}
		creationDate = LocalDateTime.now();
	}

	@PreUpdate
	void fillUpdate() {
		updateDate = LocalDateTime.now();
	}

	public VirtualAccount getCreditedAccount() {
		return creditedAccount;
	}

	public void setCreditedAccount(VirtualAccount creditedAccount) {
		this.creditedAccount = creditedAccount;
	}

	public VirtualAccount getDebitedAccount() {
		return debitedAccount;
	}

	public void setDebitedAccount(VirtualAccount debitedAccount) {
		this.debitedAccount = debitedAccount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public float getBudgetedAmount() {
		return budgetedAmount;
	}

	public void setBudgetedAmount(float budgetedAmount) {
		this.budgetedAmount = budgetedAmount;
	}

	public float getEffectiveAmount() {
		return effectiveAmount;
	}

	public void setEffectiveAmount(float effectiveAmount) {
		this.effectiveAmount = effectiveAmount;
	}

	public Integer getId() {
		return id;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public TransactionIndication getIndication() {
		return indication;
	}

	public void setIndication(TransactionIndication indication) {
		this.indication = indication;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public int compareTo(Transaction o) {
		return this.id.compareTo(o.id);

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: " + this.getId());
		sb.append(" Date: " + this.getDate().toString());
		sb.append(" B-Amount: " + this.getBudgetedAmount());
		sb.append(" From: " + this.getCreditedAccount().getName());
		sb.append(" To: " + this.getDebitedAccount().getName());
		return sb.toString();
	}

	public void updateEnums() {
		fillPersistent();
	}

	public Transaction createDublicate(LocalDate newDate) {
		Transaction newTransaction = new Transaction();
		newTransaction.creditedAccount = this.creditedAccount;
		newTransaction.debitedAccount = this.debitedAccount;
		newTransaction.description = this.description;
		newTransaction.paymentStatus = this.paymentStatus;
		newTransaction.indication = this.indication;
		newTransaction.paymentType = this.paymentType;
		newTransaction.budgetedAmount = this.budgetedAmount;
		newTransaction.effectiveAmount = this.effectiveAmount;

		newTransaction.date = newDate;

		return newTransaction;
	}

	public boolean isNotPrebudgetedAccount() {
		return !this.creditedAccount.isPrebudgetedAccount() && !this.debitedAccount.isPrebudgetedAccount();
	}
}
