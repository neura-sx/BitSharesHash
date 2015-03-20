package sx.neura.bts.gui.dto;

import java.util.List;

import sx.neura.bts.json.dto.Amount;

public class Transaction implements Comparable<Transaction> {
	private boolean is_virtual;
	private boolean is_confirmed;
	private boolean is_market;
	private boolean is_market_cancel;
	private String trx_id;
	private String block_num;
	private Amount fee;
	private String timestamp;
	private String expiration_timestamp;
	private String from_account;
	private String to_account;
	private Amount amount;
	private String memo;
	private Integer direction;
	private List<RunningBalance> running_balances;
	public boolean isIs_virtual() {
		return is_virtual;
	}
	public void setIs_virtual(boolean is_virtual) {
		this.is_virtual = is_virtual;
	}
	public boolean isIs_confirmed() {
		return is_confirmed;
	}
	public void setIs_confirmed(boolean is_confirmed) {
		this.is_confirmed = is_confirmed;
	}
	public boolean isIs_market() {
		return is_market;
	}
	public void setIs_market(boolean is_market) {
		this.is_market = is_market;
	}
	public boolean isIs_market_cancel() {
		return is_market_cancel;
	}
	public void setIs_market_cancel(boolean is_market_cancel) {
		this.is_market_cancel = is_market_cancel;
	}
	public String getTrx_id() {
		return trx_id;
	}
	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}
	public String getBlock_num() {
		return block_num;
	}
	public void setBlock_num(String block_num) {
		this.block_num = block_num;
	}
	public Amount getFee() {
		return fee;
	}
	public void setFee(Amount fee) {
		this.fee = fee;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getExpiration_timestamp() {
		return expiration_timestamp;
	}
	public void setExpiration_timestamp(String expiration_timestamp) {
		this.expiration_timestamp = expiration_timestamp;
	}
	public String getFrom_account() {
		return from_account;
	}
	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}
	public String getTo_account() {
		return to_account;
	}
	public void setTo_account(String to_account) {
		this.to_account = to_account;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public List<RunningBalance> getRunning_balances() {
		return running_balances;
	}
	public void setRunning_balances(List<RunningBalance> running_balances) {
		this.running_balances = running_balances;
	}
	
	public static class RunningBalance {
		private String name;
		private Amount amount;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Amount getAmount() {
			return amount;
		}
		public void setAmount(Amount amount) {
			this.amount = amount;
		}
	}

	@Override
	public int compareTo(Transaction o) {
		return o.getTimestamp().compareTo(getTimestamp());
	}
}
