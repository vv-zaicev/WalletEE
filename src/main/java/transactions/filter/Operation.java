package transactions.filter;

public class Operation<T extends Comparable<T>> {
    private CompareType compareType;
    private T value;

    public Operation(CompareType compareType, T value) {
	this.compareType = compareType;
	this.value = value;
    }

    public boolean check(T checkedValue) {
	return checkedValue.compareTo(value) != compareType.getResult();
    }

}
