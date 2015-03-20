package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public class WalletMarketCover extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}
	/**
		from_account_name (account_name, required): the account that will provide funds for the ask
		quantity (string, required): the quantity of asset you would like to cover
		quantity_symbol (asset_symbol, required): the type of asset you are covering (ie: USD)
		cover_id (order_id, required): the order ID you would like to cover
	*/
	private static final String COMMAND = "wallet_market_cover";
	
	public static CommandJson test(String fromAccountName, String quantity, String quantityAssetSymbol, String orderId) {
		return doTest(COMMAND, new Object[] { fromAccountName, quantity, quantityAssetSymbol, orderId });
	}
	
	public static void run(String fromAccountName, String quantity, String quantityAssetSymbol, String orderId) throws BTSUserException, BTSSystemException {
		run(0, fromAccountName, quantity, quantityAssetSymbol, orderId);
	}
	public static void run(int id, String fromAccountName, String quantity, String quantityAssetSymbol, String orderId) throws BTSUserException, BTSSystemException {
		WalletMarketCover i = (WalletMarketCover) new WalletMarketCover().doRun(id, COMMAND, 
				new Object[] { fromAccountName, quantity, quantityAssetSymbol, orderId });
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test("gringox", "1.12", "USD", "xxx");
//		try {
//			run("gringox", "1.12", "USD", "xxx");
//			System.out.println("SUCCESS");
//		} catch (BTSUserException e) {
//			System.out.println(e.getError().getMessage());
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "";
	}
}
