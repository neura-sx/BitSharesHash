package sx.neura.btsx.gui.dto;

import sx.neura.bts.json.dto.Asset;

public class AssetPair implements Comparable<AssetPair> {
	public AssetPair(Asset asset1, Asset asset2) {
		this.asset1 = asset1;
		this.asset2 = asset2;
	}
	private Asset asset1;
	private Asset asset2;
	public Asset getAsset1() {
		return asset1;
	}
	public void setAsset1(Asset asset1) {
		this.asset1 = asset1;
	}
	public Asset getAsset2() {
		return asset2;
	}
	public void setAsset2(Asset asset2) {
		this.asset2 = asset2;
	}
	@Override
	public int compareTo(AssetPair o) {
		return asset1.getSymbol().compareTo(o.getAsset1().getSymbol());
	}
}
