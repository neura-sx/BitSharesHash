package sx.neura.bts.gui.view.pages.bts;

import sx.neura.bts.gui.ModelHelper;
import sx.neura.bts.gui.view.Page;

public abstract class Page_Bts extends Page implements ModelHelper.Host {
	
	protected ModelHelper h;
	
	{
		this.h = new ModelHelper(this);
	}
}
