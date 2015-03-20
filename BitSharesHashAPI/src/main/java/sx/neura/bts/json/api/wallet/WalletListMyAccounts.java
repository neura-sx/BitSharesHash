package sx.neura.bts.json.api.wallet;

import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletListMyAccounts extends CommandJson {

	private List<Account> results;
	
	public void setResult(List<Account> results) {
		this.results = results;
	}
	
	public List<Account> getResult() {
		return results;
	}
	
	private static final String COMMAND = "wallet_list_my_accounts";
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	
	public static List<Account> run() throws BTSSystemException {
		return run(0);
	}
	public static List<Account> run(int id) throws BTSSystemException {
		WalletListMyAccounts i = (WalletListMyAccounts) new WalletListMyAccounts().doRun(
				id, COMMAND);
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
		//test();
		try {
			List<Account> list = run();
			for (Account result : list) {
				System.out.println(result.getName());
			}
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[{\"index\":10,\"id\":24106,\"name\":\"gringox\",\"public_data\":null,\"owner_key\":\"BTSX5hNx7Cn2cGFHaHTyQTGLjJDhSPsnqQPG5TuAmRoNns7X6WTrDX\",\"active_key_history\":[[\"20140902T164620\",\"BTSX5hNx7Cn2cGFHaHTyQTGLjJDhSPsnqQPG5TuAmRoNns7X6WTrDX\"]],\"registration_date\":\"20140902T164620\",\"last_update\":\"20140902T164620\",\"delegate_info\":{\"votes_for\":23236264389071,\"pay_rate\":3,\"signing_key_history\":[[5926,\"BTS4u9SXAmzTWjioNUHWGkuqYUHBcHhH5YWtZb5PoxQrryLjZsvZJ\"]],\"last_block_num_produced\":1558119,\"next_secret_hash\":\"52ff427fafa1ccf121522fc9fd9385ca1336c5df\",\"pay_balance\":551158169,\"total_paid\":2365458169,\"total_burned\":76124016,\"blocks_produced\":15302,\"blocks_missed\":191},\"meta_data\":null,\"account_address\":\"BTSX6CKZsQ9in4tvS8BwQvT7AFsQvyHBR6iXs\",\"private_data\":null,\"is_my_account\":true,\"approved\":0,\"is_favorite\":false,\"block_production_enabled\":false,\"last_used_gen_sequence\":10000},{\"index\":30,\"id\":24251,\"name\":\"gringoy\",\"public_data\":{\"gui_data\":{\"website\":\"\"}},\"owner_key\":\"BTSX6j9JntK13FX4KYnoj3YX7gzKudxmA7s7r8C8nsXKgX1FSwnEKc\",\"active_key_history\":[[\"20140904T160350\",\"BTSX6j9JntK13FX4KYnoj3YX7gzKudxmA7s7r8C8nsXKgX1FSwnEKc\"]],\"registration_date\":\"20140904T160350\",\"last_update\":\"20140904T160350\",\"delegate_info\":null,\"meta_data\":null,\"account_address\":\"BTSXCob7XtE4m7VrgMGCYQuRzM3aGbmcMiALo\",\"private_data\":{\"gui_data\":{}},\"is_my_account\":true,\"approved\":0,\"is_favorite\":false,\"block_production_enabled\":false,\"last_used_gen_sequence\":10000},{\"index\":57,\"id\":0,\"name\":\"umma\",\"public_data\":null,\"owner_key\":\"BTSX6QWvdmxThbFnmrCwYpefH9QEJNrmawVhEu2zAEk4T1yeK498kp\",\"active_key_history\":[[\"20140924T160113\",\"BTSX6QWvdmxThbFnmrCwYpefH9QEJNrmawVhEu2zAEk4T1yeK498kp\"]],\"registration_date\":\"19700101T000000\",\"last_update\":\"19700101T000000\",\"delegate_info\":null,\"meta_data\":null,\"account_address\":\"BTSXGmfLKmQRtQa9P43r18Uwvk1oLL1jdL9Bf\",\"private_data\":{\"gui_data\":{}},\"is_my_account\":true,\"approved\":0,\"is_favorite\":false,\"block_production_enabled\":false,\"last_used_gen_sequence\":10000}]}";
	}
}
