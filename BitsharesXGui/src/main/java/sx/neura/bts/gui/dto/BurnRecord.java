package sx.neura.bts.gui.dto;

import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.enumerations.BurnMethod;


public class BurnRecord implements Comparable<BurnRecord> {
//	private Account account;
	private Amount amount;
	private BurnMethod method;
	private String message;
//	public Account getAccount() {
//		return account;
//	}
//	public void setAccount(Account account) {
//		this.account = account;
//	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	public BurnMethod getMethod() {
		return method;
	}
	public void setMethod(BurnMethod method) {
		this.method = method;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public int compareTo(BurnRecord o) {
		return 0;
	}
}
