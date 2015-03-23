package sx.neura.bts.gui.view.pages.bts;

import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.ModelHelper;
import sx.neura.bts.gui.view.Page;

public abstract class Page_Bts extends Page implements ModelHelper.Host {
	
	protected Model.Helper h;
	
	{
		this.h = new Model.Helper(this);
	}
}
