package database;

public class SQLCommands {
    private SQLCommands() throws Exception {
	throw new Exception();
    }

    public static final String SELECT_WALLETS = "SELECT * FROM Wallets";
    public static final String SELECT_WALLET = "SELECT * FROM Wallets WHERE Id = ?";
    public static final String SELECT_TRANSACTION = "SELECT * FROM Transactions LEFT JOIN TransactionTypes ON Transactions.TransactionTypeId = TransactionTypes.Id WHERE Transactions.Id = ?";
    public static final String SELECT_TRANSACTIONS = "SELECT * FROM Transactions "
	    + "LEFT JOIN TransactionTypes ON Transactions.TransactionTypeId = TransactionTypes.Id "
	    + "LEFT JOIN TransactionCategory ON Transactions.TransactionCategoryId = TransactionCategory.Id "
	    + "WHERE TransactionTypeName LIKE ? AND WalletId = ? AND Date > ?";
    public static final String SELECT_TRANSACTION_TYPE = "SELECT * FROM TransactionTypes WHERE TransactionTypeName LIKE ?";

    public static final String SELECT_TRANSACTION_CATEGORY = "SELECT * FROM TransactionCategory WHERE Id = ?";
    public static final String SELECT_TRANSACTION_CATEGORIES = "SELECT * FROM TransactionCategory LEFT JOIN TransactionTypes ON TransactionCategory.TransactionTypeId = TransactionTypes.Id";

    public static final String INSERT_TRANSACTION = "INSERT INTO Transactions(Description, Sum, Date, TransactionTypeId, TransactionCategoryId, WalletId) VALUES(?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_TRANSACTION = "UPDATE Transactions SET Description = ?, Sum = ?, Date = ?, TransactionTypeId = ?, TransactionCategoryId = ? WHERE Id = ?";
    public static final String DELETE_TRANSACTION = "DELETE FROM Transactions WHERE Id = ?";

    public static final String DELETE_WALLET = "DELETE FROM Wallets WHERE Id = ?";
    public static final String DELETE_WALLET_TRANSACTIONS = "DELETE FROM Transactions WHERE WalletId = ?";
    public static final String INSERT_WALLET = "INSERT INTO Wallets(WalletName, Balance) VALUES(?, ?)";
    public static final String UPDATE_WALLET = "UPDATE Wallets SET WalletName = ?, Balance = ? WHERE Id = ?";

}
