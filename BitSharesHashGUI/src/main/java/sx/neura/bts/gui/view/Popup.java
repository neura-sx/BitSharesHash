package sx.neura.bts.gui.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import sx.neura.bts.gui.Main2;

public abstract class Popup extends Screen {
	
	private static final int EASING_TIME = 400;
	
	private BooleanProperty isOverlay;
	private Pane overlay;
	private Pane parent;
	protected Module module;
	
	public interface Callback {
		public void onConfirm();
		public void onCancel();
	}
	
	@FXML
	protected void onCancel() {
		hideItself();
	}
	
	public void show(Pane parent, Module module) {
		show(parent, module, null);
	}
	
	public void show(Pane parent, Module module, BooleanProperty isOverlay) {
		this.parent = parent;
		this.module = module;
		this.isOverlay = isOverlay;
		
		Main2.loadScreen(this);

		overlay = new StackPane(pane);
		AnchorPane.setLeftAnchor(overlay, 0.0);
		AnchorPane.setRightAnchor(overlay, 0.0);
		AnchorPane.setTopAnchor(overlay, 0.0);
		AnchorPane.setBottomAnchor(overlay, 0.0);
		overlay.getStyleClass().add("sx-popup-overlay");
		overlay.setOpacity(0);
		overlay.setVisible(true);
		parent.getChildren().add(overlay);
		
		if (isOverlay != null)
			isOverlay.set(true);
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(EASING_TIME),
				(ActionEvent t) -> onCreationComplete(),
				new KeyValue(overlay.opacityProperty(),
						1, Interpolator.EASE_BOTH)));
		timeline.play();
	}
	
	protected void onCreationComplete() {
		
	}
	
	protected void hideItself() {
		hideItself(null);
	}

	protected void hideItself(Runnable runnable) {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(EASING_TIME),
				(ActionEvent t) -> {
					overlay.setVisible(false);
					overlay.getChildren().clear();
					parent.getChildren().remove(overlay);
					module.isProcessing().set(false);
					if (isOverlay != null)
						isOverlay.set(false);
					if (runnable != null)
						runnable.run();
				},
				new KeyValue(overlay.opacityProperty(),
					0, Interpolator.EASE_BOTH)));
		timeline.play();
	}
	
}
