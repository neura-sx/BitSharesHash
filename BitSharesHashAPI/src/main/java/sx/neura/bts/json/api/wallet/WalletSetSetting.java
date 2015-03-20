package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletSetSetting extends CommandJson {

	public static class Result {
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_set_setting";
	
	public static CommandJson test(String name, String value) {
		return doTest(COMMAND, new Object[] { name, value });
	}
	
	public static WalletSetSetting.Result run(String name, String value) throws BTSSystemException {
		return run(0, name, value);
	}
	public static WalletSetSetting.Result run(int id, String name, String value) throws BTSSystemException {
		WalletSetSetting i = (WalletSetSetting) new WalletSetSetting().doRun(id, COMMAND,
				new Object[] { name, value });
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
		test("recent_markets", "[\"BTS:USD\"]");
		//test("recent_markets", "[\"USD:BTSX\",\"CNY:BTSX\"]");
//		try {
//			WalletSetSetting.Result result = run("recent_markets", "[\"BitUSD:BTSX\",\"BitCNY:BTSX\"]");
//			System.out.println(result.getValue());
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":null}";
	}
}
