package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletRemoveContactAccount extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_remove_contact_account";
	
	public static CommandJson test(String name) {
		return doTest(COMMAND, new Object[] { name });
	}
	
	public static void run(String name) throws BTSSystemException {
		run(0, name);
	}
	public static void run(int id, String name) throws BTSSystemException {
		WalletRemoveContactAccount i = (WalletRemoveContactAccount) new WalletRemoveContactAccount().doRun(id, COMMAND,
				new Object[] { name });
		i.checkSystemException();
	}

	public static void main(String[] args) {
//		test("temp-1261143848");
		try {
			run("temp-1261143848");
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
