package sx.neura.btsx.gui.view.pages.bts.impl;

public class MarketList02 extends MarketList {
	
	private static MarketList02 instance;
	public static MarketList02 getInstance() {
		if (!isInstance())
			instance = new MarketList02();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private MarketList02() {
	}
	
	@Override
	public String getName() {
		return "Market Slot 02";
	}
}
