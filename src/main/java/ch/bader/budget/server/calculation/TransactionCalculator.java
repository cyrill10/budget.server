package ch.bader.budget.server.calculation;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.bader.budget.server.calculation.implementation.function.BudgetedAmountFunction;
import ch.bader.budget.server.calculation.implementation.function.EffectiveAmountFunction;
import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.json.TransactionElement;

public class TransactionCalculator {

	private static Function<Transaction, Number> effectiveAmountFunction = new EffectiveAmountFunction();

	private static Function<Transaction, Number> budgetedAmountFunction = new BudgetedAmountFunction();

	public static Iterable<TransactionElement> getTransactionsForMonth(RealAccount realAccount,
			List<Transaction> transactions, List<VirtualAccount> virtualAccounts, LocalDate from) {
		Balance balance = new Balance(
				VirtaulAccountCalculator.getBalanceAt(virtualAccounts, from, effectiveAmountFunction));
		Balance budgetedBalance = new Balance(
				VirtaulAccountCalculator.getBalanceAt(virtualAccounts, from, budgetedAmountFunction));
		TransactionElement before = new TransactionElement("Before", balance.getBalance(),
				budgetedBalance.getBalance());
		LinkedList<TransactionElement> transactionElements = transactions.stream().distinct()
				.map(t -> createTransactionElement(t, virtualAccounts, balance, budgetedBalance)).sorted()
				.collect(Collectors.toCollection(LinkedList::new));
		TransactionElement after = new TransactionElement("After", balance.getBalance(), budgetedBalance.getBalance());
		transactionElements.push(before);
		transactionElements.add(after);
		return transactionElements;
	}

	public static TransactionElement createTransactionElement(Transaction transaction,
			List<VirtualAccount> virtualAccounts, Balance balance, Balance budgetedBalance) {
		float amount = 0;
		float budgetedAmount = 0;
		if (virtualAccounts.contains(transaction.getDebitedAccount())) {
			amount = transaction.getEffectiveAmount();
			budgetedAmount = transaction.getBudgetedAmount();
			balance.add(transaction.getEffectiveAmount());
			budgetedBalance.add(transaction.getBudgetedAmount());
		}
		if (virtualAccounts.contains(transaction.getCreditedAccount())) {
			amount = 0 - transaction.getEffectiveAmount();
			budgetedAmount = 0 - transaction.getBudgetedAmount();
			balance.subtract(transaction.getEffectiveAmount());
			budgetedBalance.subtract(transaction.getBudgetedAmount());
		}

		return new TransactionElement(transaction, amount, budgetedAmount, balance.getBalance(),
				budgetedBalance.getBalance());
	}

	public static Iterable<TransactionElement> getTransactionsForMonth(VirtualAccount virtualAccount,
			List<Transaction> transactions, LocalDate from) {
		Balance balance = new Balance(
				VirtaulAccountCalculator.getBalanceAt(virtualAccount, from, effectiveAmountFunction));
		Balance budgetedBalance = new Balance(
				VirtaulAccountCalculator.getBalanceAt(virtualAccount, from, budgetedAmountFunction));
		TransactionElement before = new TransactionElement("Before", balance.getBalance(),
				budgetedBalance.getBalance());
		LinkedList<TransactionElement> transactionElements = transactions.stream().distinct()
				.map(t -> createTransactionElement(t, virtualAccount, balance, budgetedBalance)).sorted()
				.collect(Collectors.toCollection(LinkedList::new));
		TransactionElement after = new TransactionElement("After", balance.getBalance(), budgetedBalance.getBalance());
		transactionElements.push(before);
		transactionElements.add(after);
		return transactionElements;
	}

	public static TransactionElement createTransactionElement(Transaction transaction, VirtualAccount virtualAccount,
			Balance balance, Balance budgetedBalance) {
		float amount = 0;
		float budgetedAmount = 0;
		if (transaction.getDebitedAccount().equals(virtualAccount)) {
			amount = transaction.getEffectiveAmount();
			budgetedAmount = transaction.getBudgetedAmount();
			balance.add(transaction.getEffectiveAmount());
			budgetedBalance.add(transaction.getBudgetedAmount());
		} else {
			amount = 0 - transaction.getEffectiveAmount();
			budgetedAmount = 0 - transaction.getBudgetedAmount();
			balance.subtract(transaction.getEffectiveAmount());
			budgetedBalance.subtract(transaction.getBudgetedAmount());
		}

		return new TransactionElement(transaction, amount, budgetedAmount, balance.getBalance(),
				budgetedBalance.getBalance());
	}

}
