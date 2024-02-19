package transactions.filter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.function.Predicate;

import transactions.Transaction;
import transactions.TransactionCategory;
import transactions.TransactionType;

public class TransactionFilter implements Predicate<Transaction> {
    private Operation<TransactionType> transactionType;
    private Operation<BigDecimal> minSum;
    private Operation<BigDecimal> maxSum;
    private Operation<Calendar> minDate;
    private Operation<Calendar> maxDate;
    private Operation<TransactionCategory> transactionCat;

    public static class Builder {
	private Operation<TransactionType> transactionType;
	private Operation<BigDecimal> minSum;
	private Operation<BigDecimal> maxSum;
	private Operation<Calendar> minDate;
	private Operation<Calendar> maxDate;
	private Operation<TransactionCategory> transactionCat;

	public Builder() {

	}

	public Builder transactionType(Operation<TransactionType> transactionType) {
	    this.transactionType = transactionType;
	    return this;
	}

	public Builder minSum(Operation<BigDecimal> sum) {
	    this.minSum = sum;
	    return this;
	}

	public Builder maxSum(Operation<BigDecimal> sum) {
	    this.maxSum = sum;
	    return this;
	}

	public Builder minDate(Operation<Calendar> date) {
	    this.minDate = date;
	    return this;
	}

	public Builder maxDate(Operation<Calendar> date) {
	    this.maxDate = date;
	    return this;
	}

	public Builder transactionCat(Operation<TransactionCategory> transactionCat) {
	    this.transactionCat = transactionCat;
	    return this;
	}

	public TransactionFilter build() {
	    return new TransactionFilter(this);
	}

    }

    private TransactionFilter(Builder builder) {
	transactionType = builder.transactionType;
	transactionCat = builder.transactionCat;
	minSum = builder.minSum;
	maxSum = builder.maxSum;
	minDate = builder.minDate;
	maxDate = builder.maxDate;
    }

    @Override
    public boolean test(Transaction transaction) {
	if (transactionType != null && transactionType.check(transaction.type())) {
	    return false;
	}
	if (transactionCat != null && transactionCat.check(transaction.category())) {
	    return false;
	}
	if (minDate != null && minDate.check(transaction.calendar())) {
	    return false;
	}
	if (maxDate != null && maxDate.check(transaction.calendar())) {
	    return false;
	}
	if (minSum != null && minSum.check(transaction.sum())) {
	    return false;
	}
	if (maxSum != null && maxSum.check(transaction.sum())) {
	    return false;
	}
	return true;
    }

    public TransactionType getTransactionType() {
	if (transactionType == null) {
	    return null;
	}
	return transactionType.getValue();
    }

    public TransactionCategory getTransactionCat() {
	if (transactionCat == null) {
	    return null;
	}
	return transactionCat.getValue();
    }

    public BigDecimal getMinSum() {
	if (minSum == null) {
	    return null;
	}
	return minSum.getValue();
    }

    public BigDecimal getMaxSum() {
	if (maxSum == null) {
	    return null;
	}
	return maxSum.getValue();
    }

    public Calendar getMinDate() {
	if (minDate == null) {
	    return null;
	}
	return minDate.getValue();
    }

    public Calendar getMaxDate() {
	if (maxDate == null) {
	    return null;
	}
	return maxDate.getValue();
    }

}
