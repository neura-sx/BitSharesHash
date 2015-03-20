package sx.neura.bts.json.api.blockchain;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetBlockSignee extends CommandJson {
	
	private String result;

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
	
	private static final String COMMAND = "blockchain_get_block_signee";
	
	public static CommandJson test(String block) {
		return doTest(COMMAND, new Object[] { block });
	}
	
	public static String run(String block) throws BTSSystemException {
		return run(0, block);
	}
	public static String run(int id, String block) throws BTSSystemException {
		BlockchainGetBlockSignee i = (BlockchainGetBlockSignee) new BlockchainGetBlockSignee().doRun(id, COMMAND, 
				new Object[] { block });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("1084767");
		try {
			System.out.println(run("1084767"));
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":\"delegate1-galt\"}";
	}
}
