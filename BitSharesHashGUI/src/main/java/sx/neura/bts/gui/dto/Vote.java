package sx.neura.bts.gui.dto;

import sx.neura.bts.json.dto.Account;

public class Vote implements Comparable<Vote> {
	public Vote(Account account, long votes) {
		this.account = account;
		this.votes = votes;
	}
	private Account account;
	private long votes;
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public long getVotes() {
		return votes;
	}
	public void setVotes(long votes) {
		this.votes = votes;
	}
	@Override
	public int compareTo(Vote o) {
		return account.compareTo(o.account);
	}
}
