package sx.neura.bts.gui.view;

import sx.neura.bts.json.exceptions.BTSSystemException;

public abstract class ModelHelper {
	
	public interface Host {
		public void systemException(BTSSystemException e);
	}
	
	protected Host host;
	
	public ModelHelper(Host host) {
		this.host = host;
	}
}
