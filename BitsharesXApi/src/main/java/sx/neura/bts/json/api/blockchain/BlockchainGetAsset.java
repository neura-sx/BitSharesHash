package sx.neura.bts.json.api.blockchain;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetAsset extends CommandJson {

	private Asset result;

	public void setResult(Asset result) {
		this.result = result;
	}

	public Asset getResult() {
		return result;
	}
	
	private static final String COMMAND = "blockchain_get_asset";
	
	public static CommandJson test(String assetSymbol) {
		return doTest(COMMAND, new Object[] { assetSymbol });
	}
	
	public static Asset run(String assetSymbol) throws BTSSystemException {
		return run(0, assetSymbol);
	}
	public static Asset run(int id, String assetSymbol) throws BTSSystemException {
		BlockchainGetAsset i = (BlockchainGetAsset) new BlockchainGetAsset().doRun(id, COMMAND, 
				new Object[] { assetSymbol });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("USD");
		try {
			Asset result = run("USD");
			System.out.println(String.format("%s %d %s", result.getDescription(), result.getPrecision(), result.getRegistration_date()));
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"id\":22,\"symbol\":\"USD\",\"name\":\"United States Dollar\",\"description\":\"1 United States dollar\",\"public_data\":\"\",\"issuer_account_id\":-2,\"precision\":10000,\"registration_date\":\"20140719T000000\",\"last_update\":\"20140719T000000\",\"current_share_supply\":3643649668,\"maximum_share_supply\":1000000000000000,\"collected_fees\":54499026}}";
	}
}
