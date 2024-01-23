package transactions;

import java.math.BigDecimal;
import java.util.Calendar;

import transactions.filter.Operation;
import transactions.filter.TransactionFilter;

public class Transaction {

    private String descriprion;
    private BigDecimal sum;
    private TransactionType type;
    private Calendar calendar;
    private int id;

    public Transaction(String descriprion, BigDecimal sum, TransactionType type, Calendar calendar, int id) {
	super();
	this.descriprion = descriprion;
	this.sum = sum;
	this.type = type;
	this.calendar = calendar;
	this.id = id;
    }

    public String descriprion() {
	return descriprion;
    }

    public void setDescriprion(String descriprion) {
	this.descriprion = descriprion;
    }

    public BigDecimal sum() {
	return sum;
    }

    public void setSum(BigDecimal sum) {
	this.sum = sum;
    }

    public TransactionType type() {
	return type;
    }

    public void setType(TransactionType type) {
	this.type = type;
    }

    public Calendar calendar() {
	return calendar;
    }

    public void setCalendar(Calendar calendar) {
	this.calendar = calendar;
    }

    public int id() {
	return id;
    }

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
