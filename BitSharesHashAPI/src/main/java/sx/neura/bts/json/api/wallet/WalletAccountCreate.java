package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletAccountCreate extends CommandJson {

	private String result;
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_create_account";
	
	public static CommandJson test(String name) {
		return doTest(COMMAND, new Object[] { name });
	}
	
	public static String run(String name) throws BTSSystemException {
		return run(0, name);
	}
	public static String run(int id, String name) throws BTSSystemException {
		WalletAccountCreate i = (WalletAccountCreate) new WalletAccountCreate().doRun(id, COMMAND,
				new Object[] { name });
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
//		test("parnaiba");
		try {
			System.out.println(run("parnaiba3"));
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":\"BTSX5FLLgC8z53MNEN75KcJSBcEGq6WZTdo2qqaef9EciVB6ucfHUr\"}";
	}
}
