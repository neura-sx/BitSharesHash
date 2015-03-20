package sx.neura.bts.gui.view.components.display;


public class DisplayCheckList_HV<T> extends DisplayCheckList<T> {
	@Override
	protected DisplayCheck createCheck() {
		return new DisplayCheck_V();
	}
}
