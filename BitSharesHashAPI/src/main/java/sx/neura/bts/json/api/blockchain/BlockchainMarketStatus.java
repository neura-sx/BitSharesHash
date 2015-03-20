package sx.neura.bts.json.api.blockchain;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainMarketStatus extends CommandJson {

	private Market result;

	public void setResult(Market result) {
		this.result = result;
	}

	public Market getResult() {
		return result;
	}
	
	private static final String COMMAND = "blockchain_market_status";
	
	public static CommandJson test(String quoteAssetSymbol, String baseAssetSymbol) {
		return doTest(COMMAND, new Object[] { quoteAssetSymbol, baseAssetSymbol });
	}
	
	public static Market run(String quoteAssetSymbol, String baseAssetSymbol) throws BTSSystemException {
		return run(0, quoteAssetSymbol, baseAssetSymbol);
	}
	public static Market run(int id, String quoteAssetSymbol, String baseAssetSymbol) throws BTSSystemException {
		BlockchainMarketStatus i = (BlockchainMarketStatus) new BlockchainMarketStatus().doRun(id, COMMAND, 
				new Object[] { quoteAssetSymbol, baseAssetSymbol });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
		test("USD", "BTS");
//		try {
//			Market result = run("USD", "BTS");
//			System.out.println(String.format("%12.8f %12.8f %d %d %12.8f %s",
//					result.getCurrent_feed_price(),
//					result.getLast_valid_feed_price(),
//					result.getBid_depth(),
//					result.getAsk_depth(),
//					result.getCenter_price().getRatio(),
//					result.getLast_error() != null ? result.getLast_error().getMessage() : ""));
//			
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"quote_id\":22,\"base_id\":0,\"current_feed_price\":0.01,\"last_valid_feed_price\":0.01,\"last_error\":null,\"ask_depth\":3849747317129,\"bid_depth\":2141321567572,\"center_price\":{\"ratio\":\"0.001\",\"quote_asset_id\":22,\"base_asset_id\":0}}}";
	}
}
