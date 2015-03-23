package sx.neura.bts.gui.view.pages.bts;

import sx.neura.bts.gui.ModelHelper;
import sx.neura.bts.gui.view.PageDetails;

public abstract class PageDetails_Bts<T> extends PageDetails<T> implements ModelHelper.Host {
	
	protected ModelHelper h;
	
	{
		this.h = new ModelHelper(this);
	}
	
	public PageDetails_Bts(T item) {
		super(item);
	}
	
	protected PageDetails_Bts() {
	}
	
}
