package sx.neura.bts.json.enumerations;

public enum BurnMethod {
	FOR("for"),
	AGAINST("against");
	
	private String value;
	private BurnMethod(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return value;
	}
}
