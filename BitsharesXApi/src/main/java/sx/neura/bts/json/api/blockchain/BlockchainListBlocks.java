package sx.neura.bts.json.api.blockchain;

import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainListBlocks extends CommandJson {
	
	private List<Block> results;

	public void setResult(List<Block> results) {
		this.results = results;
	}
	
	public List<Block> getResult() {
		return results;
	}
	
	private static final String COMMAND = "blockchain_list_blocks";
	
	public static CommandJson test(int firstBlockNumber, int limit) {
		return doTest(COMMAND, new Object[] { firstBlockNumber, limit });
	}
	
	public static List<Block> run(int firstBlockNumber, int limit) throws BTSSystemException {
		return run(0, firstBlockNumber, limit);
	}
	public static List<Block> run(int id, int firstBlockNumber, int limit) throws BTSSystemException {
		BlockchainListBlocks i = (BlockchainListBlocks) new BlockchainListBlocks().doRun(id, COMMAND, 
				new Object[] { firstBlockNumber, limit });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
		test(-1, 8);
//		try {
//			List<Block> list = run(0, -1000);
//			for (Block block : list) {
//				System.out.println(String.format("[%8d]: %s %2d %4s %4d",
//						block.getBlock_num(),
//						block.getTimestamp(),
//						block.getUser_transaction_ids().size(),
//						String.format("%ds", block.getLatency() / 1000000),
//						block.getBlock_size()));
//			}
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[{\"previous\":\"2eb5497a0f79e69f9891fb3f494dfd574f3ab6c5\",\"block_num\":1557913,\"timestamp\":\"2015-01-18T16:05:50\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"b747648a7984f6b68072b418ee3af82e9165dc55\",\"previous_secret\":\"a773bee428f6bb1379e23eef758b82883b4bdc63\",\"delegate_signature\":\"2020d40699f25dad9a21bfb8707447d59f6b7ff0d414f46a8a4af0a22fa6e45c004853866ab500c14bb172b6eeba4ed63ab9e2a7c4790d92e4eb06f20d77d7bf0f\",\"user_transaction_ids\":[\"1fe5bec5062fc62d9134717f05effde0fddeed4d\",\"2e92985d0145270db1681a83d719e2f77e3cfbe2\"],\"signee_shares_issued\":150000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":20000,\"random_seed\":\"6f8fa82a960a8ba77d3f6ed3cfe341cdf8699769\",\"block_size\":1579,\"latency\":1398000000,\"processing_time\":7000},{\"previous\":\"3eb0a120b56c107df12d3aa870cc0db22fd2c1fc\",\"block_num\":1557912,\"timestamp\":\"2015-01-18T16:05:40\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"524d6ef69b715a11a098630c29c18d312c106974\",\"previous_secret\":\"f6fd3de1cd4f32d6cd952be6497cd9cdf16eac24\",\"delegate_signature\":\"2041698b60552bb65e16f98dac1c1361adee4e9b56c8ddd59bde498c291ee439b16ab930fc9335ee9c53cb47880b883f41d84d5a5c7725319e408675f0f3494938\",\"user_transaction_ids\":[\"174bda74079e9f6dc1941ad1ed1a4e86aa46a3c9\",\"46d42a546918b41d65fc9385e6ae3df699957519\"],\"signee_shares_issued\":150000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":40000,\"random_seed\":\"88ed508bae600bb48f077c51b97fc139f828d345\",\"block_size\":3566,\"latency\":1408000000,\"processing_time\":7001},{\"previous\":\"7892e7e238a4d555cf3cb8dee572c94b090af3be\",\"block_num\":1557911,\"timestamp\":\"2015-01-18T16:05:20\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"9355b7b06d9aad9513b7b0977ba370352783f8e4\",\"previous_secret\":\"6b0fd92557b812fa0cc181fac9d5319e54830969\",\"delegate_signature\":\"200c4f4b510a05f1f1ab174bccc29d64135d316a90dccd25a1860e564fe700a09213c80c5e95a2a8833c8cabdc2cdfc41d6c644cbada5b36ff6af0c740e610979a\",\"user_transaction_ids\":[\"0bfd31db574b946afb8b8669fcf35e81fe2cfad2\",\"b942ac34243683fdf91b73e45f4d48d952078019\",\"defc68b5dca5d14a75314cff33d5379f7e68e44b\",\"e03e1d2eccbb6bce9e7e7f3b1dbf911b710b6725\"],\"signee_shares_issued\":150000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":20000,\"random_seed\":\"74b8084feebe4cc7c1b8a097703cab131cca3f74\",\"block_size\":6787,\"latency\":1428000000,\"processing_time\":10000},{\"previous\":\"a304051a8903bd999d19cde935491237cda58f66\",\"block_num\":1557910,\"timestamp\":\"2015-01-18T16:05:10\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"99505dbd722c9b587bc62b59910f1c54a0fa7d05\",\"previous_secret\":\"54ae515ea840d3a6888ce3cd47f20fbfa03c1184\",\"delegate_signature\":\"1f27584b29fcca4d8ff50f5fc57425fa176e0888915d232c4f223cd2e60f1d5dbc768fe8d7d0353d057ff8d2db858abae2f21729964dc5708a6f308219c8e242c8\",\"user_transaction_ids\":[\"8e1f7292e085b2b49acbd096649fc9777d5759de\",\"c88a6b3aaa53ad0ce30a4e4fe52e76654ad99cad\"],\"signee_shares_issued\":5000000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":160000,\"random_seed\":\"b47823989db4d0332fd27ba4ee921aefc708faec\",\"block_size\":3488,\"latency\":1438000000,\"processing_time\":9001},{\"previous\":\"b40a0572e6c02b68da4414b3b92a5d23744be856\",\"block_num\":1557909,\"timestamp\":\"2015-01-18T16:05:00\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"d86c81969eaa36dceb23c4ce41d9f505b712740b\",\"previous_secret\":\"b5fd14dc498c39cf98c6c2120511feb74d824e6d\",\"delegate_signature\":\"207be3204ce836e7d88d2397645d7a39aeb1efdb55e23ee2e3ac7d7945c7a1d8eb020dbe50502d2e6251420c3a4f1d6bff29f9b62c41f1b5e3626c2a3c2fb1c106\",\"user_transaction_ids\":[\"026bb168182854053ec38c61564324330209b5f0\",\"195865b68ddf43a110833d2e3bd29319b277a449\",\"01cd666953afc2bdffd45d05a53ae2b8c2657da8\",\"17cd364a991b21522931d4ea88fa86b68d9ba96a\",\"20267de5b0b3fd45563b199b12a8c4294284a656\",\"56d1cc9260c89eaad08fc301e7c3c2f271852e25\",\"719d21a3bd285d95ef5238e47d180b245c10c349\",\"cadd6a306eb75758b9fdad69fab2ccf898bbdfce\"],\"signee_shares_issued\":150000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":0,\"random_seed\":\"53d5e624e11c6167bb8f9c9c3e37d3f1c3e0daa6\",\"block_size\":13623,\"latency\":1448000000,\"processing_time\":10000},{\"previous\":\"754dde279f3d79781a10a7b4285499979a3bfd05\",\"block_num\":1557908,\"timestamp\":\"2015-01-18T16:04:50\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"30eb73c868df86a650b429754a1fe26d187d7341\",\"previous_secret\":\"5c4f9ab01e1e272b01a9eaf7d1655343280b9117\",\"delegate_signature\":\"1f06c57bf2585e72bc6df5f9dc8ee24b75089bb161b7b9c7ca2db9e57807d0eace4a3937c9bac9d1bafa02081a7786cd62a9b78357e63339f4128a54a7a1c83841\",\"user_transaction_ids\":[],\"signee_shares_issued\":5000000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":150000,\"random_seed\":\"a54b1f1a53206532ac370265d1fefb4eb5de2d82\",\"block_size\":166,\"latency\":1458000000,\"processing_time\":8001},{\"previous\":\"0684b3e6456c46ce95db281445a39d65a11067bb\",\"block_num\":1557907,\"timestamp\":\"2015-01-18T16:04:40\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"f6b77c0de800177a9e4a8ebfa5b4de63f8ecc842\",\"previous_secret\":\"31bc3f445fdb33fa8e53e2f7c0397d7cca640f0e\",\"delegate_signature\":\"1f370456776c76dcdea42d197c28c26f98cd985351fd6f173cdd2a4fcb4a16dd3e50534e03ade882bf9ce78abeae388c4aa56e2db18b167f82570c00a0b95aa7c7\",\"user_transaction_ids\":[\"490e01135c372fe6a595a2713da06218d552523f\",\"748bf117e7f8c4b5e2ed62a1117a0488c76d68d1\",\"adc873caaa3c403294ce2fef58a5876a3c1361ce\"],\"signee_shares_issued\":150000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":0,\"random_seed\":\"56bf88ee24d93bff01c45964cba00defbf3dffe3\",\"block_size\":5132,\"latency\":1468000000,\"processing_time\":5000},{\"previous\":\"37afc44808b07aa11a29788583820339bf801c2f\",\"block_num\":1557906,\"timestamp\":\"2015-01-18T16:04:30\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"e2193ab35cc17ff418fbd6f7be6086e6fe48171d\",\"previous_secret\":\"5fbe4d18267de7e168156d0c6cf87d95d80c27d6\",\"delegate_signature\":\"1f59f94c66ca595c19c8c354bb40a2bc6f3a97eace00807eaef05f4538e7b96686476638561b7b863adde37022e2e86a6e5d5be82aa49ac03e9767796adec043fc\",\"user_transaction_ids\":[],\"signee_shares_issued\":150000,\"signee_fees_collected\":0,\"signee_fees_destroyed\":0,\"random_seed\":\"934d85a5e8dc6445737a02c9b38044554cf38401\",\"block_size\":166,\"latency\":1478000000,\"processing_time\":4000}]}";
	}
}
