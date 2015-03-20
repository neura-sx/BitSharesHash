package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletAddContactAccount extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_add_contact_account";
	
	public static CommandJson test(String name, String publicKey) {
		return doTest(COMMAND, new Object[] { name, publicKey });
	}
	
	public static void run(String name, String publicKey) throws BTSSystemException {
		run(0, name, publicKey);
	}
	public static void run(int id, String name, String publicKey) throws BTSSystemException {
		WalletAddContactAccount i = (WalletAddContactAccount) new WalletAddContactAccount().doRun(id, COMMAND,
				new Object[] { name, publicKey });
		i.checkSystemException();
	}

	public static void main(String[] args) {
//		test("temp-1261143848", "BTSX7p9rHkT8TZ9NfQaRgbaAeVLZFqW1E33r62PPayU5ezhDxceEbW");
		try {
			run("temp-1261143848", "BTSX7p9rHkT8TZ9NfQaRgbaAeVLZFqW1E33r62PPayU5ezhDxceEbW");
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
