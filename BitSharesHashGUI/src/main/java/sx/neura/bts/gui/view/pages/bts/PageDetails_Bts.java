package sx.neura.bts.gui.view.pages.bts;

import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.ModelHelper;
import sx.neura.bts.gui.view.PageDetails;

public abstract class PageDetails_Bts<T> extends PageDetails<T> implements ModelHelper.Host {
	
	protected Model.Helper h;
	
	{
		this.h = new Model.Helper(this);
	}
	
	public PageDetails_Bts(T item) {
		super(item);
	}
	
	protected PageDetails_Bts() {
	}
	
}
