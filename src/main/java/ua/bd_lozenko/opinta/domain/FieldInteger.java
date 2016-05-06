package ua.bd_lozenko.opinta.domain;

public class FieldInteger extends Field<Integer> {

	@Override
	public boolean validate(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public void setValue(String value) {
		this.value = Integer.parseInt(value);
	}

	@Override
	public String getTypeDescription() {
		return "INT";
	}

	@Override
	public int getValueLength() {
		return 25;
	}
}
