package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.enumerations.BurnMethod;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public class WalletBurn extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_burn";
	
	public static CommandJson test(double amountToBurn, String assetSymbol, String fromAccountName, String toAccountName, String publicMessage, BurnMethod burnMethod, boolean anonymous) {
		return doTest(COMMAND, new Object[] { amountToBurn, assetSymbol, fromAccountName, burnMethod.toString(), toAccountName, publicMessage, anonymous});
	}
	
	public static void run(double amountToBurn, String assetSymbol, String fromAccountName, String toAccountName, String publicMessage, BurnMethod burnMethod, boolean anonymous) throws BTSUserException, BTSSystemException {
		run(0, amountToBurn, assetSymbol, fromAccountName, toAccountName, publicMessage, burnMethod, anonymous);
	}
	public static void run(int id, double amountToBurn, String assetSymbol, String fromAccountName, String toAccountName, String publicMessage, BurnMethod burnMethod, boolean anonymous) throws BTSUserException, BTSSystemException {
		WalletBurn i = (WalletBurn) new WalletBurn().doRun(id, COMMAND, 
				new Object[] { amountToBurn, assetSymbol, fromAccountName, burnMethod.toString(), toAccountName, publicMessage, anonymous });
		i.checkUserException(new int[]{36005, 20010});
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test(1.2, "BTS", "gringoy", "senna", "burning", BurnMethod.FOR, false);
//		try {
//			run(1.1, "BTS", "gringoy", "senna", "intercepting", VoteMethod.RANDOM);
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
