package transactions;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
	    Transaction transaction = new Transaction(transactionsInfo.getString("Description"), transactionsInfo.getBigDecimal("Sum"), type,
		    calendar);

	    addTransaction(transaction, false);
	}
    }

    public void addTransaction(Transaction transaction) {
	addTransaction(transaction, true);
    }

    public void addTransaction(Transaction transaction, boolean needChangeBalance) {
	transactions.add(transaction);
	changeSums(transaction, needChangeBalance);
    }

    private void changeSums(Transaction transaction, boolean needChangeBalance) {
	if (transaction.type() == TransactionType.INCOME) {
	    if (needChangeBalance)
		balance = balance.add(transaction.sum());
	    income = income.add(transaction.sum());
	} else if (transaction.type() == TransactionType.EXPENSES) {
	    if (needChangeBalance)
		balance = balance.subtract(transaction.sum());
	    expenses = expenses.add(transaction.sum());
	}
    }

    public void setBalance(BigDecimal balance) {
	this.balance = balance;
    }

    public void displayWalletName() {
	System.out.println(walletName);
    }

    public List<Transaction> getTransactions() {
	return transactions;
    }

    public List<Transaction> getTransactions(TransactionFilter transactionFilter) {
	return transactions.stream().filter(x -> x.isCorrect(transactionFilter)).limit(transactionFilter.getLimit())
		.sorted((x, y) -> y.calendar().compareTo(x.calendar())).collect(Collectors.toList());
    }
}
