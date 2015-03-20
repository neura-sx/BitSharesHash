package sx.neura.bts.json.dto;

public class Account extends RegisteredName {
	private int index;
	private String account_address;
	private boolean is_my_account;
	private int approved;
	private boolean is_favorite;
	private boolean block_production_enabled;
	private int last_used_gen_sequence;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getAccount_address() {
		return account_address;
	}
	public void setAccount_address(String account_address) {
		this.account_address = account_address;
	}
	public boolean isIs_my_account() {
		return is_my_account;
	}
	public void setIs_my_account(boolean is_my_account) {
		this.is_my_account = is_my_account;
	}
	public int getApproved() {
		return approved;
	}
	public void setApproved(int approved) {
		this.approved = approved;
	}
	public boolean isIs_favorite() {
		return is_favorite;
	}
	public void setIs_favorite(boolean is_favorite) {
		this.is_favorite = is_favorite;
	}
	public boolean isBlock_production_enabled() {
		return block_production_enabled;
	}
	public void setBlock_production_enabled(boolean block_production_enabled) {
		this.block_production_enabled = block_production_enabled;
	}
	public int getLast_used_gen_sequence() {
		return last_used_gen_sequence;
	}
	public void setLast_used_gen_sequence(int last_used_gen_sequence) {
		this.last_used_gen_sequence = last_used_gen_sequence;
	}
}
