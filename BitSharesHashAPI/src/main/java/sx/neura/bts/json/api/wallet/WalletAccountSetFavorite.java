package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletAccountSetFavorite extends CommandJson {

	public static class Result {
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_account_set_favorite";
	
	public static CommandJson test(String accountName, boolean isFavorite) {
		return doTest(COMMAND, new Object[] { accountName, isFavorite });
	}
	
	public static void run(String accountName, boolean isFavorite) throws BTSSystemException {
		run(0, accountName, isFavorite);
	}
	public static void run(int id, String accountName, boolean isFavorite) throws BTSSystemException {
		WalletAccountSetFavorite i = (WalletAccountSetFavorite) new WalletAccountSetFavorite().doRun(id, COMMAND,
				new Object[] { accountName, isFavorite });
		i.checkSystemException();
	}

	public static void main(String[] args) {
//		test("bytemaster", true);
		try {
			run("bytemaster", true);
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
