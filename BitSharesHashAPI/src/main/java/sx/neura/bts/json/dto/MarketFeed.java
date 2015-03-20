package sx.neura.bts.json.dto;

public class MarketFeed implements Comparable<MarketFeed> {
	private String delegate_name;
	private double price;
	private String last_update;
	private String asset_symbol;
	private double median_price;
	public String getDelegate_name() {
		return delegate_name;
	}
	public void setDelegate_name(String delegate_name) {
		this.delegate_name = delegate_name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public String getAsset_symbol() {
		return asset_symbol;
	}
	public void setAsset_symbol(String asset_symbol) {
		this.asset_symbol = asset_symbol;
	}
	public double getMedian_price() {
		return median_price;
	}
	public void setMedian_price(double median_price) {
		this.median_price = median_price;
	}
	@Override
	public int compareTo(MarketFeed o) {
		return asset_symbol.compareTo(o.asset_symbol);
	}
}
