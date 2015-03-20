package sx.neura.bts.gui.view.components.display;


public class DisplayToggleList_VX<T> extends DisplayToggleList<T> {
	@Override
	protected DisplayToggle createToggle() {
		return new DisplayToggle_X();
	}
}
