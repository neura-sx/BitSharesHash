package sx.neura.btsx.gui.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.btsx.gui.view.components.NavigationListView;

import com.fxexperience.javafx.animation.ShakeTransition;

public abstract class Module extends Screen {
	
	@FXML
	protected Label processingUI;
	
	@FXML
	protected LayerPane introUI;
	@FXML
	protected LayerPane workspaceUI;
	@FXML
	protected LayerPane screensUI;

	@FXML
	protected PasswordField passwordUI;
		
	@FXML
	protected Pane footerUI;
	public Pane getFooter() {
		return footerUI;
	}
	
	protected List<Screen> screens;
	protected int index;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		index = 0;
		passwordUI.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER))
					onUnlock();
			}
		});
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			processingUI.setText(String.format("Loading %s..", getName()));
			Platform.runLater(() -> {
				loadStaticData();
				initialize();
				addEventListeners();
				introUI.setIndex(1);
	        });
		}
		super.loadData();
	}
	
	protected void showScreen(NavigationListView navigationUI) {
		showScreen(navigationUI.getSelectionModel().getSelectedItem());
	}
	
	protected void showScreen(Screen screen) {
		int index = screens.indexOf(screen);
		screens.get(this.index).unloadData();
		this.index = index;
 		screensUI.setIndex(index, () -> {
 			screen.loadData();
 		});
	}
	
	protected void initialize(int index) {
		this.index = index;
 		screensUI.setIndex(index);
 		screens.get(index).loadData();
	}
	
	protected void addListenerForNavigation(NavigationListView navigationUI) {
		navigationUI.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Screen>() {
			@Override
			public void changed(ObservableValue<? extends Screen> observable,
					Screen oldValue, Screen newValue) {
				showScreen(newValue);
			}
		});
	}
	
	protected abstract void initialize();
	protected abstract void addEventListeners();
	protected abstract void loadStaticData();
	protected abstract boolean unlock(String password);
	protected abstract void lock();
	protected abstract String getHack();
	
	@FXML
	public void onLock() {
		processingUI.setText(String.format("Locking %s..", getName()));
		workspaceUI.setIndex(0, () -> {
			lock();
			introUI.setIndex(1);
		});
	}
	
    private void onUnlock() {
		processingUI.setText(String.format("Unlocking %s..", getName()));
		introUI.setIndex(0, () -> {
			if (unlock(passwordUI.getText())) {
				passwordUI.setText("");
				workspaceUI.setIndex(1);
			} else {
				introUI.setIndex(1);
				ShakeTransition shake = new ShakeTransition(passwordUI);
				shake.setOnFinished((ActionEvent event) -> passwordUI.setText(""));
				shake.play();
			}
		});
	}
	
	@FXML
	protected void onSwitchWallet() {
		featureNotImplemented();
	}
	
	@FXML
	protected void onExit() {
		//exit();
	}
	
	@FXML
	protected void onAbout() {
		featureNotImplemented();
	}
	
	@FXML
	protected void onHack(MouseEvent event) {
		if (event.getClickCount() > 1) {
			passwordUI.setText(getHack());
			onUnlock();
		}
	}
}
