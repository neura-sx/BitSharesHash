package sx.neura.bts.json.api.wallet;

import java.util.ArrayList;
import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class WalletAccountBalance extends CommandJson {
	
	public static class Result extends ArrayList<Object> {
		private String name;
		private List<Amount> amounts;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Amount> getAmounts() {
			return amounts;
		}
		public void setAmounts(List<Amount> amounts) {
			this.amounts = amounts;
		}
		
		private static final long serialVersionUID = 1L;
		public Result convert() {
			for (Object obj : this) {
				if (obj instanceof String) {
					name = (String) obj;
				} else if (obj instanceof List) {
					amounts = new ArrayList<Amount>();
					@SuppressWarnings("unchecked")
					List<List<?>> list = (List<List<?>>) obj;
					for (List<?> item : list) {
						Amount amount = new Amount();
						amount.setAsset_id(getInteger(item.get(0)));
						amount.setValue(getLong(item.get(1)));
						amounts.add(amount);
					}
				}
			}
			return this;
		}
	}
	
	private List<Result> result;

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public List<Result> getResult() {
		for (Result r : result)
			r.convert();
		return result;
	}
	
	private static final String COMMAND = "wallet_account_balance";
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	public static CommandJson test(String accountName) {
		return doTest(COMMAND, new Object[] { accountName });
	}
	
	public static List<WalletAccountBalance.Result> run() throws BTSSystemException {
		return run(0, "");
	}
	public static List<WalletAccountBalance.Result> run(String accountName) throws BTSSystemException {
		return run(0, accountName);
	}
	public static List<WalletAccountBalance.Result> run(int id, String accountName) throws BTSSystemException {
		WalletAccountBalance i = (WalletAccountBalance) new WalletAccountBalance().doRun(
				id, COMMAND, new Object[] { accountName });
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
		test("");
//		try {
//			List<WalletAccountBalance.Result> list = run("");
//			for (Result result : list) {
//				System.out.println(result.getName());
//				for (Amount amount : result.getAmounts())
//					System.out.println(String.format("%d: %d", amount.getAsset_id(), amount.getValue()));
//			}
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[[\"fellowtraveler\",[[0,13199084],[22,15997]]],[\"gringox\",[[0,4830392],[14,16284],[22,741]]],[\"gringoy\",[[0,43950905],[14,115453],[22,173796]]],[\"jaunpol\",[[0,95950],[22,19792]]],[\"senna\",[[0,710000]]],[\"umma\",[[0,90000],[22,2500]]]]}";
	}
}
