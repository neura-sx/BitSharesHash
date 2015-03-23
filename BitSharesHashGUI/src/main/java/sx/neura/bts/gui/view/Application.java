package sx.neura.bts.gui.view;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sx.neura.bts.Version;

public abstract class Application extends javafx.application.Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("ResourceBundle");
	
	protected static Stage mainStage;
	protected static Screen screen;
	
	public Application() {
		logger.info(String.format("Starting application.. %s (%s)", Version.id, Version.name));
	}
	
	public static String t(String id) {
		return resources.getString(id);
	}
	
	public static void loadColorSet(String name) {
		loadColorSet(mainStage.getScene(), name);
	}
	public static void unloadColorSet(String name) {
		unloadColorSet(mainStage.getScene(), name);
	}
	
	protected static void loadColorSet(Scene scene, String name) {
		scene.getStylesheets().add(String.format("/styles/colors/%s.css", name));
	}
	private static void unloadColorSet(Scene scene, String name) {
		scene.getStylesheets().remove(String.format("/styles/colors/%s.css", name));
	}
	
	public static Pane loadScreen(Screen screen) {
		Pane pane = loadPane(screen);
		screen.setPane(pane);
		return pane;
	}
	
	public static Pane loadPane(Initializable root) {
		FXMLLoader loader = getFXMLLoader(root);
		loader.setController(root);
		Pane pane = null;
		try {
			pane = (Pane) loader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		return pane;
	}
	
	public static Pane loadComponent(Initializable root) {
		FXMLLoader loader = getFXMLLoader(root);
		loader.setRoot(root);
		loader.setController(root);
		Pane pane = null;
		try {
			pane = (Pane) loader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		return pane;
	}
	
	private static FXMLLoader getFXMLLoader(Initializable root) {
		String fxml = String.format("/%s.fxml", root.getClass().getName()
				.replaceAll("\\.", "/"));
		FXMLLoader loader = new FXMLLoader(root.getClass().getResource(fxml));
		loader.setResources(resources);
		return loader;
	}
}
