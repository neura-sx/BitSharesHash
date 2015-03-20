package sx.neura.bts.gui.view.components;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.Module;
import sx.neura.bts.json.api.blockchain.BlockchainGetAccount;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlockSignee;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.json.dto.Price;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.exceptions.BTSSystemException;

public abstract class Tile<T> extends AnchorPane implements Initializable {
	
	@FXML
	protected Node boxUI;
	
	protected T item;
	protected Module module;
	protected Region region;
	
	public void setItem(T item) {
		this.item = item;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (item != null && boxUI != null)
			boxUI.setVisible(true);
	}
	
	protected static boolean isAccountFavorite(String name) {
		return  Model.getInstance().isAccountFavorite(name);
	}
	
	protected static int getAccountApproval(String name) {
		return  Model.getInstance().getAccountApproval(name);
	}
	
	protected static boolean isDelegate(RegisteredName registeredName) {
		return  Model.getInstance().isDelegate(registeredName);
	}
	
	protected static boolean isActiveDelegate(RegisteredName registeredName) {
		return  Model.getInstance().isActiveDelegate(registeredName);
	}
	
	protected static boolean isAccountUnregistered(Account account) {
		return Model.getInstance().isAccountUnregistered(account);
	}
	
	protected static double getDelegateSupport(RegisteredName item) {
		return  Model.getInstance().getDelegateSupport(item);
	}
	
	protected static double getDelegateReliability(RegisteredName item) {
		return Model.getInstance().getDelegateReliability(item);
	}
	
	protected static String getAccountApprovalLabel(RegisteredName item) {
		return Model.getInstance().getAccountApprovalLabel(item);
	}
	
	protected static String[] getAmountPair(int assetId, long value) {
		return Model.getInstance().getAmountPair(assetId, value);
	}
	
	protected static String[] getAmountPair(Amount amount) {
		return Model.getInstance().getAmountPair(amount);
	}
	
	protected static String getAmount(Amount amount) {
    	return Model.getInstance().getAmount(amount);
    }
	
	protected static String getAmount(int assetId, long value) {
		return Model.getInstance().getAmount(assetId, value);
	}
	
	protected static String getAmount(Asset asset, long value) {
		return Model.getInstance().getAmount(asset, value);
	}
	
	protected static String getAmount(Asset asset, Double volume) {
		return Model.getInstance().getAmount(asset, volume);
	}
	
	protected static Asset getAssetById(int assetId) {
		return Model.getInstance().getAssetById(assetId);
	}
	
	protected static Asset getAssetBySymbol(String symbol) {
		return Model.getInstance().getAssetBySymbol(symbol);
	}
	
//	protected static Image getAvatarImage(String name) {
//		return Model.getInstance().getAvatarImage(name);
//	}
	
	protected static String getMarketLabel(Market market) {
		return Model.getInstance().getMarketLabel(market);
	}
	
	protected static String getMarketLabel(Asset quoteAsset, Asset baseAsset) {
		return Model.getInstance().getMarketLabel(quoteAsset, baseAsset);
	}
	
	protected static String getOrderTypeLabel(String bidType, String askType) {
		return Model.getInstance().getOrderTypeLabel(bidType, askType);
	}
	
	protected static double getRealPrice(Price price) {
		return Model.getInstance().getRealPrice(price);
	}
	
	protected static boolean isMarketPeggedAsset(Asset asset) {
		return Model.getInstance().isMarketPeggedAsset(asset);
	}
	
	
	
	
	protected Pane getModalPane() {
		return module.getPane();
	}

	protected Account getAccount(String name) {
		Account account = Model.getInstance().getAccountByName(name);
		if (account != null)
			return account;
		try {
			account = BlockchainGetAccount.run(name);
		} catch (BTSSystemException e) {
			module.systemException(e);
		}
		return account;
	}
	
	protected Account getAccount(long id) {
		if (id <= 0)
			return null;
		return getAccount(new Long(id).toString());
	}
	
	protected String getBlockSignee(String blockNumber) {
		String signee = null;
		try {
			signee = BlockchainGetBlockSignee.run(blockNumber);
		} catch (BTSSystemException e) {
			module.systemException(e);
		}
		return signee;
	}
}
