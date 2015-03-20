package sx.neura.bts.json.dto;

public class Asset implements Comparable<Asset> {
	private int id;
	private String symbol;
	private String name;
	private String description;
	private Object public_data;
	private long issuer_account_id;
	private int precision;
	private String registration_date;
	private String last_update;
	private long maximum_share_supply;
	private long current_share_supply;
	private long collected_fees;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Object getPublic_data() {
		return public_data;
	}
	public void setPublic_data(Object public_data) {
		this.public_data = public_data;
	}
	public long getIssuer_account_id() {
		return issuer_account_id;
	}
	public void setIssuer_account_id(long issuer_account_id) {
		this.issuer_account_id = issuer_account_id;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public String getRegistration_date() {
		return registration_date;
	}
	public void setRegistration_date(String registration_date) {
		this.registration_date = registration_date;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public long getMaximum_share_supply() {
		return maximum_share_supply;
	}
	public void setMaximum_share_supply(long maximum_share_supply) {
		this.maximum_share_supply = maximum_share_supply;
	}
	public long getCurrent_share_supply() {
		return current_share_supply;
	}
	public void setCurrent_share_supply(long current_share_supply) {
		this.current_share_supply = current_share_supply;
	}
	public long getCollected_fees() {
		return collected_fees;
	}
	public void setCollected_fees(long collected_fees) {
		this.collected_fees = collected_fees;
	}
	@Override
	public int compareTo(Asset o) {
		return getSymbol().compareTo(o.getSymbol());
	}
}
