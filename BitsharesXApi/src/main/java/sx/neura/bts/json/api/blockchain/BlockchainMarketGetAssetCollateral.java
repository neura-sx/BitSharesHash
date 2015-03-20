package sx.neura.bts.json.api.blockchain;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainMarketGetAssetCollateral extends CommandJson {

	private Long result;

	public void setResult(Long result) {
		this.result = result;
	}

	public Long getResult() {
		return result;
	}
	
	private static final String COMMAND = "blockchain_market_get_asset_collateral";
	
	public static CommandJson test(String assetSymbol) {
		return doTest(COMMAND, new Object[] { assetSymbol });
	}
	
	public static Long run(String assetSymbol) throws BTSSystemException {
		return run(0, assetSymbol);
	}
	public static Long run(int id, String assetSymbol) throws BTSSystemException {
		BlockchainMarketGetAssetCollateral i = (BlockchainMarketGetAssetCollateral) new BlockchainMarketGetAssetCollateral().doRun(id, COMMAND, 
				new Object[] { assetSymbol });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("USD");
		try {
			Long result = run("GLD");
			System.out.println(result);
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":3328237127701}";
	}
}
