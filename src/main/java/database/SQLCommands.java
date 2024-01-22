package database;

public class SQLCommands {
    private SQLCommands() throws Exception {
	throw new Exception();
    }

    public static final String SELECT_WALLET_NAMES = "SELECT WalletName FROM Wallets";
    public static final String SELECT_WALLET = "SELECT * FROM Wallets WHERE WalletName = ?";
    public static final String SELECT_TRANSACTIONS = "SELECT * FROM Transactions "
	    + "LEFT JOIN TransactionTypes ON Transactions.TransactionTypeId = TransactionTypes.Id "
	    + "WHERE TransactionTypeName LIKE ? AND WalletId = ? AND Date > ?";
    public static final String SELECT_TRANSACTION_TYPE = "SELECT * FROM TransactionTypes WHERE TransactionTypeName LIKE ?";

    public static final String INSERT_TRANSACTION = "INSERT INTO Transactions(Description, Sum, Date, TransactionTypeId, WalletId) VALUES(?, ?, ?, ?, ?)";

    public static final String UPDATE_BALANCE = "UPDATE Wallets SET Balance = ? WHERE Id = ?";

}
