package sx.neura.bts.gui.dto;

import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;


public class AmountAndAccount {
	private Amount amount;
	private Account account;
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
