package sx.neura.bts.gui.view.pages.bts;

import java.util.List;

import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.json.api.blockchain.BlockchainGetAccount;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlock;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlockSignee;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.api.wallet.WalletGetTransactionFee;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.json.dto.Price;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.enumerations.VoteMethod;
import sx.neura.bts.json.exceptions.BTSSystemException;

public abstract class Page_Bts extends Page {
	
	protected static double getDelegateSupport(RegisteredName item) {
		return  Model.getInstance().getDelegateSupport(item);
	}
	
	protected static double getDelegateReliability(RegisteredName item) {
		return Model.getInstance().getDelegateReliability(item);
	}
	
	protected static String getAccountApprovalLabel(RegisteredName item) {
		return Model.getInstance().getAccountApprovalLabel(item);
	}
	
	protected static String getAccountApprovalLabel(int approval) {
		return Model.getInstance().getAccountApprovalLabel(approval);
	}
	
	protected static Asset getAssetById(int assetId) {
		return Model.getInstance().getAssetById(assetId);
	}
	
	protected static Asset getAssetBySymbol(String symbol) {
		return Model.getInstance().getAssetBySymbol(symbol);
	}
	
	protected static Account getAccountByName(String name) {
		return Model.getInstance().getAccountByName(name);
	}
	
	protected static String[] getAmountPair(int assetId, long value) {
		return Model.getInstance().getAmountPair(assetId, value);
	}
	
	protected static String[] getAmountPair(Amount amount) {
		return Model.getInstance().getAmountPair(amount);
	}
	
	protected static String getAmount(Asset asset, Double volume) {
		return Model.getInstance().getAmount(asset, volume);
	}
	
	protected static String getAmount(Asset asset, long value) {
		return Model.getInstance().getAmount(asset, value);
	}
	
	protected static String getAmount(int assetId, long value) {
		return Model.getInstance().getAmount(assetId, value);
	}
	
	protected static String getAmount(int assetId, long value, int precision) {
		return Model.getInstance().getAmount(assetId, value, precision);
	}
	
	protected static double getRealValue(Asset asset, long value) {
		return Model.getInstance().getRealValue(asset, value);
	}
	
	protected static double getRealValue(int assetId, long value) {
		return Model.getInstance().getRealValue(assetId, value);
	}
	
	protected static double getRealPrice(Price price) {
		return Model.getInstance().getRealPrice(price);
	}
	
//	protected static Image getAvatarImage(String name) {
//		return Model.getInstance().getAvatarImage(name);
//	}
	
	protected static boolean isMarketPeggedAsset(Asset asset) {
		return Model.getInstance().isMarketPeggedAsset(asset);
	}
	
	protected static boolean isDelegate(RegisteredName registeredName) {
		return Model.getInstance().isDelegate(registeredName);
	}
	
	protected static boolean isActiveDelegate(RegisteredName registeredName) {
		return Model.getInstance().isActiveDelegate(registeredName);
	}
	
	protected static boolean isAccountUnregistered(Account account) {
		return Model.getInstance().isAccountUnregistered(account);
	}
	
	protected static String getVoteMethodLabel(VoteMethod voteMethod) {
		return Model.getInstance().getVoteMethodLabel(voteMethod);
	}
	
	protected static String getMarketLabel(Market market) {
		return Model.getInstance().getMarketLabel(market);
	}
	
	protected static String getMarketLabel(Asset quoteAsset, Asset baseAsset) {
		return Model.getInstance().getMarketLabel(quoteAsset, baseAsset);
	}
	
	protected static String format(double realValue, int precision) {
	    return Model.getInstance().format(realValue, precision);
	}
	
	
	
	
	
	protected String getBlockSignee(String blockNumber) {
		String signee = null;
		try {
			signee = BlockchainGetBlockSignee.run(blockNumber);
		} catch (BTSSystemException e) {
			systemException(e);
		}
		return signee;
	}
	
	protected Block getBlock(String blockNumber) {
		Block block = null;
		try {
			block = BlockchainGetBlock.run(blockNumber);
		} catch (BTSSystemException e) {
			systemException(e);
		}
		return block;
	}
	
	protected Account getAccount(String name) {
		Account account = Model.getInstance().getAccountByName(name);
		if (account != null)
			return account;
		try {
			account = BlockchainGetAccount.run(name);
		} catch (BTSSystemException e) {
			systemException(e);
		}
		return account;
	}
	
	protected Account getAccount(long id) {
		if (id <= 0)
			return null;
		return getAccount(new Long(id).toString());
	}
	
	protected long getTransactionFee(Asset asset) {
		Amount amount = null;
		try {
			amount = WalletGetTransactionFee.run(asset.getSymbol());
		} catch (BTSSystemException e) {
			systemException(e);
		}
		return amount.getValue();
	}
	
	protected long getAvailableAmount(String accountName, Asset asset) {
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
			systemException(e);
		}
		return 0;
	}
}
