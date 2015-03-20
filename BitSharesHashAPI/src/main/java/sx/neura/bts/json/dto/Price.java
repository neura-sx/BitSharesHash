package sx.neura.bts.json.dto;

public class Price {
	private double ratio;
	private int quote_asset_id;
	private int base_asset_id;
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public int getQuote_asset_id() {
		return quote_asset_id;
	}
	public void setQuote_asset_id(int quote_asset_id) {
		this.quote_asset_id = quote_asset_id;
	}
	public int getBase_asset_id() {
		return base_asset_id;
	}
	public void setBase_asset_id(int base_asset_id) {
		this.base_asset_id = base_asset_id;
	}
}
