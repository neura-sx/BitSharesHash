package sx.neura.bts.json.api.blockchain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetBlockTransactions extends CommandJson {
	
	public static class Result extends ArrayList<Object> {
		private static final long serialVersionUID = 1L;
		
		private String id;
		private Transaction transaction;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

		public Transaction getTransaction() {
			return transaction;
		}
		public void setTransaction(Transaction transaction) {
			this.transaction = transaction;
		}
		
		public Result convert() {
			for (Object obj : this) {
				if (obj instanceof String) {
					id = (String) obj;
				} else if (obj instanceof LinkedHashMap<?, ?>) {
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, ?> t1 = (LinkedHashMap<String, ?>) obj;
					transaction = new Transaction();
//					transaction.setCurrent_op_index((Integer) t1.get("current_op_index"));
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, ?> t2 = (LinkedHashMap<String, ?>) t1.get("trx");
					Trx trx = new Trx();
					trx.setExpiration((String) t2.get("expiration"));
					
					{
						List<Operation> operations = new ArrayList<Operation>();
						@SuppressWarnings("unchecked")
						List<LinkedHashMap<String, ?>> t3 = (List<LinkedHashMap<String, ?>>) t2.get("operations");
						for (LinkedHashMap<String, ?> t4 : t3) {
							Operation operation =  new Operation();
							operation.setType((String) t4.get("type"));
							operations.add(operation);
						}
						trx.setOperations(operations);
					}
					{
						List<NetDelegateVote> netDelegateVotes = new ArrayList<NetDelegateVote>();
						@SuppressWarnings("unchecked")
						List<List<Object>> t5 = (List<List<Object>>) t1.get("net_delegate_votes");
						for (List<Object> t6 : t5) {
							NetDelegateVote netDelegateVote =  new NetDelegateVote();
							for (Object t7 : t6) {
								if (t7 instanceof Long) {
									netDelegateVote.setId((Long) t7);
								} else if (t7 instanceof Integer) {
									netDelegateVote.setId(((Integer) t7).longValue());
								} else if (t7 instanceof LinkedHashMap<?, ?>) {
									@SuppressWarnings("unchecked")
									LinkedHashMap<String, ?> t8 = (LinkedHashMap<String, ?>) t7;
									netDelegateVote.setVotes_for((Integer) t8.get("votes_for"));
								}
							}
							netDelegateVotes.add(netDelegateVote);
						}
						trx.setNet_delegate_votes(netDelegateVotes);
					}
					{
						List<Amount> deposits = new ArrayList<Amount>();
						@SuppressWarnings("unchecked")
						List<List<Object>> t5 = (List<List<Object>>) t1.get("deposits");
						for (List<Object> t6 : t5) {
							Amount amount =  new Amount();
							for (Object t7 : t6) {
								if (t7 instanceof LinkedHashMap<?, ?>) {
									@SuppressWarnings("unchecked")
									LinkedHashMap<String, ?> t8 = (LinkedHashMap<String, ?>) t7;
									amount.setAsset_id((Integer) t8.get("asset_id"));
									if (t8.get("amount") instanceof Integer)
										amount.setValue((Integer) t8.get("amount"));
									else
										amount.setValue((Long) t8.get("amount"));
								}
							}
							deposits.add(amount);
						}
						trx.setDeposits(deposits);
					}
					{
						List<Amount> withdraws = new ArrayList<Amount>();
						@SuppressWarnings("unchecked")
						List<List<Object>> t5 = (List<List<Object>>) t1.get("withdraws");
						for (List<Object> t6 : t5) {
							Amount amount =  new Amount();
							for (Object t7 : t6) {
								if (t7 instanceof LinkedHashMap<?, ?>) {
									@SuppressWarnings("unchecked")
									LinkedHashMap<String, ?> t8 = (LinkedHashMap<String, ?>) t7;
									amount.setAsset_id((Integer) t8.get("asset_id"));
									if (t8.get("amount") instanceof Integer)
										amount.setValue((Integer) t8.get("amount"));
									else
										amount.setValue((Long) t8.get("amount"));
								}
							}
							withdraws.add(amount);
						}
						trx.setWithdraws(withdraws);
					}
					{
						List<Amount> balance = new ArrayList<Amount>();
						@SuppressWarnings("unchecked")
						List<List<Integer>> t5 = (List<List<Integer>>) t1.get("balance");
						for (List<Integer> t6 : t5) {
							Amount amount =  new Amount();
							amount.setAsset_id(t6.get(0));
							amount.setValue(t6.get(1));
							balance.add(amount);
						}
						trx.setBalance(balance);
					}
					transaction.setTrx(trx);
				}
			}
			return this;
		}
	}
	
	public static class Transaction {
		private Trx trx;
//		private int current_op_index;
		public Trx getTrx() {
			return trx;
		}
		public void setTrx(Trx trx) {
			this.trx = trx;
		}
//		public int getCurrent_op_index() {
//			return current_op_index;
//		}
//		public void setCurrent_op_index(int current_op_index) {
//			this.current_op_index = current_op_index;
//		}
	}
	
	public static class Trx {
		private String expiration;
		private String slate_id;
		private List<Operation> operations;
		private List<String> signatures;
		private List<NetDelegateVote> net_delegate_votes;
		private List<Amount> withdraws;
		private List<Amount> deposits;
		private List<Amount> balance;
		public String getExpiration() {
			return expiration;
		}
		public void setExpiration(String expiration) {
			this.expiration = expiration;
		}
		public String getSlate_id() {
			return slate_id;
		}
		public void setSlate_id(String slate_id) {
			this.slate_id = slate_id;
		}
		public List<Operation> getOperations() {
			return operations;
		}
		public void setOperations(List<Operation> operations) {
			this.operations = operations;
		}
		public List<String> getSignatures() {
			return signatures;
		}
		public void setSignatures(List<String> signatures) {
			this.signatures = signatures;
		}
		public List<NetDelegateVote> getNet_delegate_votes() {
			return net_delegate_votes;
		}
		public void setNet_delegate_votes(List<NetDelegateVote> net_delegate_votes) {
			this.net_delegate_votes = net_delegate_votes;
		}
		public List<Amount> getWithdraws() {
			return withdraws;
		}
		public void setWithdraws(List<Amount> withdraws) {
			this.withdraws = withdraws;
		}
		public List<Amount> getDeposits() {
			return deposits;
		}
		public void setDeposits(List<Amount> deposits) {
			this.deposits = deposits;
		}
		public List<Amount> getBalance() {
			return balance;
		}
		public void setBalance(List<Amount> balance) {
			this.balance = balance;
		}
		
	}
	
	public static class Operation {
		private String type;
		private Object data;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Object getData() {
			return data;
		}
		public void setData(Object data) {
			this.data = data;
		}
	}
	
	public static class NetDelegateVote {
		private long id;
		private long votes_for;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public long getVotes_for() {
			return votes_for;
		}
		public void setVotes_for(long votes_for) {
			this.votes_for = votes_for;
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
	
	private static final String COMMAND = "blockchain_get_block_transactions";
	
	public static CommandJson test(String block) {
		return doTest(COMMAND, new Object[] { block });
	}
	
	public static List<Result> run(String block) throws BTSSystemException {
		return run(0, block);
	}
	public static List<Result> run(int id, String block) throws BTSSystemException {
		BlockchainGetBlockTransactions i = (BlockchainGetBlockTransactions) new BlockchainGetBlockTransactions().doRun(id, COMMAND, 
				new Object[] { block });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("1336209");
		try {
			List<BlockchainGetBlockTransactions.Result> list = run("1336209");
			for (Result result : list) {
				System.out.println(String.format("%s %s", result.getId(), 
//						result.getTransaction().getCurrent_op_index(),
						result.getTransaction().getTrx().getExpiration()));
			}
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "";
	}
}
