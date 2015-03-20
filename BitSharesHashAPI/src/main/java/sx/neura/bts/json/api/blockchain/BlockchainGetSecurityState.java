package sx.neura.bts.json.api.blockchain;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetSecurityState extends CommandJson {
	
	public static class Result {
		private String alert_level;
		private int estimated_confirmation_seconds;
		private double participation_rate;
		public String getAlert_level() {
			return alert_level;
		}
		public void setAlert_level(String alert_level) {
			this.alert_level = alert_level;
		}
		public int getEstimated_confirmation_seconds() {
			return estimated_confirmation_seconds;
		}
		public void setEstimated_confirmation_seconds(int estimated_confirmation_seconds) {
			this.estimated_confirmation_seconds = estimated_confirmation_seconds;
		}
		public double getParticipation_rate() {
			return participation_rate;
		}
		public void setParticipation_rate(double participation_rate) {
			this.participation_rate = participation_rate;
		}
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "blockchain_get_security_state";
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	
	public static Result run() throws BTSSystemException {
		return run(0);
	}
	public static Result run(int id) throws BTSSystemException {
		BlockchainGetSecurityState i = (BlockchainGetSecurityState) new BlockchainGetSecurityState().doRun(id, COMMAND);
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
//		test();
		try {
			Result result = BlockchainGetSecurityState.run();
			System.out.println(String.format("%s %d %.2f", result.getAlert_level(), result.getEstimated_confirmation_seconds(), result.getParticipation_rate()));
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"alert_level\":\"yellow\",\"estimated_confirmation_seconds\":10,\"participation_rate\":65.584415584415581}}";
	}
}
