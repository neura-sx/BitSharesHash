package sx.neura.btsx.gui.view;

import java.net.URL;
import java.util.ResourceBundle;

import sx.neura.btsx.gui.Main;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class Popup extends Screen {
	
	private static final boolean CACHING = false;
	
	@FXML
	protected Label titleUI;
	
	private String title;
	private Pane dimmer;
	private Pane parent;
	
	@FXML
	protected void onCancel(ActionEvent event) {
		hideItself();
	}
	
	public void setTitle(String title) {
		this.title = title;
		applyTitle();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		applyTitle();
	}
	
	private void applyTitle() {
		if (titleUI != null && title != null)
			titleUI.setText(title.toUpperCase());
	}
	
	public void show(Pane parent) {
		this.parent = parent;
		Main.loadPage(this);
		dimmer = Main.loadPane(new ModalDimmer());
		dimmer.getChildren().add(pane);
		dimmer.setOpacity(0);
		dimmer.setVisible(true);
		if (CACHING)
			dimmer.setCache(true);
		parent.getChildren().add(dimmer);
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300),
				(ActionEvent t) -> onCreationComplete(),
				new KeyValue(dimmer.opacityProperty(),
						1, Interpolator.EASE_BOTH)));
		timeline.play();
	}
	
	protected void onCreationComplete() {
		if (CACHING)
			dimmer.setCache(false);
	}

	protected void hideItself() {
		if (CACHING)
			dimmer.setCache(true);
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300),
				(ActionEvent t) -> {
					if (CACHING)
						dimmer.setCache(false);
					dimmer.setVisible(false);
					dimmer.getChildren().clear();
					parent.getChildren().remove(dimmer);
				},
				new KeyValue(dimmer.opacityProperty(),
					0, Interpolator.EASE_BOTH)));
		timeline.play();
	}
	
}
