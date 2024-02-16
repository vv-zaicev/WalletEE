package transactions;

import java.math.BigDecimal;
import java.util.Calendar;

public class Transaction {

    private String description;
    private BigDecimal sum;
    private TransactionType type;
    private Calendar calendar;
    private int id;
    private TransactionCategory category;

    public Transaction(String description, BigDecimal sum, TransactionType type, Calendar calendar, TransactionCategory category) {
	this.description = description == null ? "" : description;
	this.category = category;
	this.sum = sum;
	this.type = type;
	this.calendar = calendar;
    }

    public Transaction(String description, BigDecimal sum, TransactionType type, Calendar calendar, TransactionCategory category, int id) {
	this(description, sum, type, calendar, category);
	this.id = id;
    }

    public String description() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
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

    public TransactionCategory category() {
	return category;
    }

    public void setCategory(TransactionCategory category) {
	this.category = category;
    }

    public int id() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }
}
