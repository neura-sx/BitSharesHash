package sx.neura.bts.gui.view.pages.bts;

import sx.neura.bts.gui.ModelHelper;
import sx.neura.bts.gui.view.Tile;
import sx.neura.bts.json.exceptions.BTSSystemException;

public abstract class Tile_Bts<T> extends Tile<T> implements ModelHelper.Host {

	protected ModelHelper h;
	
	{
		this.h = new ModelHelper(this);
	}
	
	@Override
	public void systemException(BTSSystemException e) {
		module.systemException(e);
	}

	
}
