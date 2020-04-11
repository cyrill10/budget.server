package ch.bader.budget.server.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.NonNull;

import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NonNull
	private VirtualAccount creditedAccount;

	@NonNull
	private VirtualAccount debitedAccount;

	@NonNull
	private LocalDate date;

	@NonNull
	private PaymentStatus paymentStatus;

	@NonNull
	private TransactionIndication indication;

	@NonNull
	private PaymentType paymentType;

	private float budgetedAmount;

	private float effectiveAmount;

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
}
