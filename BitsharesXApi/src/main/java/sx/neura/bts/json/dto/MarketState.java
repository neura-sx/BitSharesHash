package sx.neura.bts.json.dto;

public class MarketState {
	private long balance;
	private Price limit_price;
	private String last_update;
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public Price getLimit_price() {
		return limit_price;
	}
	public void setLimit_price(Price limit_price) {
		this.limit_price = limit_price;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
}
