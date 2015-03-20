package sx.neura.bts.json.api;

import java.util.ArrayList;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class Ping extends CommandJson {
	
	public static class Result extends ArrayList<Object> {
		private static final long serialVersionUID = 1L;
	}

	public void setResult(Result result) {
	}
	
	private static final String COMMAND = "wallet_account_balance";
	private static String content = null;
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	
	public static boolean run() throws BTSSystemException {
		return run(0);
	}
	public static boolean run(int id) throws BTSSystemException {
		Ping i = (Ping) new Ping().doRun(id, COMMAND);
		i.checkSystemException();
		if (content == null) {
			content = i.getContent();
			return false;
		}
		if (content.equals(i.getContent()))
			return false;
		content = i.getContent();
		return true;
	}

	public static void main(String[] args) {
		//test();
		try {
			for (int i = 0; i < 10; i++)
				System.out.println(run());
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[[\"gringox\",[[0,160876],[22,119998]]],[\"gringoy\",[[0,2687877]]]]}";
	}
}
