package sx.neura.bts.gui.view.stages;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import sx.neura.bts.Version;
import sx.neura.bts.gui.view.components.LayerPane;

public class Splash implements Initializable {
	
//	private static Splash instance;
//	public static Splash getInstance() {
//		if (!isInstance())
//			instance = new Splash();
//		return instance;
//	}
//	public static boolean isInstance() {
//		return (instance != null);
//	}
//	private Splash() {
//	}
	
	@FXML
	private Label versionUI;
	
	@FXML
	private Node introUI;
	
	@FXML
	private LayerPane introLogoUI;
	@FXML
	private LayerPane introTextUI;
	
	@FXML
	private Label progressTextUI;
	@FXML
	private Label errorUI;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		versionUI.setText(String.format("%s %s", "version", Version.id));
	}
	
	public Node getIntro() {
		return introUI;
	}
	public LayerPane getIntroLogo() {
		return introLogoUI;
	}
	public LayerPane getIntroText() {
		return introTextUI;
	}
	
	public void setError(String error) {
		errorUI.setText(error);
	}
	
//	public ProgressBar getProgressBar() {
//		return progressBarUI;
//	}
//	
	public Label getProgressText() {
		return progressTextUI;
	}
	
	@FXML
	private void onExit() {
		Platform.exit();
	}

}
