package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletGetSetting extends CommandJson {

	public static class Result {
		private int index;
		private String name;
		private String value;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "wallet_get_setting";
	
	public static CommandJson test(String name) {
		return doTest(COMMAND, new Object[] { name });
	}
	
	public static WalletGetSetting.Result run(String name) throws BTSSystemException {
		return run(0, name);
	}
	public static WalletGetSetting.Result run(int id, String name) throws BTSSystemException {
		WalletGetSetting i = (WalletGetSetting) new WalletGetSetting().doRun(id, COMMAND,
				new Object[] { name });
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
//		test("recent_markets");
		try {
			WalletGetSetting.Result result = run("recent_markets");
			System.out.println(result.getValue());
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}
	
	@Override
	protected String hack(Object[] params) {
		if (params[0].equals("sx-color-set"))
			return "{\"id\":0,\"result\":{\"index\":28,\"name\":\"sx-color-set\",\"value\":\"Set02\"}}";
		else if (params[0].equals("recent_markets"))
			return "{\"id\":0,\"result\":{\"index\":28,\"name\":\"recent_markets\",\"value\":\"['BTS:USD','BTS:CNY','BTS:GOLD']\"}}";
		return null;
	}
}
