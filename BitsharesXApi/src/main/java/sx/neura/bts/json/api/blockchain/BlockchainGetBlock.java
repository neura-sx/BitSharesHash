package sx.neura.bts.json.api.blockchain;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetBlock extends CommandJson {
	
	private Block result;

	public void setResult(Block result) {
		this.result = result;
	}

	public Block getResult() {
		return result;
	}
	
	private static final String COMMAND = "blockchain_get_block";
	
	public static CommandJson test(String block) {
		return doTest(COMMAND, new Object[] { block });
	}
	
	public static Block run(String block) throws BTSSystemException {
		return run(0, block);
	}
	public static Block run(int id, String block) throws BTSSystemException {
		BlockchainGetBlock i = (BlockchainGetBlock) new BlockchainGetBlock().doRun(id, COMMAND, 
				new Object[] { block });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
		test("1084767");
//		try {
//			Block block = run("1084767");
//			System.out.println(String.format("%8d %s %2d", block.getBlock_num(), block.getTimestamp(), block.getUser_transaction_ids().size()));
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"previous\":\"4096d9377c615187241daffbfa3f8220b814676f\",\"block_num\":1084767,\"timestamp\":\"2014-11-23T13:07:30\",\"transaction_digest\":\"c8cf12fe3180ed901a58a0697a522f1217de72d04529bd255627a4ad6164f0f0\",\"next_secret_hash\":\"8df6a4cc411472e1aed8712095733322f84392e9\",\"previous_secret\":\"d4039ef43933095b339a614205f1adcfa69dd3b5\",\"delegate_signature\":\"1fecdbf2bc10b40bd4585b5dcaa281e1dea1bff70539835f76f45b2a40b7db34af09139fc2d5a99e9972cfcf26b8d53f10ca605e2fc0cb51db9502c53031dc0c97\",\"user_transaction_ids\":[\"44fb76c2e38173660bcd2ee274673301cb817d7c\"],\"block_size\":382,\"latency\":7070297000000,\"signee_shares_issued\":0,\"signee_fees_collected\":0,\"signee_fees_destroyed\":0,\"random_seed\":\"0000000000000000000000000000000000000000\",\"processing_time\":0}}";
	}
}
