package sx.neura.bts.json.dto;

public class DelegateInfo {
	private long votes_for;
	private int blocks_produced;
	private int blocks_missed;
	private String next_secret_hash;
	private int last_block_num_produced;
	private int pay_rate;
	private long pay_balance;
	private long total_paid;
	private long total_burned;
	private String block_signing_key;
	private Object meta_data;
	public long getVotes_for() {
		return votes_for;
	}
	public void setVotes_for(long votes_for) {
		this.votes_for = votes_for;
	}
	public int getBlocks_produced() {
		return blocks_produced;
	}
	public void setBlocks_produced(int blocks_produced) {
		this.blocks_produced = blocks_produced;
	}
	public int getBlocks_missed() {
		return blocks_missed;
	}
	public void setBlocks_missed(int blocks_missed) {
		this.blocks_missed = blocks_missed;
	}
	public String getNext_secret_hash() {
		return next_secret_hash;
	}
	public void setNext_secret_hash(String next_secret_hash) {
		this.next_secret_hash = next_secret_hash;
	}
	public int getLast_block_num_produced() {
		return last_block_num_produced;
	}
	public void setLast_block_num_produced(int last_block_num_produced) {
		this.last_block_num_produced = last_block_num_produced;
	}
	public int getPay_rate() {
		return pay_rate;
	}
	public void setPay_rate(int pay_rate) {
		this.pay_rate = pay_rate;
	}
	public long getPay_balance() {
		return pay_balance;
	}
	public void setPay_balance(long pay_balance) {
		this.pay_balance = pay_balance;
	}
	public long getTotal_paid() {
		return total_paid;
	}
	public void setTotal_paid(long total_paid) {
		this.total_paid = total_paid;
	}
	public long getTotal_burned() {
		return total_burned;
	}
	public void setTotal_burned(long total_burned) {
		this.total_burned = total_burned;
	}
	public String getBlock_signing_key() {
		return block_signing_key;
	}
	public void setBlock_signing_key(String block_signing_key) {
		this.block_signing_key = block_signing_key;
	}
	public Object getMeta_data() {
		return meta_data;
	}
	public void setMeta_data(Object meta_data) {
		this.meta_data = meta_data;
	}
}
