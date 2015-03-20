package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletAccountRegister extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_account_register";
	
	public static CommandJson test(String name, String payFromAccountName) {
		return doTest(COMMAND, new Object[] { name, payFromAccountName });
	}
	
	public static void run(String name, String payFromAccountName) throws BTSSystemException {
		run(0, name, payFromAccountName);
	}
	public static void run(int id, String name, String payFromAccountName) throws BTSSystemException {
		WalletAccountRegister i = (WalletAccountRegister) new WalletAccountRegister().doRun(id, COMMAND,
				new Object[] { name, payFromAccountName });
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test("fellowtraveler", "gringox");
//		try {
//			run("fellowtraveler", "gringox");
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
