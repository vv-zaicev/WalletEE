package transactions;

import java.math.BigDecimal;
import java.util.Calendar;

import transactions.filter.Operation;
import transactions.filter.TransactionFilter;

public record Transaction(String descriprion, BigDecimal sum, TransactionType type, Calendar calendar) {

    public boolean isCorrect(TransactionFilter transactionFilter) {
	Operation<BigDecimal> sumOperation = transactionFilter.getSum();
	Operation<Calendar> dateOperation = transactionFilter.getDate();
	Operation<TransactionType> transactionTypeOperation = transactionFilter.getTransactionType();

	if (sumOperation != null && sumOperation.check(sum)) {
	    return false;
	}
	if (dateOperation != null && dateOperation.check(calendar)) {
	    return false;
	}
	if (transactionTypeOperation != null && transactionTypeOperation.check(type)) {
	    return false;
	}
	return true;
    }
}
