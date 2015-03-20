package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public class WalletMarketSubmitBid extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_market_submit_bid";
	
	public static CommandJson test(String accountName, String buyQuantity, String buyAssetSymbol, String bidPrice, String bidAssetSymbol, boolean allowStupidBid) {
		return doTest(COMMAND, new Object[] { accountName, buyQuantity, buyAssetSymbol, bidPrice, bidAssetSymbol, allowStupidBid });
	}
	
	public static void run(String accountName, String buyQuantity, String buyAssetSymbol, String bidPrice, String bidAssetSymbol, boolean allowStupidBid) throws BTSUserException, BTSSystemException {
		run(0, accountName, buyQuantity, buyAssetSymbol, bidPrice, bidAssetSymbol, allowStupidBid);
	}
	public static void run(int id, String accountName, String buyQuantity, String buyAssetSymbol, String bidPrice, String bidAssetSymbol, boolean allowStupidBid) throws BTSUserException, BTSSystemException {
		WalletMarketSubmitBid i = (WalletMarketSubmitBid) new WalletMarketSubmitBid().doRun(id, COMMAND, 
				new Object[] { accountName, buyQuantity, buyAssetSymbol, bidPrice, bidAssetSymbol, allowStupidBid });
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test("gringox", "38.1194", "BTSX", "0.02021", "USD", false);
//		try {
//			run("gringox", "38.1194", "BTSX", "0.02021", "USD", false);
//			System.out.println("SUCCESS");
//		} catch (BTSUserException e) {
//			System.out.println(e.getError().getMessage());
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"index\":338,\"record_id\":\"5e0c2129872be9d8fa6f141b5e4a90697423db9c\",\"block_num\":0,\"is_virtual\":false,\"is_confirmed\":false,\"is_market\":true,\"trx\":{\"expiration\":\"20141029T033704\",\"delegate_slate_id\":null,\"operations\":[{\"type\":\"bid_op_type\",\"data\":{\"amount\":7703,\"bid_index\":{\"order_price\":{\"ratio\":\"0.002021\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSXZ6fYct1iexKwbsxM56KZosgt8A6gzHF2\"}}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSX8dm5gKEWbE1vuZGTdM4S3JioifDV4cSta\",\"amount\":10000,\"claim_input_data\":\"\"}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSXD1nMiaLu7P8mAi1bfkEiG2opQFUBtkohw\",\"amount\":527,\"claim_input_data\":\"\"}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSXDQAoJd7S6jWvMvfFqDfXTPZcnWZWrefb2\",\"amount\":4135,\"claim_input_data\":\"\"}},{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSXKVec7L8g52Y17mzr2VfURqzwM29fiaybb\",\"amount\":3041,\"claim_input_data\":\"\"}}],\"signatures\":[\"203f3c88c4b9c6545e0817790efb379e73e371669461d411729f6623f63e22ed4c84b4b6b6fe2ba0e88ae6ec922742c95b497c7ec934ad73a57a3278d05c446104\",\"208abd9dac1209d97958cee911337e499527b8ac1ff5b6f735e9509e5eb000a85f7ae6b277328a4e1ed0c1009a7652457929d3e6df88255409cb8c9cff6ddfa67e\",\"203e68a96fa8c2624516b7c1a9ffa5697a005e343d912614113f089da1ffd8cf3b2c04faea0e5a8dc03ce1150f28b7a7a72a128a61ac5664b3ac8cf25975e37bfb\",\"1f64cb046616e10b6178f0e9ea8ac660a0039d375167c4e93eed306386b273fb946edb9d698c44d4ee338ebf11494062e158f2afbfc375f8820d6d74ca2e3be000\",\"2091f2ae16d49e756049542d5be46dacc953c9620525815f89a1d77e82d48579e9bdb728fd24d9e4a8b7e257b1fe62c90bd179685336bab023a7459e9b6869afd0\"]},\"ledger_entries\":[{\"from_account\":\"BTSX5hNx7Cn2cGFHaHTyQTGLjJDhSPsnqQPG5TuAmRoNns7X6WTrDX\",\"to_account\":\"BTSX6pRsx8pvbCqQ6d9tJPFEyodCjeTDACEBCGCLWe19N573maEXpB\",\"amount\":{\"amount\":7703,\"asset_id\":22},\"memo\":\"buy BTSX @ 0.02021 USD / BTSX\",\"memo_from_account\":null}],\"fee\":{\"amount\":10000,\"asset_id\":0},\"created_time\":\"20141029T022944\",\"received_time\":\"20141029T022944\",\"extra_addresses\":[]}}";
	}
}
