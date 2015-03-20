package sx.neura.bts.json.api.blockchain;

import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetAccountWall extends CommandJson {
	
	public static class Result {
		private int account_id;
		private String transaction_id;
		private Amount amount;
		private String message;
		private String signer;
		public int getAccount_id() {
			return account_id;
		}
		public void setAccount_id(int account_id) {
			this.account_id = account_id;
		}
		public String getTransaction_id() {
			return transaction_id;
		}
		public void setTransaction_id(String transaction_id) {
			this.transaction_id = transaction_id;
		}
		public Amount getAmount() {
			return amount;
		}
		public void setAmount(Amount amount) {
			this.amount = amount;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getSigner() {
			return signer;
		}
		public void setSigner(String signer) {
			this.signer = signer;
		}
	}
	
	private List<Result> results;

	public void setResult(List<Result> results) {
		this.results = results;
	}
	
	public List<Result> getResult() {
		return results;
	}
	
	private static final String COMMAND = "blockchain_get_account_wall";
	
	public static CommandJson test(String name) {
		return doTest(COMMAND, new Object[] { name });
	}
	
	public static List<BlockchainGetAccountWall.Result> run(String name) throws BTSSystemException {
		return run(0, name);
	}
	public static List<BlockchainGetAccountWall.Result> run(int id, String name) throws BTSSystemException {
		BlockchainGetAccountWall i = (BlockchainGetAccountWall) new BlockchainGetAccountWall().doRun(id, COMMAND, 
				new Object[] { name });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("dev0.nikolai");
		try {
			List<BlockchainGetAccountWall.Result> list = run("dev0.nikolai");
			for (BlockchainGetAccountWall.Result result : list) {
				System.out.println(String.format("[%s]: %s %6d %s",
						result.getTransaction_id(),
						result.getSigner(),
						result.getAmount().getValue(),
						result.getMessage()));
			}
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[{\"account_id\":30739,\"transaction_id\":\"13dd19adbaf8da722c784b5549accbf27c38952a\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"this is delegate of toast\",\"signer\":\"203c49fd114f014be9401f4be5ce8d878b5394d601b6a5e7130cc6d8aa70e50910528807cd8ec3b02be19132aacb3791707e29cbd4db20707fe6ce93077898dae6\"},{\"account_id\":30739,\"transaction_id\":\"282aef982dd5f1ca7098269d8b9cd55b71cff534\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"欢迎加入�? 比特原型股社区 www.bitpts.org ^_^\",\"signer\":\"1f52ad5519aab4adec0e334c44943ca3a6c12d8582f669e68de710091350f8a0467d4be1fc68591a8b229fc52cfe6d5f950184f17d4f000d22dc92d89195ac528b\"},{\"account_id\":30739,\"transaction_id\":\"4b5761c41c5c187d486d36baa598408588fa275d\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"出售正宗的土鸡蛋，�?的电�?�?�系15070538286\",\"signer\":\"1f0cf1a904a44b8b6721eb0af438c3fa6de07c5b8ca84cedb5c26667b8221d922a537535213543570794aeeb416a3904d4e4e43ce2435e0edc506acbd638f2a9c9\"},{\"account_id\":30739,\"transaction_id\":\"58c799500932e3d9ec812156aaf920fdab126c29\",\"amount\":{\"amount\":200000,\"asset_id\":0},\"message\":\"BITVPN 资产 拥有�?�以�?费使用VPN上网\",\"signer\":\"1f3a8d1a5de349d437638e677e0c39ce8d185516767e2f32ec2d98ee9d8710477a3461b333da04efd58f1940704d3b9a5743cfe04738f2bc624538a5a7f7d6af05\"},{\"account_id\":30739,\"transaction_id\":\"624a1a8ed2d49987939b088eef700e8f2bdbf2b1\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"�?�的什么钱包啊，�?�?了10W BTS，是�?是你赔我？\",\"signer\":\"1f2a69dc7639a570cebbb785e9442a10ad875a21b5340943fdb05c90c6b499f64b6e43bcfea2615933ffe9d7a6fea223026abb6045ada65865e28870fbce39741e\"},{\"account_id\":30739,\"transaction_id\":\"9aecb992a64aaa9a6fa559689c2012b9daae4afa\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"qiangqiang dao ci yi you\",\"signer\":\"1f09a4e65405866400bd618dfca0e9f3cfedc82551a867b6c70f6e7848a2ae366f4572b282f859e0c888c8d995e2895bcc4c5b5014524b5ce631e76dba374f1714\"},{\"account_id\":30739,\"transaction_id\":\"a6de28255e939a59e4da6d6243c03d5d79f20b9e\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"�?最新的�?��?地�?�，请�?��?10BTS到wuyanren账�?�，消�?�写上CL.\",\"signer\":\"1f39501eaa13e51305480701f1ef931e416e2f1acc0c631de94b88ac7fabc3a2b9132322dc7e7486551752d877945b19c6294b5a721492362492f6cc13fc2b0a11\"},{\"account_id\":30739,\"transaction_id\":\"aba27483511105841c69983799e3b44116b69a87\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"代找�?��?电影�?�?，需�?的给我�?�1BTS，memo电影�??字，下载�?功�?�看�?�给�?费。ID：bigbig\",\"signer\":\"1f64798aa8e5d30362301f4f43cea363a6b2c58cdce718c62951e383bbe8f1ccf96d04e1814e477cda56537d47da307e0b88785e55154a747e71cd6d9421ece47d\"},{\"account_id\":30739,\"transaction_id\":\"befcc46215bcfb4bfde9028e95cff04fd6a83e56\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"BTS能到500刀�?�\",\"signer\":\"1f5288067ad031c0b0173514e61de9e3bfd9312e8b185f6be856e61a399ec209ea2c3f16a8553c529e638cce77815ffa57fe2d730ded343688ca3588bc92ea7f38\"},{\"account_id\":30739,\"transaction_id\":\"c963bd14136415b7c9981d8ccf64c22a77f97fdb\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"土�?�哥哥，�?妹妹很寂寞啊，你晚上有空陪我�?�？\",\"signer\":\"1f5077f79443dcac79c8f8cf78a4e77e4802f35f66fcba437120a339226852eece508918f5dbd7d3d30f2bf4df8799faf5c7823bfbe4782b7dd2bfd3ec46529210\"},{\"account_id\":30739,\"transaction_id\":\"d3db5d9a1beee7397ce4b6eb5a5279c4578bc248\",\"amount\":{\"amount\":100000,\"asset_id\":0},\"message\":\"无言人让我�?��?�的，我没有办法，借你的墙一用啊。\",\"signer\":\"1f0562b7c40b6139643531b0b0986faeba58dc48089b3c2a54873690283c5d5f2a6b33b395edc0162c37c0bb5c598babaf3d36e60748621fdd435409e3feb7b4c3\"}]}";
	}
}
