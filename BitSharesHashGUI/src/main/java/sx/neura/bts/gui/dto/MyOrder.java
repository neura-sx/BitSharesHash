package sx.neura.bts.gui.dto;

import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.MarketOrder;

public class MyOrder implements Comparable<MyOrder> {
	public MyOrder(String id, MarketOrder order, Account account) {
		this.id = id;
		this.order = order;
		this.account = account;
	}
	private String id;
	private MarketOrder order;
	private Account account;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MarketOrder getOrder() {
		return order;
	}
	public void setOrder(MarketOrder order) {
		this.order = order;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@Override
	public int compareTo(MyOrder o) {
		return id.compareTo(o.id);
	}
}
