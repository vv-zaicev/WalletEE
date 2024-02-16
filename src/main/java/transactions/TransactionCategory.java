package transactions;

public record TransactionCategory(String name, int id) implements Comparable<TransactionCategory> {

    @Override
    public int compareTo(TransactionCategory transactionCategory) {

	return this.id - transactionCategory.id();
    }

}
