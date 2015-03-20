package sx.neura.bts.gui.view.components;

import java.util.ArrayList;
import java.util.List;

import sx.neura.bts.util.Util;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MenuPane extends AnchorPane {
	
	public enum Mode {
		OFF,
		ANIMATE_CREATION,
		ANIMATE_TOGGLE,
		ANIMATE_BOTH,
	}
	
	private boolean initialized;
	private List<ToggleButton> toggles;
	private List<ToggleButton> togglesSorted;
	
	private double spacing;
	private int duration;
	private Mode mode;
	
	public double getSpacing() {
		return spacing;
	}
	public void setSpacing(double spacing) {
		this.spacing = spacing;
	}
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void setIndex(int index) {
		if (index >= toggles.size())
			return;
		ToggleButton toggle = toggles.get(index);
		if (toggle.isSelected())
			return;
		toggle.setSelected(true);
		setToggle(toggle);
	}
	
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		if (initialized || getChildren().size() == 0)
			return;
		
		ToggleGroup group = new ToggleGroup();
		toggles = new ArrayList<ToggleButton>();
		togglesSorted = new ArrayList<ToggleButton>();
		for (Node node : getChildren()) {
			if (node.isManaged() && node instanceof ToggleButton) {
				ToggleButton toggle = (ToggleButton) node;
				toggle.setToggleGroup(group);
				toggles.add(toggle);
				togglesSorted.add(toggle);
			}
		}
		setIndex(0);
		Util.manageToggleGroup(group);
		group.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					if (newValue != null)
						setToggle((ToggleButton) newValue);
		});
		initialized = true;
	}
	
	private void setToggle(ToggleButton toggle) {
		toggle.toFront();
		togglesSorted.remove(toggle);
		togglesSorted.add(0, toggle);
		
		double offset = 0;
		for (ToggleButton t : togglesSorted) {
			if (duration > 0 
					&& ((mode.equals(Mode.ANIMATE_TOGGLE) && initialized) || mode.equals(Mode.ANIMATE_CREATION) || mode.equals(Mode.ANIMATE_BOTH)))
				new Timeline(
						new KeyFrame(Duration.millis(duration), (ActionEvent event) -> {},
						new KeyValue(t.translateXProperty(), offset, Interpolator.EASE_BOTH))).play();
			else
				t.setTranslateX(offset);
			offset += t.getWidth() + spacing;
		}
	}
}
