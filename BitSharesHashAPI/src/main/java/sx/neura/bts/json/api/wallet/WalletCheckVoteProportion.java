package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletCheckVoteProportion extends CommandJson {

	public static class Result {
		private double utilization;
		private double negative_utilization;
		public double getUtilization() {
			return utilization;
		}
		public void setUtilization(double utilization) {
			this.utilization = utilization;
		}
		public double getNegative_utilization() {
			return negative_utilization;
		}
		public void setNegative_utilization(double negative_utilization) {
			this.negative_utilization = negative_utilization;
		}
	}
	
	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_check_vote_proportion";
	
	public static CommandJson test(String accountName) {
		return doTest(COMMAND, new Object[] { accountName });
	}
	
	public static Result run(String accountName) throws BTSSystemException {
		return run(0, accountName);
	}
	public static Result run(int id, String accountName) throws BTSSystemException {
		WalletCheckVoteProportion i = (WalletCheckVoteProportion) new WalletCheckVoteProportion().doRun(id, COMMAND, 
				new Object[] { accountName });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("gringox");
		try {
			Result result = run("gringox");
			System.out.println(String.format("%.8f %.8f", result.getUtilization(), result.getNegative_utilization()));
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"utilization\":0.0080582248046994209,\"negative_utilization\":0}}";
	}
}
