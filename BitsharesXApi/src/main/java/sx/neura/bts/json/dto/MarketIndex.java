package sx.neura.bts.json.dto;

public class MarketIndex {
	private Price order_price;
	private String owner;
	public Price getOrder_price() {
		return order_price;
	}
	public void setOrder_price(Price order_price) {
		this.order_price = order_price;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
