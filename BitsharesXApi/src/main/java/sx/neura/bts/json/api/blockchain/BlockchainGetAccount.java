package sx.neura.bts.json.api.blockchain;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetAccount extends CommandJson {
	
	private Account result;

	public void setResult(Account result) {
		this.result = result;
	}

	public Account getResult() {
		return result;
	}
	
	private static final String COMMAND = "blockchain_get_account";
	
	public static CommandJson test(String name) {
		return doTest(COMMAND, new Object[] { name });
	}
	
	public static Account run(String name) throws BTSSystemException {
		return run(0, name);
	}
	public static Account run(int id, String name) throws BTSSystemException {
		BlockchainGetAccount i = (BlockchainGetAccount) new BlockchainGetAccount().doRun(id, COMMAND, 
				new Object[] { name });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
		test("zajac");
//		try {
//			Account account = run("299");
//			System.out.println(String.format("%30s: %s", account.getName(), account.getRegistration_date()));
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"id\":28609,\"name\":\"zajac\",\"public_data\":{\"gui_data\":{\"website\":\"\"}},\"owner_key\":\"BTS8Umho5xjXb3axLvwrs3MP3ycPc5bhvj1zvcPkQF9GK8pFMJJb9\",\"active_key_history\":[[\"2014-09-20T10:07:20\",\"BTS8Umho5xjXb3axLvwrs3MP3ycPc5bhvj1zvcPkQF9GK8pFMJJb9\"]],\"registration_date\":\"2014-09-20T10:07:20\",\"last_update\":\"2014-09-20T10:07:20\",\"delegate_info\":null,\"meta_data\":null}}";
	}
}
