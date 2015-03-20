package sx.neura.bts.gui.dto;

import java.util.List;

import sx.neura.bts.json.dto.Asset;

public class AmountAndAccounts implements Comparable<AmountAndAccounts> {
	private Asset asset;
	private long amount;
	private List<AccountAndAmount> split;
	
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	public List<AccountAndAmount> getSplit() {
		return split;
	}
	public void setSplit(List<AccountAndAmount> split) {
		this.split = split;
	}
	@Override
	public int compareTo(AmountAndAccounts o) {
		return getAsset().compareTo(o.getAsset());
	}
}
