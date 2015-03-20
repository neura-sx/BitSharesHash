package sx.neura.btsx.gui.view.pages.bts;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import sx.neura.bts.json.api.blockchain.BlockchainGetAccount;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlock;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlockSignee;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.api.wallet.WalletGetTransactionFee;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.dto.Price;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.Page;

public abstract class Page_Bts extends Page {
	
	protected static double getDelegateSupport(RegisteredName item) {
		if (item.getDelegate_info() == null)
			return 0;
		return item.getDelegate_info().getVotes_for() / Model.getInstance().getCurrentShareSupply();
	}
	
	protected static double getDelegateReliability(RegisteredName item) {
		if (item.getDelegate_info() == null)
			return 0;
		return item.getDelegate_info().getBlocks_produced() / (double) (item.getDelegate_info().getBlocks_produced() + item.getDelegate_info().getBlocks_missed());
	}
	
	protected static int getAccountApproval(RegisteredName item) {
		return Model.getInstance().getAccountApproval(item.getName());
	}
	
	protected static String getAccountApprovalLabel(RegisteredName item) {
		return getAccountApprovalLabel(getAccountApproval(item));
	}
	
	private static String getAccountApprovalLabel(int approval) {
		if (approval == 1)
			return "positive";
		if (approval == -1)
			return "negative";
		return "neutral";
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
	
	protected static String[] getAmountPair(Amount amount) {
		return Model.getInstance().getAmountPair(amount);
	}
	
	protected static String getAmount(Asset asset, Double volume) {
		return Model.getInstance().getAmount(asset, volume);
	}
	
	protected static String getAmount(Asset asset, long amount) {
		return Model.getInstance().getAmount(asset, amount);
	}
	
	protected static String getAmount(int assetId, long amount) {
		return Model.getInstance().getAmount(assetId, amount);
	}
	
	protected static String getAmount(int assetId, long amount, int precision) {
		return Model.getInstance().getAmount(assetId, amount, precision);
	}
	
	protected static double getRealValue(Asset asset, long amount) {
		return Model.getInstance().getRealValue(asset, amount);
	}
	
	protected static double getRealValue(int assetId, long amount) {
		return Model.getInstance().getRealValue(assetId, amount);
	}
	
	protected static double getRealPrice(Price price) {
		return Model.getInstance().getRealPrice(price);
	}
	
	protected static Image getAvatarImage(String name) {
		return Model.getInstance().getAvatarImage(name);
	}
	
	protected static boolean isDelegate(RegisteredName registeredName) {
		return registeredName.getDelegate_info() != null;
	}
	
	protected static boolean isActiveDelegate(RegisteredName registeredName) {
		return Model.getInstance().isActiveDelegate(registeredName);
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
		Account account = null;
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
	
	protected <T, S> void adjustTableColumn(TableColumn<T, S> tc, Pos pos) {
    	tc.setCellFactory(item -> {
    		TableCell<T, S> cell = new TableCell<T, S>() {
    			@Override
    	        public void updateItem(S item, boolean empty) {
    	            super.updateItem(item, empty);
    	            setText(empty ? null : (item == null ? "" : item.toString()));
    	            setGraphic(null);
    	        }
    		};
    		cell.setAlignment(pos);
    		return cell;
		});
    }
}
