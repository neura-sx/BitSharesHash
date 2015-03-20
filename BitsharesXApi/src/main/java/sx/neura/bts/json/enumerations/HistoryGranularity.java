package sx.neura.bts.json.enumerations;

public enum HistoryGranularity {
	EACH_BLOCK("each_block"),
	EACH_HOUR("each_hour"),
	EACH_DAY("each_day");
	
	private String value;
	private HistoryGranularity(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return value;
	}
}
