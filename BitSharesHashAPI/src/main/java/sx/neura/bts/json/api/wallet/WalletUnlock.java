package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public class WalletUnlock extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_unlock";
	
	public static CommandJson test(int timeout, String password) {
		return doTest(COMMAND, new Object[] { timeout, password });
	}
	
	public static void run(int timeout, String password) throws BTSUserException, BTSSystemException {
		run(0, timeout, password);
	}
	public static void run(int id, int timeout, String password) throws BTSUserException, BTSSystemException {
		WalletUnlock i = (WalletUnlock) new WalletUnlock().doRun(id, COMMAND, 
				new Object[] { timeout, password });
		i.checkUserException(new int[]{20001, 20015});
		i.checkSystemException();
	}

	public static void main(String[] args) {
		//test(10000, DEFAULT_WALLET_PASSPHRASE);
		try {
			run(10000, DEFAULT_WALLET_PASSPHRASE);
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
