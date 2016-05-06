package ua.bd_lozenko.opinta.domain;

public abstract class Field<T> {
	protected T value;
	
	public abstract boolean validate(String value);

	public T getValue() {
		return value;
	}

	public abstract void setValue(String value);
	
	public abstract String getTypeDescription();

	public abstract int getValueLength();
}
