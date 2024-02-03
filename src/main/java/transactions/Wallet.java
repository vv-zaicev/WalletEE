package transactions;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.sql.rowset.CachedRowSet;

import transactions.filter.TransactionFilter;

public class Wallet {
    private List<Transaction> transactions;
    private BigDecimal balance;
    private BigDecimal income;
    private BigDecimal expenses;
    private String walletName;

    public Wallet(CachedRowSet walletInfo, CachedRowSet transactionsInfo) {
	transactions = new ArrayList<Transaction>();
	balance = new BigDecimal(0);
	income = new BigDecimal(0);
	expenses = new BigDecimal(0);
	try {
	    walletInfo.first();
	    this.walletName = walletInfo.getString("WalletName");
	    this.balance = walletInfo.getBigDecimal("balance");
	    fillTransactions(transactionsInfo);
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public BigDecimal getBalance() {
	return balance;
    }

    public BigDecimal getIncome() {
	return income;
    }

    public BigDecimal getExpenses() {
	return expenses;
    }

    public String getWalletName() {
	return walletName;
    }

    public void fillTransactions(CachedRowSet transactionsInfo) throws SQLException {
	while (transactionsInfo.next()) {
	    TransactionType type = TransactionType.valueOf(transactionsInfo.getString("TransactionTypeName").toUpperCase());
	    Date date = transactionsInfo.getDate("Date");
	    Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTime(date);
	    int id = transactionsInfo.getInt("Id");
	    TransactionCategory category = new TransactionCategory(transactionsInfo.getString("TransactionCategoryName"),
		    transactionsInfo.getInt("TransactionCategoryId"));
	    Transaction transaction = new Transaction(transactionsInfo.getString("Description"), transactionsInfo.getBigDecimal("Sum"), type,
		    calendar, category, id);

	    transactions.add(transaction);

	}
	changeSums();
    }

    public void addTransaction(Transaction transaction) {
	transactions.add(transaction);
	changeSums();
	changeBalance(transaction, x -> x == TransactionType.INCOME);
    }

    public void removeTransaction(Transaction transaction) {
	transactions.remove(transaction);
	changeSums();
	changeBalance(transaction, x -> x == TransactionType.EXPENSES);
    }

    private void changeSums() {
	income = transactions.stream().filter(x -> x.type() == TransactionType.INCOME).map(x -> x.sum()).reduce(BigDecimal.ZERO, BigDecimal::add);
	expenses = transactions.stream().filter(x -> x.type() == TransactionType.EXPENSES).map(x -> x.sum()).reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private void changeBalance(Transaction transaction, Predicate<TransactionType> predicate) {
	if (predicate.test(transaction.type())) {
	    balance.add(transaction.sum());
	} else {
	    balance.subtract(transaction.sum());
	}
    }

    public void setBalance(BigDecimal balance) {
	this.balance = balance;
    }

    public void displayWalletName() {
	System.out.println(walletName);
    }

    public Transaction getTransaction(int id) {
	return transactions.stream().filter(x -> x.id() == id).findFirst().orElse(null);
    }

    public List<Transaction> getTransactions() {
	return transactions.stream().sorted((x, y) -> y.calendar().compareTo(x.calendar())).collect(Collectors.toList());
    }

    public List<Transaction> getTransactions(TransactionFilter transactionFilter) {
	return transactions.stream().filter(x -> x.isCorrect(transactionFilter)).sorted((x, y) -> y.calendar().compareTo(x.calendar()))
		.limit(transactionFilter.getLimit()).collect(Collectors.toList());
    }
}
