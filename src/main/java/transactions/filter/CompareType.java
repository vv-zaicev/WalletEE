package transactions.filter;

public enum CompareType {
    MORE(1), LESS(-1), EQUALS(0);

    private int result;

    CompareType(int result) {
	this.result = result;
    }

    public int getResult() {
	return result;
    }
}
