package sx.neura.bts.json.dto;

public class Amount {
	private long value;
	private int asset_id;
	public void setAmount(long value) {
		this.value = value;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public int getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(int asset_id) {
		this.asset_id = asset_id;
	}
}
