package sx.neura.bts.json.api.blockchain;

import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.MarketOrder;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainMarketListShorts extends CommandJson {

	private List<MarketOrder> results;

	public void setResult(List<MarketOrder> results) {
		this.results = results;
	}
	
	public List<MarketOrder> getResult() {
		return results;
	}
	
	private static final String COMMAND = "blockchain_market_list_shorts";
	
	public static CommandJson test(String quoteAssetSymbol, int limit) {
		return doTest(COMMAND, new Object[] { quoteAssetSymbol, limit });
	}
	
	public static List<MarketOrder> run(String quoteAssetSymbol, int limit) throws BTSSystemException {
		return run(0, quoteAssetSymbol, limit);
	}
	public static List<MarketOrder> run(int id, String quoteAssetSymbol, int limit) throws BTSSystemException {
		BlockchainMarketListShorts i = (BlockchainMarketListShorts) new BlockchainMarketListShorts().doRun(
				id, COMMAND, new Object[] { quoteAssetSymbol, limit });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
		test("USD", 1);
//		try {
//			List<MarketOrder> list = run("USD", 10);
//			for (MarketOrder result : list) {
//				System.out.println(String.format("[%s]: %12.8f %d %d %d %12.8f",
//						result.getType(),
//						result.getMarket_index().getOrder_price().getRatio(),
//						result.getCollateral(),
//						result.getState().getBalance(),
//						(long) (result.getState().getBalance() * result.getMarket_index().getOrder_price().getRatio()),
//						result.getState().getLimit_price() != null ? result.getState().getLimit_price().getRatio() : 0));
//			}
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.031\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTS6PirVjZM99Tbd38PBTYpyudwxCmu7nER4\"},\"state\":{\"balance\":1000000,\"limit_price\":{\"ratio\":\"0.00008\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-22T09:13:30\"},\"collateral\":1000000,\"interest_rate\":{\"ratio\":\"0.031\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.03\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSBQ8TtH7qcNqDjK4JPx3nQorYJkPHwdcif\"},\"state\":{\"balance\":28747894675,\"limit_price\":{\"ratio\":\"0.00078125\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-17T22:33:20\"},\"collateral\":28747894675,\"interest_rate\":{\"ratio\":\"0.03\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.0001\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSPwqcfV6bhxPHYTJfhNZcYUNeEimkLMbWv\"},\"state\":{\"balance\":20000000000,\"limit_price\":{\"ratio\":\"0.0009090909091\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-25T13:22:10\"},\"collateral\":20000000000,\"interest_rate\":{\"ratio\":\"0.0001\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.0001\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSMeUqm7yqyTswr47ry4qWN8u6jH8vUrzkZ\"},\"state\":{\"balance\":20000000000,\"limit_price\":{\"ratio\":\"0.0008695652174\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-25T13:21:20\"},\"collateral\":20000000000,\"interest_rate\":{\"ratio\":\"0.0001\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSMPN1JzadAM1UBa4p42WnaXxD4PEzDsS6u\"},\"state\":{\"balance\":7464200000,\"limit_price\":{\"ratio\":\"0.0010101010101\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-26T10:35:00\"},\"collateral\":7464200000,\"interest_rate\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSCrXtpJi8B1cnCrRp9GhAYnofN9FcdKJyU\"},\"state\":{\"balance\":20000000000,\"limit_price\":{\"ratio\":\"0.00097\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-26T10:00:20\"},\"collateral\":20000000000,\"interest_rate\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSBZaBNcZ74tesgXoRhXEQP7QYYfB1jcoPR\"},\"state\":{\"balance\":4270280997,\"limit_price\":{\"ratio\":\"0.0008814\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-25T13:30:10\"},\"collateral\":4270280997,\"interest_rate\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTS81ZMDjr8BFz3S2aMvrVPGYFMg2DDaWmQ\"},\"state\":{\"balance\":4266185962,\"limit_price\":{\"ratio\":\"0.0008\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"2015-02-25T13:28:30\"},\"collateral\":4266185962,\"interest_rate\":{\"ratio\":\"0.\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":null}]}";
	}
}
