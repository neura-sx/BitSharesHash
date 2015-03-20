package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletGetTransactionFee extends CommandJson {

	private Amount result;

	public void setResult(Amount result) {
		this.result = result;
	}

	public Amount getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_get_transaction_fee";
	
	public static CommandJson test(String assetSymbol) {
		return doTest(COMMAND, new Object[] { assetSymbol });
	}
	
	public static Amount run(String assetSymbol) throws BTSSystemException {
		return run(0, assetSymbol);
	}
	public static Amount run(int id, String assetSymbol) throws BTSSystemException {
		WalletGetTransactionFee i = (WalletGetTransactionFee) new WalletGetTransactionFee().doRun(id, COMMAND, 
				new Object[] { assetSymbol });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("USD");
		try {
			Amount result = run("BTSX");
			System.out.println(String.format("%d", result.getValue()));
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"amount\":72,\"asset_id\":22}}";
	}
}
