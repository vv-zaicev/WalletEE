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
import transactions.TransactionCategory;
import transactions.TransactionType;
import transactions.Wallet;

public class DatabaseController implements AutoCloseable {
    private static final String HOST = "jdbc:mysql://database:3306/wallets?useUnicode=true&characterEncoding=UTF-8";
    private Connection connection;
    private static int numberMonths = 1;

    public DatabaseController() throws SQLException {

	try {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
	    System.out.println("Class not found " + e);
	}
	connection = DriverManager.getConnection(HOST, "root", System.getenv("MYSQL_ROOT_PASSWORD"));

    }

    private ResultSet getCurrentWallet(int id) {
	try {
	    PreparedStatement walletStatement = connection.prepareStatement(SQLCommands.SELECT_WALLET, ResultSet.TYPE_SCROLL_INSENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    walletStatement.setInt(1, id);
	    ResultSet currentWallet = walletStatement.executeQuery();

	    currentWallet.first();

	    return currentWallet;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public Wallet getWallet(int id) {
	ResultSet currentWallet = getCurrentWallet(id);
	CachedRowSet transactionsInfo = getTransactionsInfo(currentWallet, numberMonths);
	CachedRowSet walletInfo = getWalletInfo(currentWallet);
	return new Wallet(walletInfo, transactionsInfo);
    }

    public List<Wallet> getWallets() {
	List<Wallet> wallets = new ArrayList<Wallet>();
	try {
	    Statement selectWalletStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    ResultSet walletsInfo = selectWalletStatement.executeQuery(SQLCommands.SELECT_WALLETS);

	    while (walletsInfo.next()) {
		CachedRowSet walletInfo = getWalletInfo(getCurrentWallet(walletsInfo.getInt("Id")));
		wallets.add(new Wallet(walletInfo));
	    }

	    return wallets;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return wallets;
	}

    }

    private CachedRowSet getTransactionsInfo(ResultSet currentWallet, int NumberMonths) {

	try {
	    int idWallet = currentWallet.getInt("Id");
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

    private CachedRowSet getWalletInfo(ResultSet currentWallet) {
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

    public boolean addTransaction(int walletId, Transaction transaction) {
	try {
	    PreparedStatement addTransactionsStatement = connection.prepareStatement(SQLCommands.INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
	    connection.setAutoCommit(false);

	    addTransactionsStatement.setString(1, transaction.description());
	    addTransactionsStatement.setBigDecimal(2, transaction.sum());
	    addTransactionsStatement.setDate(3, new java.sql.Date(transaction.calendar().getTimeInMillis()));
	    addTransactionsStatement.setInt(4, getIdTransactionType(transaction.type()));
	    addTransactionsStatement.setInt(5, transaction.category().id());
	    addTransactionsStatement.setInt(6, walletId);
	    addTransactionsStatement.executeUpdate();
	    changeBalance(walletId, transaction.sum(), transaction.type() == TransactionType.INCOME);

	    try (ResultSet generatedKeys = addTransactionsStatement.getGeneratedKeys()) {
		if (generatedKeys.next()) {
		    transaction.setId(generatedKeys.getInt(1));
		}
	    }

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

    public boolean updateTransaction(int walletId, Transaction transaction) {
	CachedRowSet currentTransaction = getTransactionInfo(transaction.id());
	try {
	    connection.setAutoCommit(false);

	    BigDecimal currentSum = currentTransaction.getBigDecimal("Sum");
	    TransactionType type = TransactionType.valueOf(currentTransaction.getString("TransactionTypeName").toUpperCase());
	    changeBalance(walletId, currentSum, !(type == TransactionType.INCOME));
	    PreparedStatement updateTransactionsStatement = connection.prepareStatement(SQLCommands.UPDATE_TRANSACTION);

	    updateTransactionsStatement.setString(1, transaction.description());
	    updateTransactionsStatement.setBigDecimal(2, transaction.sum());
	    updateTransactionsStatement.setDate(3, new java.sql.Date(transaction.calendar().getTimeInMillis()));
	    updateTransactionsStatement.setInt(4, getIdTransactionType(transaction.type()));
	    updateTransactionsStatement.setInt(5, transaction.category().id());
	    updateTransactionsStatement.setInt(6, transaction.id());
	    updateTransactionsStatement.executeUpdate();

	    changeBalance(walletId, transaction.sum(), transaction.type() == TransactionType.INCOME);

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

    public boolean removeTransaction(int walletId, Transaction transaction) {
	try {
	    connection.setAutoCommit(false);

	    changeBalance(walletId, transaction.sum(), !(transaction.type() == TransactionType.INCOME));
	    PreparedStatement deleteTransactionsStatement = connection.prepareStatement(SQLCommands.DELETE_TRANSACTION);
	    deleteTransactionsStatement.setInt(1, transaction.id());
	    deleteTransactionsStatement.executeUpdate();

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

    public boolean updateWallet(Wallet wallet, String newName, BigDecimal newBalance) {
	try {
	    PreparedStatement updateWalletStatement = connection.prepareStatement(SQLCommands.UPDATE_WALLET);
	    updateWalletStatement.setString(1, newName);
	    updateWalletStatement.setBigDecimal(2, newBalance);
	    updateWalletStatement.setInt(3, wallet.getId());
	    updateWalletStatement.executeUpdate();
	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}

    }

    public boolean createWallet(String name, BigDecimal balance) {
	try {
	    PreparedStatement createWalletStatement = connection.prepareStatement(SQLCommands.INSERT_WALLET);
	    createWalletStatement.setString(1, name);
	    createWalletStatement.setBigDecimal(2, balance);
	    createWalletStatement.executeUpdate();
	    return true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public boolean removeWallet(Wallet wallet) {
	try {
	    connection.setAutoCommit(false);

	    PreparedStatement deleteWalletStatement = connection.prepareStatement(SQLCommands.DELETE_WALLET);
	    PreparedStatement deleteWalletTransactionsStatement = connection.prepareStatement(SQLCommands.DELETE_WALLET_TRANSACTIONS);
	    deleteWalletTransactionsStatement.setInt(1, wallet.getId());
	    deleteWalletTransactionsStatement.executeUpdate();
	    deleteWalletStatement.setInt(1, wallet.getId());
	    deleteWalletStatement.executeUpdate();

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

    private void changeBalance(int walletId, BigDecimal sum, boolean isPositive) throws SQLException {
	ResultSet currentWallet = getCurrentWallet(walletId);
	BigDecimal currentBalance = currentWallet.getBigDecimal("Balance");

	if (!isPositive) {
	    currentBalance = currentBalance.subtract(sum);
	} else {
	    currentBalance = currentBalance.add(sum);
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

    public TransactionCategory getTransactionCategory(int id) {
	try {
	    PreparedStatement transactionCategoryStatement = connection.prepareStatement(SQLCommands.SELECT_TRANSACTION_CATEGORY,
		    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    transactionCategoryStatement.setInt(1, id);
	    ResultSet transactionCategoryResultSet = transactionCategoryStatement.executeQuery();
	    transactionCategoryResultSet.first();
	    return new TransactionCategory(transactionCategoryResultSet.getString("TransactionCategoryName"),
		    transactionCategoryResultSet.getInt("Id"));
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private CachedRowSet getTransactionInfo(int id) {
	try {
	    PreparedStatement transactionInfoStatement = connection.prepareStatement(SQLCommands.SELECT_TRANSACTION,
		    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    transactionInfoStatement.setInt(1, id);
	    CachedRowSet transactionInfo = cacheResultSet(transactionInfoStatement.executeQuery());
	    transactionInfo.first();
	    return transactionInfo;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}

    }

    public CachedRowSet getTransactionCategoriesInfo() {
	try {
	    PreparedStatement transactionCategoryStatement = connection.prepareStatement(SQLCommands.SELECT_TRANSACTION_CATEGORIES,
		    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    CachedRowSet transactionCategoryInfo = cacheResultSet(transactionCategoryStatement.executeQuery());
	    return transactionCategoryInfo;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public void close() throws Exception {
	connection.close();
    }

}
