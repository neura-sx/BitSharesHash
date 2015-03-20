package sx.neura.bts.json.api.network;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class NetworkGetConnectionCount extends CommandJson {

	private Integer result;

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getResult() {
		return result;
	}
	
	private static final String COMMAND = "network_get_connection_count";
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	
	public static Integer run() throws BTSSystemException {
		return run(0);
	}
	public static Integer run(int id) throws BTSSystemException {
		NetworkGetConnectionCount i = (NetworkGetConnectionCount) new NetworkGetConnectionCount().doRun(id, COMMAND);
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
		//test();
		try {
			System.out.println(run());
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":7}";
	}
}
