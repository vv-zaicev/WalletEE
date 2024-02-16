package transactions.filter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.function.Predicate;

import transactions.Transaction;
import transactions.TransactionType;

public class TransactionFilter implements Predicate<Transaction> {
    private Operation<TransactionType> transactionType;
    private Operation<BigDecimal> minSum;
    private Operation<BigDecimal> maxSum;
    private Operation<Calendar> minDate;
    private Operation<Calendar> maxDate;
    private int limit;

    public static class Builder {
	private Operation<TransactionType> transactionType;
	private Operation<BigDecimal> minSum;
	private Operation<BigDecimal> maxSum;
	private Operation<Calendar> minDate;
	private Operation<Calendar> maxDate;
	private int limit = Integer.MAX_VALUE;

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
	    this.minDate = date;
	    return this;
	}

	public Builder limit(int limit) {
	    this.limit = limit;
	    return this;
	}

	public TransactionFilter build() {
	    return new TransactionFilter(this);
	}

    }

    private TransactionFilter(Builder builder) {
	transactionType = builder.transactionType;
	minSum = builder.minSum;
	maxSum = builder.maxSum;
	minDate = builder.minDate;
	maxDate = builder.maxDate;
	limit = builder.limit;
    }

    @Override
    public boolean test(Transaction transaction) {
	if (transactionType != null && transactionType.check(transaction.type())) {
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

    public Operation<TransactionType> getTransactionType() {
	return transactionType;
    }

    public Operation<BigDecimal> getMinSum() {
	return minSum;
    }

    public Operation<BigDecimal> getMaxSum() {
	return maxSum;
    }

    public Operation<Calendar> getMinDate() {
	return minDate;
    }

    public Operation<Calendar> getMaxDate() {
	return maxDate;
    }

    public int getLimit() {
	return limit;
    }

}
