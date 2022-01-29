package ch.bader.budget.server.calculation;

import ch.bader.budget.server.calculation.implementation.function.BudgetedAmountFunction;
import ch.bader.budget.server.calculation.implementation.function.EffectiveAmountFunction;
import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.json.TransactionElement;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionCalculator {

	private static final Function<Transaction, Number> effectiveAmountFunction = new EffectiveAmountFunction();

	private static final Function<Transaction, Number> budgetedAmountFunction = new BudgetedAmountFunction();

	public static Iterable<TransactionElement> getTransactionsForMonth(List<Transaction> transactions, List<VirtualAccount> virtualAccounts, LocalDate from) {
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
		Balance in = new Balance(0);
		Balance out = new Balance(0);
		Balance budgetedIn = new Balance(0);
		Balance budgetedOut = new Balance(0);

		Balance balance = new Balance(
				VirtaulAccountCalculator.getBalanceAt(virtualAccount, from, effectiveAmountFunction));
		Balance budgetedBalance = new Balance(
				VirtaulAccountCalculator.getBalanceAt(virtualAccount, from, budgetedAmountFunction));
		TransactionElement before = new TransactionElement("Before", balance.getBalance(),
				budgetedBalance.getBalance());
		LinkedList<TransactionElement> transactionElements = transactions.stream().distinct()
				.map(t -> createTransactionElement(t, virtualAccount, balance, budgetedBalance)).sorted().peek(t -> {
					if (t.getAmount() >= 0) {
						in.add(t.getAmount());
					} else {
						out.subtract(t.getAmount());
					}
					if (t.getBudgetedAmount() >= 0) {
						budgetedIn.add(t.getBudgetedAmount());
					} else {
						budgetedOut.subtract(t.getBudgetedAmount());
					}
				}).collect(Collectors.toCollection(LinkedList::new));
		TransactionElement after = new TransactionElement("After", balance.getBalance(), budgetedBalance.getBalance());
		TransactionElement in_out = new TransactionElement(in.getBalance(), out.getBalance(), budgetedIn.getBalance(),
				budgetedOut.getBalance());
		transactionElements.push(before);
		transactionElements.add(after);
		transactionElements.add(in_out);
		return transactionElements;
	}

	public static TransactionElement createTransactionElement(Transaction transaction, VirtualAccount virtualAccount,
			Balance balance, Balance budgetedBalance) {
		float amount;
		float budgetedAmount;
		if (transaction.getDebitedAccount().equals(virtualAccount)) {
			amount = transaction.getEffectiveAmount();
			budgetedAmount = transaction.getBudgetedAmount();
			balance.add(effectiveAmountFunction.apply(transaction));
			budgetedBalance.add(budgetedAmountFunction.apply(transaction));
		} else {
			amount = 0 - transaction.getEffectiveAmount();
			budgetedAmount = 0 - transaction.getBudgetedAmount();
			balance.subtract(effectiveAmountFunction.apply(transaction));
			budgetedBalance.subtract(budgetedAmountFunction.apply(transaction));
		}

		return new TransactionElement(transaction, amount, budgetedAmount, balance.getBalance(),
				budgetedBalance.getBalance());
	}

}
