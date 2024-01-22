package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import transactions.Transaction;
import transactions.TransactionType;
import transactions.Wallet;

public class DatabaseController implements AutoCloseable {
    private static final String HOST = "jdbc:mysql://localhost:3306/wallets";
    private Connection connection;
    private ResultSet currentWallet;

    public DatabaseController(String username, String password) throws SQLException {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
	    System.out.println("Class not found " + e);
	}
	connection = DriverManager.getConnection(HOST, username, password);

    }

    public boolean setCurrentWallet(String name) {
	try {
	    PreparedStatement walletStatement = connection.prepareStatement(SQLCommands.SELECT_WALLET, ResultSet.TYPE_SCROLL_INSENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    walletStatement.setString(1, name);
	    ResultSet wallet = walletStatement.executeQuery();

	    currentWallet = wallet;

	    currentWallet.first();

	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public Wallet getWallet(int numberMonths) {
	CachedRowSet transactionsInfo = getTransactionsInfo(numberMonths);
	CachedRowSet walletInfo = getWalletInfo();
	return new Wallet(walletInfo, transactionsInfo);
    }

    public List<String> getWalletNames() {
	List<String> walletNames = new ArrayList<String>();
	try {
	    Statement selectWalletStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    ResultSet wallets = selectWalletStatement.executeQuery(SQLCommands.SELECT_WALLET_NAMES);

	    while (wallets.next()) {
		walletNames.add(wallets.getString("WalletName"));
	    }

	    return walletNames;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return walletNames;
	}

    }

    private CachedRowSet getTransactionsInfo(int NumberMonths) {

	try {
	    int idWallet = currentWallet.getInt("id");
	    Calendar startTime = GregorianCalendar.getInstance();
	    startTime.add(Calendar.MONTH, -NumberMonths);

	    PreparedStatement transactionStatement = connection.prepareStatement(SQLCommands.SELECT_TRANSACTIONS, ResultSet.TYPE_SCROLL_INSENSITIVE,
		    ResultSet.CONCUR_READ_ONLY);
	    transactionStatement.setString(1, TransactionType.ALL.getSQLNameString());
	    transactionStatement.setInt(2, idWallet);
	    transactionStatement.setDate(3, new Date(startTime.getTimeInMillis()));

	    ResultSet transactionsResultSet = transactionStatement.executeQuery();

	    return cacheResultSet(transactionsResultSet);

	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private CachedRowSet cacheResultSet(ResultSet resultSet) throws SQLException {
	RowSetFactory factory = RowSetProvider.newFactory();
	CachedRowSet cachedRowSet = factory.createCachedRowSet();
	cachedRowSet.populate(resultSet);
	return cachedRowSet;

    }

    private CachedRowSet getWalletInfo() {
	try {
	    currentWallet.absolute(0);
	    CachedRowSet walletInfo = cacheResultSet(currentWallet);
	    currentWallet.first();
	    return walletInfo;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}

    }

    public boolean addTransaction(Transaction transaction) {
	try {
	    PreparedStatement addTransactionsStatement = connection.prepareStatement(SQLCommands.INSERT_TRANSACTION);
	    connection.setAutoCommit(false);

	    addTransactionsStatement.setString(1, transaction.descriprion());
	    addTransactionsStatement.setBigDecimal(2, transaction.sum());
	    addTransactionsStatement.setDate(3, new java.sql.Date(transaction.calendar().getTimeInMillis()));
	    addTransactionsStatement.setInt(4, getIdTransactionType(transaction.type()));
	    addTransactionsStatement.setInt(5, currentWallet.getInt("Id"));
	    addTransactionsStatement.executeUpdate();
	    changeBalance(transaction);

	    connection.commit();
	    return true;
	} catch (SQLException e) {
	    try {
		connection.rollback();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	    e.printStackTrace();
	    return false;
	}

    }

    private void changeBalance(Transaction transaction) throws SQLException {
	BigDecimal currentBalance = currentWallet.getBigDecimal("Balance");

	if (transaction.type() == TransactionType.EXPENSES) {
	    currentBalance = currentBalance.subtract(transaction.sum());
	} else {
	    currentBalance = currentBalance.add(transaction.sum());
	}

	currentWallet.updateBigDecimal("Balance", currentBalance);
	currentWallet.updateRow();

    }

    private int getIdTransactionType(TransactionType transactionType) {
	try {
	    PreparedStatement transactionTypeStatement = connection.prepareStatement(SQLCommands.SELECT_TRANSACTION_TYPE,
		    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    transactionTypeStatement.setString(1, transactionType.getSQLNameString());
	    ResultSet transactionTypeResultSet = transactionTypeStatement.executeQuery();
	    transactionTypeResultSet.first();
	    return transactionTypeResultSet.getInt("Id");
	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;
	}
    }

    @Override
    public void close() throws Exception {
	connection.close();
    }

}
