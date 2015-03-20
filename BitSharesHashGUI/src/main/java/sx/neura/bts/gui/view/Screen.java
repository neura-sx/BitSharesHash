package sx.neura.bts.gui.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sx.neura.bts.gui.Main;
import sx.neura.bts.gui.view.popups.FeatureNotImplemented;
import sx.neura.bts.gui.view.popups.SystemException;
import sx.neura.bts.gui.view.popups.UserException;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;

public abstract class Screen implements Initializable {
	
	protected Pane pane;
	protected boolean isDataLoaded;
	protected BooleanProperty isOverlay;
	protected Module module;
	
	public void setPane(Pane pane) {
		this.pane = pane;
	}
	public Pane getPane() {
		return pane;
	}
	
	public void setModule(Module module) {
		this.module = module;
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	protected Pane loadScreen(Screen screen) {
		return Main.loadScreen(screen);
	}
	
	public void loadData() {
		if (!isDataLoaded) {
			isDataLoaded = true;
			System.out.println(String.format("#### %s: %s", getClass().getSimpleName(), getName()));
		}
	}
	
	public void reloadData() {
	}
	
	public void unloadData() {
	}
	
	public void ping() {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isDataLoaded = false;
		isOverlay = new SimpleBooleanProperty(false);
	}
	
	protected void featureNotImplemented() {
		new FeatureNotImplemented().show(getModalPane(), module, isOverlay);
	}
	
	protected void userException(String message) {
		new UserException(message).show(getModalPane(), module, isOverlay);
	}
	protected void userException(BTSUserException e) {
		new UserException(e).show(getModalPane(), module, isOverlay);
	}
	
	protected void systemException(String message) {
		new SystemException(message).show(getModalPane(), module, isOverlay);
	}
	public void systemException(BTSSystemException e) {
		new SystemException(e).show(getModalPane(), module, isOverlay);
	}

	protected Pane getModalPane() {
		return pane;
	}
	
	protected static void runWorkerThread(final Runnable worker, final Runnable responder) {
		new Thread(() -> {
				worker.run();
				Platform.runLater(responder);
		}).start();
	}
	
	protected static void animateProgress(double progress, Node node, Runnable runnable) {
		animateProgress(progress, 1000, node, runnable);
	}
	
	protected static void animateProgress(double progress, int duration, Node node, Runnable runnable) {
		new Timeline(
				new KeyFrame(Duration.millis(duration), (ActionEvent event) -> runnable.run(),
				new KeyValue(node.scaleXProperty(), progress, Interpolator.EASE_BOTH))).play();
	}
	
	protected static String t(String id) {
		return Main.t(id);
	}
	
	protected static boolean c(String s, String i) {
		return Util.c(s, i);
	}
}
