package sx.neura.bts.gui.view.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class SlidePane extends ScrollPane {
	
	private static final double WIDTH_OFFSET = 4.0;
	
	private boolean isInitialized;
	
	private int listSize;
	private double itemWidth;
	private Button nodeFwd;
	private Button nodeBck;
	
	private double step;
	private BooleanProperty isOverflow;
	
	{
		isOverflow = new SimpleBooleanProperty();
	}
	
	public int getListSize() {
		return listSize;
	}
	public void setListSize(int listSize) {
		this.listSize = listSize;
		if (isInitialized)
			reset();
	}
	
	public double getItemWidth() {
		return itemWidth;
	}
	public void setItemWidth(double itemWidth) {
		this.itemWidth = itemWidth;
	}
	
	public Button getNodeFwd() {
		return nodeFwd;
	}
	public void setNodeFwd(Button nodeFwd) {
		this.nodeFwd = nodeFwd;
	}
	
	public Button getNodeBck() {
		return nodeBck;
	}
	public void setNodeBck(Button nodeBck) {
		this.nodeBck = nodeBck;
	}
	
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		if (isInitialized || getContent() == null)
			return;
		reset();
		layoutBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) -> reset());
		nodeFwd.visibleProperty().bind(hvalueProperty().greaterThanOrEqualTo(1.0).not().and(isOverflow));
		nodeBck.visibleProperty().bind(hvalueProperty().lessThanOrEqualTo(0.0).not());
		nodeFwd.setOnAction((ActionEvent event) -> scrollFwd());
		nodeBck.setOnAction((ActionEvent event) -> scrollBck());
		isInitialized = true;
	}
	
	private void scrollBck() {
		setHvalue(getHvalue() - step);
	}
	private void scrollFwd() {
		setHvalue(getHvalue() + step);
	}
	
	private void reset() {
		//System.out.println("reseting..");
		double r = getWidth() - WIDTH_OFFSET;
		int items = (int) (r / itemWidth);
		Pane content = (Pane) getContent();
		if (items >= listSize) {
			step = 0;
			content.setMinWidth(itemWidth * listSize);
			isOverflow.set(false);
		} else {
			step = 1 / ((double) (listSize - items));
			double w = r / ((double) items) * listSize;
			content.setMinWidth(w);
			isOverflow.set(w > r);
		}
		setHvalue(0);
	}
}
