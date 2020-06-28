package ch.bader.budget.server.calculation;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ch.bader.budget.server.calculation.implementation.function.EffectiveAmountFunction;
import ch.bader.budget.server.calculation.implementation.predicate.TransactionInMonthPredicate;
import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.entity.VirtualAccount;

public class MatrixCalculator {

	static final String OTHER_KEY = "Other";

	static final String SUM_KEY = "Sum";

	static final EffectiveAmountFunction effectiveAmountFunction = new EffectiveAmountFunction();

	public static Map<String, Map<String, Number>> getMatrix(List<VirtualAccount> accounts, LocalDate withDayOfMonth) {

		Predicate<Transaction> transactionInMonth = new TransactionInMonthPredicate(withDayOfMonth);

		Map<String, Map<String, Number>> accountMap = new LinkedHashMap<>();

		accounts.stream().distinct().forEach(account -> accountMap.put(account.getName(),
				getMatrixLineForAccount(accounts, account, transactionInMonth)));

		return accountMap;
	}

	public static Map<String, Number> getMatrixLineForAccount(List<VirtualAccount> accounts, VirtualAccount account,
			Predicate<Transaction> transactionInMonthPredicate) {

		Map<String, Number> accountMap = new LinkedHashMap<>();
		accounts.stream().distinct().forEach(a -> accountMap.put(a.getName(), 0));
		accountMap.put(OTHER_KEY, 0);
		accountMap.put(SUM_KEY, 0);

		List<Transaction> transactions = account.getCreditedTransactions().stream().filter(transactionInMonthPredicate)
				.collect(Collectors.toList());

		transactions.stream().distinct().forEach(a -> addTransactionToMap(a, accountMap));
		return accountMap;

	}

	private static void addTransactionToMap(Transaction a, Map<String, Number> accountMap) {
		String key = a.getDebitedAccount().getName();
		float effectiveAmount = effectiveAmountFunction.apply(a).floatValue();
		if (accountMap.containsKey(key)) {
			accountMap.put(key, accountMap.get(key).floatValue() + effectiveAmount);
		} else {
			accountMap.put(OTHER_KEY, accountMap.get(OTHER_KEY).floatValue() + effectiveAmount);
		}

		accountMap.put(SUM_KEY, accountMap.get(SUM_KEY).floatValue() + effectiveAmount);
	}
}