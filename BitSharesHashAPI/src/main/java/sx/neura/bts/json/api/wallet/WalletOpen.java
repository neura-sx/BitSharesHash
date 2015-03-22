package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

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
	
	public static CommandJson test(String name) {
		return doTest(COMMAND, new Object[] { name });
	}
	
	public static void run() throws BTSUserException, BTSSystemException {
		run(0, DEFAULT_WALLET_NAME);
	}
	public static void run(String name) throws BTSUserException, BTSSystemException {
		run(0, name);
	}
	public static void run(int id, String name) throws BTSUserException, BTSSystemException {
		WalletOpen i = (WalletOpen) new WalletOpen().doRun(id, COMMAND,
				new Object[] { name });
		i.checkUserException(new int[]{20004});
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test(DEFAULT_WALLET_NAME);
		try {
			run();
			System.out.println("SUCCESS");
		} catch (BTSUserException e) {
			System.out.println(e.getError().getMessage());
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":null}";
	}
}
