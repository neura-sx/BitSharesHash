package sx.neura.bts.json.api.wallet;

import java.util.ArrayList;
import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletAccountVoteSummary extends CommandJson {

	public static class Result extends ArrayList<Object> {
		private String name;
		private long votes;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public long getVotes() {
			return votes;
		}
		public void setVotes(long votes) {
			this.votes = votes;
		}
		private static final long serialVersionUID = 1L;
		public Result convert() {
			for (Object obj : this) {
				if (obj instanceof String)
					name = (String) obj;
				else if (obj instanceof Integer)
					votes = new Long((Integer) obj);
				else if (obj instanceof Long)
					votes = (Long) obj;
			}
			return this;
		}
	}
	
	private List<Result> result;

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public List<Result> getResult() {
		for (Result r : result)
			r.convert();
		return result;
	}
	
	private static final String COMMAND = "wallet_account_vote_summary";
	
	public static CommandJson test(String accountName) {
		return doTest(COMMAND, new Object[] { accountName });
	}
	
	public static List<WalletAccountVoteSummary.Result> run(String accountName) throws BTSSystemException {
		return run(0, accountName);
	}
	public static List<WalletAccountVoteSummary.Result> run(int id, String accountName) throws BTSSystemException {
		WalletAccountVoteSummary i = (WalletAccountVoteSummary) new WalletAccountVoteSummary().doRun(id, COMMAND,
				new Object[] { accountName });
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
//		test("gringox");
		try {
			List<WalletAccountVoteSummary.Result> list = run("gringox");
			for (WalletAccountVoteSummary.Result result : list) {
				System.out.println(String.format("[%30s]: %d",
						result.getName(),
						result.getVotes()));
			}
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[[\"luckybit\",5229006],[\"madam\",5229006],[\"now.dacwin\",5229006]]}";
	}
}
