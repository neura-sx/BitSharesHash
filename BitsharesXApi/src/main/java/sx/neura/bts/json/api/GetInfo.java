package sx.neura.bts.json.api;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class GetInfo extends CommandJson {

	public static class Result {
		private int blockchain_head_block_num;
		private int blockchain_head_block_age;
		private String blockchain_head_block_timestamp;
		private int blockchain_average_delegate_participation;
		private int blockchain_confirmation_requirement;
		private long blockchain_share_supply;
		private int blockchain_blocks_left_in_round;
		private int blockchain_next_round_time;
		private String blockchain_next_round_timestamp;
		private String blockchain_random_seed;
		private String client_data_dir;
		private String client_version;
		private int network_num_connections;
		private int network_num_connections_max;
		private boolean network_chain_downloader_running;
		private String network_chain_downloader_blocks_remaining;
		private String ntp_time;
		private double ntp_time_error;
		private boolean wallet_open;
		private boolean wallet_unlocked;
		private String wallet_unlocked_until;
		private String wallet_unlocked_until_timestamp;
		private String wallet_last_scanned_block_timestamp;
		private String wallet_scan_progress;
		private String wallet_block_production_enabled;
		private String wallet_next_block_production_time;
		private String wallet_next_block_production_timestamp;
		public int getBlockchain_head_block_num() {
			return blockchain_head_block_num;
		}
		public void setBlockchain_head_block_num(int blockchain_head_block_num) {
			this.blockchain_head_block_num = blockchain_head_block_num;
		}
		public int getBlockchain_head_block_age() {
			return blockchain_head_block_age;
		}
		public void setBlockchain_head_block_age(int blockchain_head_block_age) {
			this.blockchain_head_block_age = blockchain_head_block_age;
		}
		public String getBlockchain_head_block_timestamp() {
			return blockchain_head_block_timestamp;
		}
		public void setBlockchain_head_block_timestamp(
				String blockchain_head_block_timestamp) {
			this.blockchain_head_block_timestamp = blockchain_head_block_timestamp;
		}
		public int getBlockchain_average_delegate_participation() {
			return blockchain_average_delegate_participation;
		}
		public void setBlockchain_average_delegate_participation(
				int blockchain_average_delegate_participation) {
			this.blockchain_average_delegate_participation = blockchain_average_delegate_participation;
		}
		public int getBlockchain_confirmation_requirement() {
			return blockchain_confirmation_requirement;
		}
		public void setBlockchain_confirmation_requirement(
				int blockchain_confirmation_requirement) {
			this.blockchain_confirmation_requirement = blockchain_confirmation_requirement;
		}
		public long getBlockchain_share_supply() {
			return blockchain_share_supply;
		}
		public void setBlockchain_share_supply(long blockchain_share_supply) {
			this.blockchain_share_supply = blockchain_share_supply;
		}
		public int getBlockchain_blocks_left_in_round() {
			return blockchain_blocks_left_in_round;
		}
		public void setBlockchain_blocks_left_in_round(
				int blockchain_blocks_left_in_round) {
			this.blockchain_blocks_left_in_round = blockchain_blocks_left_in_round;
		}
		public int getBlockchain_next_round_time() {
			return blockchain_next_round_time;
		}
		public void setBlockchain_next_round_time(int blockchain_next_round_time) {
			this.blockchain_next_round_time = blockchain_next_round_time;
		}
		public String getBlockchain_next_round_timestamp() {
			return blockchain_next_round_timestamp;
		}
		public void setBlockchain_next_round_timestamp(
				String blockchain_next_round_timestamp) {
			this.blockchain_next_round_timestamp = blockchain_next_round_timestamp;
		}
		public String getBlockchain_random_seed() {
			return blockchain_random_seed;
		}
		public void setBlockchain_random_seed(String blockchain_random_seed) {
			this.blockchain_random_seed = blockchain_random_seed;
		}
		public String getClient_data_dir() {
			return client_data_dir;
		}
		public void setClient_data_dir(String client_data_dir) {
			this.client_data_dir = client_data_dir;
		}
		public String getClient_version() {
			return client_version;
		}
		public void setClient_version(String client_version) {
			this.client_version = client_version;
		}
		public int getNetwork_num_connections() {
			return network_num_connections;
		}
		public void setNetwork_num_connections(int network_num_connections) {
			this.network_num_connections = network_num_connections;
		}
		public int getNetwork_num_connections_max() {
			return network_num_connections_max;
		}
		public void setNetwork_num_connections_max(int network_num_connections_max) {
			this.network_num_connections_max = network_num_connections_max;
		}
		public boolean isNetwork_chain_downloader_running() {
			return network_chain_downloader_running;
		}
		public void setNetwork_chain_downloader_running(
				boolean network_chain_downloader_running) {
			this.network_chain_downloader_running = network_chain_downloader_running;
		}
		public String getNetwork_chain_downloader_blocks_remaining() {
			return network_chain_downloader_blocks_remaining;
		}
		public void setNetwork_chain_downloader_blocks_remaining(
				String network_chain_downloader_blocks_remaining) {
			this.network_chain_downloader_blocks_remaining = network_chain_downloader_blocks_remaining;
		}
		public String getNtp_time() {
			return ntp_time;
		}
		public void setNtp_time(String ntp_time) {
			this.ntp_time = ntp_time;
		}
		public double getNtp_time_error() {
			return ntp_time_error;
		}
		public void setNtp_time_error(double ntp_time_error) {
			this.ntp_time_error = ntp_time_error;
		}
		public boolean isWallet_open() {
			return wallet_open;
		}
		public void setWallet_open(boolean wallet_open) {
			this.wallet_open = wallet_open;
		}
		public boolean isWallet_unlocked() {
			return wallet_unlocked;
		}
		public void setWallet_unlocked(boolean wallet_unlocked) {
			this.wallet_unlocked = wallet_unlocked;
		}
		public String getWallet_unlocked_until() {
			return wallet_unlocked_until;
		}
		public void setWallet_unlocked_until(String wallet_unlocked_until) {
			this.wallet_unlocked_until = wallet_unlocked_until;
		}
		public String getWallet_unlocked_until_timestamp() {
			return wallet_unlocked_until_timestamp;
		}
		public void setWallet_unlocked_until_timestamp(
				String wallet_unlocked_until_timestamp) {
			this.wallet_unlocked_until_timestamp = wallet_unlocked_until_timestamp;
		}
		public String getWallet_last_scanned_block_timestamp() {
			return wallet_last_scanned_block_timestamp;
		}
		public void setWallet_last_scanned_block_timestamp(
				String wallet_last_scanned_block_timestamp) {
			this.wallet_last_scanned_block_timestamp = wallet_last_scanned_block_timestamp;
		}
		public String getWallet_scan_progress() {
			return wallet_scan_progress;
		}
		public void setWallet_scan_progress(String wallet_scan_progress) {
			this.wallet_scan_progress = wallet_scan_progress;
		}
		public String getWallet_block_production_enabled() {
			return wallet_block_production_enabled;
		}
		public void setWallet_block_production_enabled(
				String wallet_block_production_enabled) {
			this.wallet_block_production_enabled = wallet_block_production_enabled;
		}
		public String getWallet_next_block_production_time() {
			return wallet_next_block_production_time;
		}
		public void setWallet_next_block_production_time(
				String wallet_next_block_production_time) {
			this.wallet_next_block_production_time = wallet_next_block_production_time;
		}
		public String getWallet_next_block_production_timestamp() {
			return wallet_next_block_production_timestamp;
		}
		public void setWallet_next_block_production_timestamp(
				String wallet_next_block_production_timestamp) {
			this.wallet_next_block_production_timestamp = wallet_next_block_production_timestamp;
		}
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "get_info";
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	
	public static GetInfo.Result run() throws BTSSystemException {
		return run(0);
	}
	public static GetInfo.Result run(int id) throws BTSSystemException {
		GetInfo i = (GetInfo) new GetInfo().doRun(id, COMMAND);
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
		test();
//		try {
//			GetInfo.Result result = GetInfo.run();
//			System.out.println(String.format("%d %s", result.getBlockchain_head_block_age(), result.getBlockchain_head_block_timestamp()));
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"blockchain_head_block_num\":1814897,\"blockchain_head_block_age\":8,\"blockchain_head_block_timestamp\":\"2015-02-17T18:40:20\",\"blockchain_average_delegate_participation\":100,\"blockchain_confirmation_requirement\":1,\"blockchain_share_supply\":250090804249702,\"blockchain_blocks_left_in_round\":73,\"blockchain_next_round_time\":722,\"blockchain_next_round_timestamp\":\"2015-02-17T18:52:30\",\"blockchain_random_seed\":\"7cf8a5fcf921eff5b0a7a44c0d2c36a7b7939f9a\",\"client_data_dir\":\"C:/Users/Robin/AppData/Roaming/BitShares\",\"client_version\":\"0.6.1\",\"network_num_connections\":47,\"network_num_connections_max\":200,\"network_chain_downloader_running\":false,\"network_chain_downloader_blocks_remaining\":null,\"ntp_time\":\"2015-02-17T18:40:28\",\"ntp_time_error\":-2.4950019999999999,\"wallet_open\":true,\"wallet_unlocked\":false,\"wallet_unlocked_until\":null,\"wallet_unlocked_until_timestamp\":null,\"wallet_last_scanned_block_timestamp\":null,\"wallet_scan_progress\":null,\"wallet_block_production_enabled\":null,\"wallet_next_block_production_time\":null,\"wallet_next_block_production_timestamp\":null}}";
	}
}
