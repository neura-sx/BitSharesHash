package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public class WalletMarketSubmitAsk extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_market_submit_ask";
	
	public static CommandJson test(String accountName, String sellQuantity, String sellAssetSymbol, String askPrice, String askAssetSymbol, boolean allowStupidAsk) {
		return doTest(COMMAND, new Object[] { accountName, sellQuantity, sellAssetSymbol, askPrice, askAssetSymbol, allowStupidAsk });
	}
	
	public static void run(String accountName, String sellQuantity, String sellAssetSymbol, String askPrice, String askAssetSymbol, boolean allowStupidAsk) throws BTSUserException, BTSSystemException {
		run(0, accountName, sellQuantity, sellAssetSymbol, askPrice, askAssetSymbol, allowStupidAsk);
	}
	public static void run(int id, String accountName, String sellQuantity, String sellAssetSymbol, String askPrice, String askAssetSymbol, boolean allowStupidAsk) throws BTSUserException, BTSSystemException {
		WalletMarketSubmitAsk i = (WalletMarketSubmitAsk) new WalletMarketSubmitAsk().doRun(id, COMMAND, 
				new Object[] { accountName, sellQuantity, sellAssetSymbol, askPrice, askAssetSymbol, allowStupidAsk });
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test("gringox", "38.1194", "BTSX", "0.02421", "USD", false);
//		try {
//			run("gringox", "38.1194", "BTSX", "0.02421", "USD", false);
//			System.out.println("SUCCESS");
//		} catch (BTSUserException e) {
//			System.out.println(e.getError().getMessage());
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"index\":352,\"record_id\":\"d45b72b2a6d2112eb5656b9d21495a69a4bee11d\",\"block_num\":0,\"is_virtual\":false,\"is_confirmed\":false,\"is_market\":true,\"trx\":{\"expiration\":\"20141029T180723\",\"delegate_slate_id\":null,\"operations\":[{\"type\":\"ask_op_type\",\"data\":{\"amount\":3811940,\"ask_index\":{\"order_price\":{\"ratio\":\"0.002421\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSX7BKSAjRCzcBrqsUmLmKzPRNvPVH568e3v\"}}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSX8dm5gKEWbE1vuZGTdM4S3JioifDV4cSta\",\"amount\":2393,\"claim_input_data\":\"\"}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSXA8AQbRQ2mAfVXapL3NiVD2SshrXgtv6Bd\",\"amount\":1000000,\"claim_input_data\":\"\"}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSXFgiUA2yVGibTT6zn2HPao1PnqF9RFEMR7\",\"amount\":1990000,\"claim_input_data\":\"\"}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSXMhyy8nb33PF2jjfftpzYfB8zENMTWmCEf\",\"amount\":829547,\"claim_input_data\":\"\"}}],\"signatures\":[\"1f67caf1cac00e4910aad24fbf5427456e486af309e61543fc896afedc538dcf443a6072dfbcbb2b8c7ccfae9d87c3f2837faca9e3faf31c1bf0ca808476e5240b\",\"1fcb6b8128d351917aedc963d5912c7b540cd3a0ae690d079f3cfd3b6d47f612c2a804e6e49f08eef2990073f28bdc2c30e7389c7c3c7e122bbe25a7aafa258aa8\",\"1f56a894274d01bb99b6991ad6ff3bf5af6db35e7566d23a22b18a2a252a0b1c9d7acc3d66e1a350035b39420581a34796bfbbb001aaf29cd15a2972355445d6cd\",\"20ed814cf3f13addd2aac8fc26149e71e5df0a6b2efee7c538c79d04050a617df6bd6b6f185a1b8771dd5d6e54aafa69ec6f745ac4ac78d356715708d8461c2ef5\",\"1f67666b506dec0e0846714003109fab0c5e2ebf21177bee3524cbac423f4bb1bcea5c85639e9207c2dbef352f64f09c5f8ea454478e432dcbe01a27c2776de0bd\"]},\"ledger_entries\":[{\"from_account\":\"BTSX5hNx7Cn2cGFHaHTyQTGLjJDhSPsnqQPG5TuAmRoNns7X6WTrDX\",\"to_account\":\"BTSX5QjJbrd7zDNCAhm5w3G2triEdnNVbDsEvRNQvdqmrN8z1fvtqM\",\"amount\":{\"amount\":3811940,\"asset_id\":0},\"memo\":\"sell BTSX @ 0.02421 USD / BTSX\",\"memo_from_account\":null}],\"fee\":{\"amount\":10000,\"asset_id\":0},\"created_time\":\"20141029T170003\",\"received_time\":\"20141029T170003\",\"extra_addresses\":[]}}";
	}
}
