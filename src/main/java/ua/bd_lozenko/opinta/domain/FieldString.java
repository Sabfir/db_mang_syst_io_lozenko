package ua.bd_lozenko.opinta.domain;

public class FieldString extends Field<String>{

	@Override
	public boolean validate(String value) {
		return true;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String getTypeDescription() {
		return "STRING";
	}

	@Override
	public int getValueLength() {
		return 50;
	}
}
