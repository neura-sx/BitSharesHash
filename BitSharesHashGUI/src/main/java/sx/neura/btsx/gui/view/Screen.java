package sx.neura.btsx.gui.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;
import sx.neura.btsx.gui.Main;
import sx.neura.btsx.gui.view.popups.FeatureNotImplemented;
import sx.neura.btsx.gui.view.popups.SystemException;
import sx.neura.btsx.gui.view.popups.UserException;

public abstract class Screen implements Initializable {
	
	protected Pane pane;
	protected boolean isDataLoaded;
	
	public void setPane(Pane pane) {
		this.pane = pane;
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	protected Pane loadPage(Screen page) {
		return Main.loadPage(page);
	}
	
	protected Pane loadPageAsChild(Screen page) {
		Pane pane = Main.loadPane(page);
		page.setPane(this.pane);
		return pane;
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
	}
	
	
	public void featureNotImplemented() {
		new FeatureNotImplemented().show(pane);
	}
	
	public void userException(String message) {
		new UserException(message).show(pane);
	}
	public void userException(BTSUserException e) {
		new UserException(e).show(pane);
	}
	
	public void systemException(String message) {
		new SystemException(message).show(pane);
	}
	public void systemException(BTSSystemException e) {
		new SystemException(e).show(pane);
	}
	
	protected static void runWorkerThread(final Runnable worker, final Runnable responder) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				worker.run();
				Platform.runLater(responder);
			}
		}).start();
	}
	
	protected static String t(String id) {
		return Main.t(id);
	}
	
	protected static boolean c(String s, String i) {
		return Util.c(s, i);
	}
}
