package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletLock extends CommandJson {

	public static class Result {
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_lock";
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	
	public static void run() throws BTSSystemException {
		run(0);
	}
	public static void run(int id) throws BTSSystemException {
		WalletLock i = (WalletLock) new WalletLock().doRun(id, COMMAND);
		i.checkSystemException();
	}

	public static void main(String[] args) {
		//test();
		try {
			run();
			System.out.println("SUCCESS");
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":null}";
	}
}
