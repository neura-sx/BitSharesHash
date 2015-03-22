package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletCreate extends CommandJson {

	public static class Result {
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_create";
	
	public static CommandJson test(String name, String passphrase) {
		return doTest(COMMAND, new Object[] { name, passphrase });
	}
	
	public static WalletCreate.Result run() throws BTSSystemException {
		return run(0, DEFAULT_WALLET_NAME, DEFAULT_WALLET_PASSPHRASE);
	}
	public static WalletCreate.Result run(String passphrase) throws BTSSystemException {
		return run(0, DEFAULT_WALLET_NAME, passphrase);
	}
	public static WalletCreate.Result run(String name, String passphrase) throws BTSSystemException {
		return run(0, name, passphrase);
	}
	public static WalletCreate.Result run(int id, String name, String passphrase) throws BTSSystemException {
		WalletCreate i = (WalletCreate) new WalletCreate().doRun(id, COMMAND,
				new Object[] { name, passphrase });
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
		test(DEFAULT_WALLET_NAME, DEFAULT_WALLET_PASSPHRASE);
//		try {
//			run();
//			System.out.println("SUCCESS");
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":null}";
	}
}
