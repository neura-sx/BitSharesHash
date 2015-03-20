package sx.neura.bts.json.api.wallet;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.enumerations.VoteMethod;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public class WalletTransfer extends CommandJson {

	public static class Result {
	}

	private Result result;
	
	public void setResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}

	private static final String COMMAND = "wallet_transfer";
	
	public static CommandJson test(double amountToTransfer, String assetSymbol, String fromAccountName, String toAccountName, String memoMessage, VoteMethod voteMethod) {
		return doTest(COMMAND, new Object[] { amountToTransfer, assetSymbol, fromAccountName, toAccountName, memoMessage, voteMethod.toString()});
	}
	
	public static void run(double amountToTransfer, String assetSymbol, String fromAccountName, String toAccountName, String memoMessage, VoteMethod voteMethod) throws BTSUserException, BTSSystemException {
		run(0, amountToTransfer, assetSymbol, fromAccountName, toAccountName, memoMessage, voteMethod);
	}
	public static void run(int id, double amountToTransfer, String assetSymbol, String fromAccountName, String toAccountName, String memoMessage, VoteMethod voteMethod) throws BTSUserException, BTSSystemException {
		WalletTransfer i = (WalletTransfer) new WalletTransfer().doRun(id, COMMAND, 
				new Object[] { amountToTransfer, assetSymbol, fromAccountName, toAccountName, memoMessage, voteMethod.toString() });
		i.checkUserException(new int[]{36005, 20010});
		i.checkSystemException();
	}

	public static void main(String[] args) {
		test(1.2, "BTSX", "gringoy", "senna", "intercepting", VoteMethod.RANDOM);
//		try {
//			run(1.1, "BTSX", "gringoy", "senna", "intercepting", VoteMethod.RANDOM);
//			System.out.println("SUCCESS");
//		} catch (BTSUserException e) {
//			System.out.println(e.getError().getMessage());
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"index\":99,\"record_id\":\"87839287a2a4bce1cef938f6b33799f03a3f29ff\",\"block_num\":0,\"is_virtual\":false,\"is_confirmed\":false,\"is_market\":false,\"trx\":{\"expiration\":\"20141006T175945\",\"delegate_slate_id\":null,\"operations\":[{\"type\":\"withdraw_op_type\",\"data\":{\"balance_id\":\"BTSXFZmZAJyksMRv4Z7NEKyGNZxYzQbrJLnZn\",\"amount\":130000,\"claim_input_data\":\"\"}},{\"type\":\"deposit_op_type\",\"data\":{\"amount\":120000,\"condition\":{\"asset_id\":0,\"delegate_slate_id\":0,\"type\":\"withdraw_signature_type\",\"data\":{\"owner\":\"BTSXMyVPQ9rzCLYMV517LftRfBJndS7L56pnd\",\"memo\":{\"one_time_key\":\"BTSX8AstXxotHGtE1ngQiTq5S7g1wdn4mB2yqUuYErKzBvjsApfND9\",\"encrypted_memo_data\":\"de8839c7198c8db9942dad35e5e32afb83606d52d8a51635583ebefe9b2cc47595c8b99f03c7a148b12037d663845f7d8ec63783e8048b8363840cc7b1abfdde\"}}}}}],\"signatures\":[\"200022cd5bfa458aadf75799460e5310b6d61f3092b2445bbb0a8033c0d1252403feedec52e2d4e827562a1da703886887732ae453e44f215fe768d5b2bf7956a4\"]},\"ledger_entries\":[{\"from_account\":\"BTSX6j9JntK13FX4KYnoj3YX7gzKudxmA7s7r8C8nsXKgX1FSwnEKc\",\"to_account\":\"BTSX5HweZHhiYJct6NRdoUprMq13owVdvhtFsjuDfggt8oZRMj7Q3p\",\"amount\":{\"amount\":120000,\"asset_id\":0},\"memo\":\"intercepting\",\"memo_from_account\":null}],\"fee\":{\"amount\":10000,\"asset_id\":0},\"created_time\":\"20141006T165225\",\"received_time\":\"20141006T165225\",\"extra_addresses\":[]}}";
	}
}
