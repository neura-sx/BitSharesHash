package sx.neura.bts.json.api.wallet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.MarketOrder;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletMarketOrderList extends CommandJson {
	
	public static class Result extends ArrayList<Object> {
		private String id;
		private MarketOrder order;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public MarketOrder getOrder() {
			return order;
		}
		public void setOrder(MarketOrder order) {
			this.order = order;
		}
		
		private static final long serialVersionUID = 1L;
		public Result convert() {
			ObjectMapper mapper = new ObjectMapper();
			for (Object obj : this) {
				if (obj instanceof String)
					id = (String) obj;
				else if (obj instanceof LinkedHashMap)
					order = mapper.convertValue(obj, MarketOrder.class);
			}
			return this;
		}
	}
	
	private List<Result> result;

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public List<Result> getResult() {
		for (Result r : result)
			r.convert();
		return result;
	}
	
	private static final String COMMAND = "wallet_market_order_list";
	
	public static CommandJson test(String quoteAssetSymbol, String baseAssetSymbol, int limit, String accountName) {
		return doTest(COMMAND, new Object[] { quoteAssetSymbol, baseAssetSymbol, limit, accountName });
	}
	
	public static List<WalletMarketOrderList.Result> run(String quoteAssetSymbol, String baseAssetSymbol, int limit, String accountName) throws BTSSystemException {
		return run(0, quoteAssetSymbol, baseAssetSymbol, limit, accountName);
	}
	public static List<WalletMarketOrderList.Result> run(int id, String quoteAssetSymbol, String baseAssetSymbol, int limit, String accountName) throws BTSSystemException {
		WalletMarketOrderList i = (WalletMarketOrderList) new WalletMarketOrderList().doRun(
				id, COMMAND, new Object[] { quoteAssetSymbol, baseAssetSymbol, limit, accountName });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
		test("USD", "BTS", -1, "");
//		try {
//			List<WalletMarketOrderList.Result> list = run("USD", "BTS", -1, "gringox");
//			for (WalletMarketOrderList.Result result : list) {
//				System.out.println(String.format("[%s] [%s]: %12.8f %s %d %s",
//						result.getId(),
//						result.getOrder().getType(),
//						result.getOrder().getMarket_index().getOrder_price().getRatio(),
//						result.getOrder().getMarket_index().getOwner(),
//						result.getOrder().getState().getBalance(),
//						result.getOrder().getCollateral()));
//			}
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[[\"d5aa495dc8a7d016aebae75d4596bf855ca9b897\",{\"type\":\"cover_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.000520937903617256\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSLm9JRbNrJdd4B38TbPHLTHYgEAZgLEife\"},\"state\":{\"balance\":3386,\"limit_price\":null,\"last_update\":\"1970-01-01T00:00:00\"},\"collateral\":9749723,\"interest_rate\":{\"ratio\":\"0.12\",\"quote_asset_id\":22,\"base_asset_id\":0},\"expiration\":\"2015-03-04T19:23:10\"}]]}";
	}
}
