package ch.bader.budget.server.entity;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.lang.NonNull;

import ch.bader.budget.server.type.EnumUtil;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;

@Entity
public class Transaction implements Comparable<Transaction> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	@ManyToOne
	private VirtualAccount creditedAccount;

	@NonNull
	@ManyToOne
	private VirtualAccount debitedAccount;

	@NonNull
	private LocalDate date;

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
	}

	public Transaction(VirtualAccount creditedAccount, VirtualAccount debitedAccount, LocalDate date) {
		this.creditedAccount = creditedAccount;
		this.debitedAccount = debitedAccount;
		this.date = date;
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

	@Override
	public int compareTo(Transaction o) {
		return this.getDate().compareTo(o.getDate());

	}
}
