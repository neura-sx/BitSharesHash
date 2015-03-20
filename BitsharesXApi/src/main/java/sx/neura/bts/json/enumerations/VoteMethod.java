package sx.neura.bts.json.enumerations;

public enum VoteMethod {
	ALL("vote_all"),
	RANDOM("vote_random"),
	RECOMMENDED("vote_recommended"),
	NONE("vote_none");
	
	private String value;
	private VoteMethod(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return value;
	}
}
