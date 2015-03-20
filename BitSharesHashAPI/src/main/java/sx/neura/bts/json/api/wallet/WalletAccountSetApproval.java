package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletAccountSetApproval extends CommandJson {

	private Integer result;

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_account_set_approval";
	
	public static CommandJson test(String accountName, int approval) {
		return doTest(COMMAND, new Object[] { accountName, approval });
	}
	
	public static Integer run(String accountName, int approval) throws BTSSystemException {
		return run(0, accountName, approval);
	}
	public static Integer run(int id, String accountName, int approval) throws BTSSystemException {
		WalletAccountSetApproval i = (WalletAccountSetApproval) new WalletAccountSetApproval().doRun(id, COMMAND, 
				new Object[] { accountName, approval });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("fox", 1);
		try {
			Integer result = run("fox", 1);
			System.out.println(result);
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":1}";
	}
}
