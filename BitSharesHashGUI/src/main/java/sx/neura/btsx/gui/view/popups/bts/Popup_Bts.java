package sx.neura.btsx.gui.view.popups.bts;

import java.util.List;

import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.api.wallet.WalletGetTransactionFee;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.Popup;

public abstract class Popup_Bts extends Popup {
	
	protected static Asset getAssetById(int assetId) {
		return Model.getInstance().getAssetById(assetId);
	}
	
	protected static String getAmount(Asset asset, Double volume) {
		return Model.getInstance().getAmount(asset, volume);
	}
	
	protected static String getAmount(Asset asset, long amount) {
		return Model.getInstance().getAmount(asset, amount);
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
	
	protected static double getRealValue(Asset asset, long amount) {
		return Model.getInstance().getRealValue(asset, amount);
	}
	
	protected static double getRealValue(int assetId, long amount) {
		return Model.getInstance().getRealValue(assetId, amount);
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
