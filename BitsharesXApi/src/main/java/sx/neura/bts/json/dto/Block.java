package sx.neura.bts.json.dto;

import java.util.List;

public class Block implements Comparable<Block> {
	private String previous;
	private String block_num;
	private String timestamp;
	private String transaction_digest;
	private String next_secret_hash;
	private String previous_secret;
	private String delegate_signature;
	private List<String> user_transaction_ids;
	private String random_seed;
	private int block_size;
	private long latency;
	private int processing_time;
	public String getPrevious() {
		return previous;
	}
	public void setPrevious(String previous) {
		this.previous = previous;
	}
	public String getBlock_num() {
		return block_num;
	}
	public void setBlock_num(String block_num) {
		this.block_num = block_num;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransaction_digest() {
		return transaction_digest;
	}
	public void setTransaction_digest(String transaction_digest) {
		this.transaction_digest = transaction_digest;
	}
	public String getNext_secret_hash() {
		return next_secret_hash;
	}
	public void setNext_secret_hash(String next_secret_hash) {
		this.next_secret_hash = next_secret_hash;
	}
	public String getPrevious_secret() {
		return previous_secret;
	}
	public void setPrevious_secret(String previous_secret) {
		this.previous_secret = previous_secret;
	}
	public String getDelegate_signature() {
		return delegate_signature;
	}
	public void setDelegate_signature(String delegate_signature) {
		this.delegate_signature = delegate_signature;
	}
	public List<String> getUser_transaction_ids() {
		return user_transaction_ids;
	}
	public void setUser_transaction_ids(List<String> user_transaction_ids) {
		this.user_transaction_ids = user_transaction_ids;
	}
	public String getRandom_seed() {
		return random_seed;
	}
	public void setRandom_seed(String random_seed) {
		this.random_seed = random_seed;
	}
	public int getBlock_size() {
		return block_size;
	}
	public void setBlock_size(int block_size) {
		this.block_size = block_size;
	}
	public long getLatency() {
		return latency;
	}
	public void setLatency(long latency) {
		this.latency = latency;
	}
	public int getProcessing_time() {
		return processing_time;
	}
	public void setProcessing_time(int processing_time) {
		this.processing_time = processing_time;
	}
	@Override
	public int compareTo(Block o) {
		return getBlock_num().compareTo(o.getBlock_num());
	}
}
