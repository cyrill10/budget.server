package ch.bader.budget.server.calculation;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import ch.bader.budget.server.calculation.implementation.predicate.TransactionBeforePredicate;
import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.entity.VirtualAccount;

public class VirtaulAccountCalculator {

	public static float getBalanceAt(VirtualAccount account, LocalDate date,
			Function<Transaction, Number> balanceTypeFunction) {
		Balance balance = new Balance(account.getBalance());
		Predicate<Transaction> beforePredicate = new TransactionBeforePredicate(date);

		account.getCreditedTransactions().stream().distinct().filter(beforePredicate).map(balanceTypeFunction)
				.forEach(balance::subtract);
		account.getDebitedTransactions().stream().distinct().filter(beforePredicate).map(balanceTypeFunction)
				.forEach(balance::add);
		return balance.getBalance();

	}

	public static float getBalanceAt(List<VirtualAccount> accounts, LocalDate date,
			Function<Transaction, Number> balanceTypeFunction) {
		Balance balance = new Balance(0);
		accounts.stream().distinct().forEach(account -> {
			balance.add(getBalanceAt(account, date, balanceTypeFunction));
		});
		return balance.getBalance();

	}

	public static float getBalanceAfterTransactions(float value, List<Transaction> credited, List<Transaction> debited,
			Function<Transaction, Number> balanceTypeFunction) {
		Balance balance = new Balance(value);

		credited.stream().distinct().map(balanceTypeFunction).forEach(balance::subtract);
		debited.stream().distinct().map(balanceTypeFunction).forEach(balance::add);
		return balance.getBalance();

	}
}
