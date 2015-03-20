package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public class WalletMarketSubmitShort extends CommandJson {

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
		short_collateral (string, required): the amount of collateral you wish to fund this short with
		collateral_symbol (asset_symbol, required): the type of asset collateralizing this short (i.e. XTS)
		interest_rate (string, required): the APR you wish to pay interest at (0.0% to 1000.0%)
		quote_symbol (asset_symbol, required): the asset to short sell (i.e. USD)
		short_price_limit (string, optional, defaults to 0): maximim price (USD per XTS) that the short will execute at, if 0 then no limit will be applied
		{"jsonrpc":"2.0","id":1,"method":"wallet_market_submit_short","params":["gringox",243.12674,"BTSX",5.89,"USD",0]}
	*/
	private static final String COMMAND = "wallet_market_submit_short";
	
	public static CommandJson test(String accountName, String collateral, String collateralAssetSymbol, String interestRate, String quoteAssetSymbol, String priceLimit) {
		return doTest(COMMAND, new Object[] { accountName, collateral, collateralAssetSymbol, interestRate, quoteAssetSymbol, priceLimit });
	}
	
	public static void run(String accountName, String collateral, String collateralAssetSymbol, String interestRate, String quoteAssetSymbol, String priceLimit) throws BTSUserException, BTSSystemException {
		run(0, accountName, collateral, collateralAssetSymbol, interestRate, quoteAssetSymbol, priceLimit);
	}
	public static void run(int id, String accountName, String collateral, String collateralAssetSymbol, String interestRate, String quoteAssetSymbol, String priceLimit) throws BTSUserException, BTSSystemException {
		WalletMarketSubmitShort i = (WalletMarketSubmitShort) new WalletMarketSubmitShort().doRun(id, COMMAND, 
				new Object[] { accountName, collateral, collateralAssetSymbol, interestRate, quoteAssetSymbol, priceLimit });
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test("gringox", "38.1194", "BTS", "12.99", "USD", "0");
//		try {
//			run("gringox", "38.1194", "BTS", "12.99", "USD", "0");
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
