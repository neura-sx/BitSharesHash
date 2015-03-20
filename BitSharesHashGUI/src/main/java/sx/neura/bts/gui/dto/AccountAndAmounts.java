package sx.neura.bts.gui.dto;

import java.util.List;

import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;

public class AccountAndAmounts implements Comparable<AccountAndAmounts> {
	private Account account;
	private List<Amount> amounts;
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public List<Amount> getAmounts() {
		return amounts;
	}
	public void setAmounts(List<Amount> amounts) {
		this.amounts = amounts;
	}
	@Override
	public int compareTo(AccountAndAmounts o) {
		return getAccount().compareTo(o.getAccount());
	}
}
