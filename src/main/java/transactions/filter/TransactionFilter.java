package transactions.filter;

import java.math.BigDecimal;
import java.util.Calendar;

import transactions.TransactionType;

public class TransactionFilter {
    private Operation<TransactionType> transactionType;
    private Operation<BigDecimal> sum;
    private Operation<Calendar> date;
    private int limit;

    public static class Builder {
	private Operation<TransactionType> transactionType;
	private Operation<BigDecimal> sum;
	private Operation<Calendar> date;
	private int limit = Integer.MAX_VALUE;

	public Builder() {

	}

	public Builder transactionType(Operation<TransactionType> transactionType) {
	    this.transactionType = transactionType;
	    return this;
	}

	public Builder sum(Operation<BigDecimal> sum) {
	    this.sum = sum;
	    return this;
	}

	public Builder date(Operation<Calendar> date) {
	    this.date = date;
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
	sum = builder.sum;
	date = builder.date;
	limit = builder.limit;
    }

    public Operation<TransactionType> getTransactionType() {
	return transactionType;
    }

    public Operation<BigDecimal> getSum() {
	return sum;
    }

    public Operation<Calendar> getDate() {
	return date;
    }

    public int getLimit() {
	return limit;
    }

}
