package sx.neura.bts.gui.dto;

import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;


public class AccountAndAmount implements Comparable<AccountAndAmount> {
	private Account account;
	private Amount amount;
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	@Override
	public int compareTo(AccountAndAmount o) {
		return account.compareTo(o.account);
	}
}
