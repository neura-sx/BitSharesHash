package sx.neura.bts.json.api.blockchain;

import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.PublicData;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainListAccounts extends CommandJson {

	private List<RegisteredName> results;

	public void setResult(List<RegisteredName> results) {
		this.results = results;
	}
	
	public List<RegisteredName> getResult() {
		return results;
	}
	
	private static final String COMMAND = "blockchain_list_accounts";
	
	public static CommandJson test(String firstSymbol, int limit) {
		return doTest(COMMAND, new Object[] { firstSymbol, limit });
	}
	
	public static List<RegisteredName> run(String firstSymbol, int limit) throws BTSSystemException {
		return run(0, firstSymbol, limit);
	}
	public static List<RegisteredName> run(int id, String firstSymbol, int limit) throws BTSSystemException {
		BlockchainListAccounts i = (BlockchainListAccounts) new BlockchainListAccounts().doRun(
				id, COMMAND, new Object[] { firstSymbol, limit });
//				new Filter() {
//					@Override
//					public String filter(String json) {
//						return json.replaceAll("\\{\\s*\\}", "\"\"");
//					}
//				});
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("m", 873);
		try {
			List<RegisteredName> list = run("m", 873);
			for (RegisteredName name : list) {
				System.out.println(String.format("[%8d]: %40s %s %s",
						name.getId(),
						name.getName(),
						name.getRegistration_date(),
						(name.getPublic_data() != null && name.getPublic_data() instanceof PublicData ? ((PublicData) name.getPublic_data()).getVersion() : "")));
			}
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[{\"id\":298,\"name\":\"n\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX8fLWegcoL7ZRXf24UEkfqwCazbJ5vC5g4iVZWqMrVWh6dNX8ij\",\"active_key_history\":[[\"20140719T032920\",\"BTSX8fLWegcoL7ZRXf24UEkfqwCazbJ5vC5g4iVZWqMrVWh6dNX8ij\"]],\"registration_date\":\"20140719T032920\",\"last_update\":\"20140719T032920\",\"delegate_info\":null,\"meta_data\":null},{\"id\":19430,\"name\":\"n-a-s\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX5j2FnKCba8NXbdn6JcdDBtBKRP4RmkWtC1WX855r5cDi9M2AYY\",\"active_key_history\":[[\"20140811T165640\",\"BTSX5j2FnKCba8NXbdn6JcdDBtBKRP4RmkWtC1WX855r5cDi9M2AYY\"]],\"registration_date\":\"20140811T165640\",\"last_update\":\"20140811T165640\",\"delegate_info\":null,\"meta_data\":null},{\"id\":18079,\"name\":\"n-f-l\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX8SdVYDDQVwyx9QXTaEq44bkSsNxkEkwnYQwWHDxQ9VReMuiAk8\",\"active_key_history\":[[\"20140807T031620\",\"BTSX8SdVYDDQVwyx9QXTaEq44bkSsNxkEkwnYQwWHDxQ9VReMuiAk8\"]],\"registration_date\":\"20140807T031620\",\"last_update\":\"20140807T031620\",\"delegate_info\":null,\"meta_data\":null},{\"id\":16506,\"name\":\"n-i-g-a\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX8ZTvMeU2473m5JGp7jQxB1XVyRDRYrE5zBwW9aSEKnSBvPcEZF\",\"active_key_history\":[[\"20140804T073020\",\"BTSX8ZTvMeU2473m5JGp7jQxB1XVyRDRYrE5zBwW9aSEKnSBvPcEZF\"]],\"registration_date\":\"20140804T073020\",\"last_update\":\"20140804T073020\",\"delegate_info\":null,\"meta_data\":null},{\"id\":15667,\"name\":\"n-i-g-g-a\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX8M3FWYi5BTc3kZ5J2Pxpue2NsmFUkjq5LQz6u4VVRSyXRcK4he\",\"active_key_history\":[[\"20140801T194450\",\"BTSX8M3FWYi5BTc3kZ5J2Pxpue2NsmFUkjq5LQz6u4VVRSyXRcK4he\"]],\"registration_date\":\"20140801T194450\",\"last_update\":\"20140801T194450\",\"delegate_info\":null,\"meta_data\":null},{\"id\":10672,\"name\":\"n-o\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX5fREzBec7rf1v4mPMbZF28JeYR3vxvLjtK9gR8g2HiyHHc1zGK\",\"active_key_history\":[[\"20140724T015710\",\"BTSX5fREzBec7rf1v4mPMbZF28JeYR3vxvLjtK9gR8g2HiyHHc1zGK\"]],\"registration_date\":\"20140724T015710\",\"last_update\":\"20140724T015710\",\"delegate_info\":null,\"meta_data\":null},{\"id\":18072,\"name\":\"n-s-a\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX7yyWjcRwGYC6S3z5ecvJcZgCt7hv6rU6zUNhi6BEkNq134jC4M\",\"active_key_history\":[[\"20140807T030320\",\"BTSX7yyWjcRwGYC6S3z5ecvJcZgCt7hv6rU6zUNhi6BEkNq134jC4M\"]],\"registration_date\":\"20140807T030320\",\"last_update\":\"20140807T030320\",\"delegate_info\":null,\"meta_data\":null},{\"id\":19520,\"name\":\"n-w-a\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX6qLJf1NZJ6anQJzKXJ6mJZ84UYoVStjHfAXB3xTVQqUvfPpWDL\",\"active_key_history\":[[\"20140811T180440\",\"BTSX6qLJf1NZJ6anQJzKXJ6mJZ84UYoVStjHfAXB3xTVQqUvfPpWDL\"]],\"registration_date\":\"20140811T180440\",\"last_update\":\"20140811T180440\",\"delegate_info\":null,\"meta_data\":null},{\"id\":21533,\"name\":\"n-w-o\",\"public_data\":{\"gui_data\":{\"website\":\"\"}},\"owner_key\":\"BTSX7rYi6sjuVuPeb3BT97FZeBDvj3WKa5AJwHVqFUceb3wwPx6kXP\",\"active_key_history\":[[\"20140822T150440\",\"BTSX7rYi6sjuVuPeb3BT97FZeBDvj3WKa5AJwHVqFUceb3wwPx6kXP\"]],\"registration_date\":\"20140822T150440\",\"last_update\":\"20140822T150440\",\"delegate_info\":null,\"meta_data\":null},{\"id\":15613,\"name\":\"n-word\",\"public_data\":{\"gravatarID\":\"\"},\"owner_key\":\"BTSX6jLieWBHuadswqDbSRFmBrB3JxkmistzDFiPUWdcBiKqUkWAom\",\"active_key_history\":[[\"20140801T182830\",\"BTSX6jLieWBHuadswqDbSRFmBrB3JxkmistzDFiPUWdcBiKqUkWAom\"]],\"registration_date\":\"20140801T182830\",\"last_update\":\"20140801T182830\",\"delegate_info\":null,\"meta_data\":null}]}";
	}
}
