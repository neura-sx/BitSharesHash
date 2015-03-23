package sx.neura.bts.gui;

import java.util.List;

import sx.neura.bts.json.api.blockchain.BlockchainGetAccount;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlock;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlockSignee;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.api.wallet.WalletGetTransactionFee;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.exceptions.BTSSystemException;


public class ModelHelper {
	
	public interface Host {
		public void systemException(BTSSystemException e);
	}
	
	private Host host;
	
	public ModelHelper(Host host) {
		this.host = host;
	}
	
	
	public String getBlockSignee(String blockNumber) {
		String signee = null;
		try {
			signee = BlockchainGetBlockSignee.run(blockNumber);
		} catch (BTSSystemException e) {
			host.systemException(e);
		}
		return signee;
	}
	
	public Block getBlock(String blockNumber) {
		Block block = null;
		try {
			block = BlockchainGetBlock.run(blockNumber);
		} catch (BTSSystemException e) {
			host.systemException(e);
		}
		return block;
	}
	
	public Account getAccount(String name) {
		Account account = Model.getInstance().getAccountByName(name);
		if (account != null)
			return account;
		try {
			account = BlockchainGetAccount.run(name);
		} catch (BTSSystemException e) {
			host.systemException(e);
		}
		return account;
	}
	
	public Account getAccount(long id) {
		if (id <= 0)
			return null;
		return getAccount(new Long(id).toString());
	}
	
	public long getTransactionFee(Asset asset) {
		Amount amount = null;
		try {
			amount = WalletGetTransactionFee.run(asset.getSymbol());
		} catch (BTSSystemException e) {
			host.systemException(e);
		}
		return amount.getValue();
	}
	
	public long getAvailableAmount(String accountName, Asset asset) {
		try {
			List<WalletAccountBalance.Result> list = WalletAccountBalance.run(accountName);
			if (list.size() == 0)
				return 0;
			WalletAccountBalance.Result item = list.get(0);
			for (Amount amount : item.getAmounts()) {
				if (amount.getAsset_id() == asset.getId())
					return amount.getValue();
			}
		} catch (BTSSystemException e) {
			host.systemException(e);
		}
		return 0;
	}
}
