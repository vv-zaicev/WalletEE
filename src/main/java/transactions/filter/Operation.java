package transactions.filter;

public class Operation<T extends Comparable<T>> {
    private CompareType compareType;
    private T value;

    public Operation(CompareType compareType, T value) {
	this.compareType = compareType;
	this.value = value;
    }

    public boolean check(T checkedValue) {
	int result = checkedValue.compareTo(value);
	return result != compareType.getResult() && result != 0;
    }

    public T getValue() {
	return value;
    }

}
