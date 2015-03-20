package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletOpen extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_open";
	
	public static CommandJson test() {
		return doTest(COMMAND, new Object[] { "default" });
	}
	public static CommandJson test(String name) {
		return doTest(COMMAND, new Object[] { name });
	}
	
	public static void run() throws BTSSystemException {
		run(0, "default");
	}
	public static void run(String name) throws BTSSystemException {
		run(0, name);
	}
	public static void run(int id, String name) throws BTSSystemException {
		WalletOpen i = (WalletOpen) new WalletOpen().doRun(id, COMMAND,
				new Object[] { name });
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
