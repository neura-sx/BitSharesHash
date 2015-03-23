package sx.neura.bts.gui.view.pages.bts;

import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.ModelHelper;
import sx.neura.bts.gui.view.Tile;

public abstract class Tile_Bts<T> extends Tile<T> implements ModelHelper.Host {

	protected Model.Helper h;
	
	{
		this.h = new Model.Helper(this);
	}
}
