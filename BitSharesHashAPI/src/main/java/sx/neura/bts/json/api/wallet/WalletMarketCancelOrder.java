package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletMarketCancelOrder extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_market_cancel_order";
	
	public static CommandJson test(String orderId) {
		return doTest(COMMAND, new Object[] { orderId });
	}
	
	public static void run(String orderId) throws BTSSystemException {
		run(0, orderId);
	}
	public static void run(int id, String orderId) throws BTSSystemException {
		WalletMarketCancelOrder i = (WalletMarketCancelOrder) new WalletMarketCancelOrder().doRun(id, COMMAND, 
				new Object[] { orderId });
		i.checkSystemException();
	}

	public static void main(String[] args) {
//		test("4c2ca85b869054e3cb8680d14722f7f9369883cb");
		try {
			run("4c2ca85b869054e3cb8680d14722f7f9369883cb");
			System.out.println("SUCCESS");
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "";
	}
}
