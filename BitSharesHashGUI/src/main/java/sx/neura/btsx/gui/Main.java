package sx.neura.btsx.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sx.neura.bts.gui.view.stages.Splash;
import sx.neura.bts.json.CommandJson;
import sx.neura.btsx.gui.view.Screen;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.stages.Home;

public class Main extends Application {

	private static class BitsharesClientStarter {

		private final String command = "C:\\Program Files\\BitShares\\bin\\bitshares_client.exe";
		private final File workingDir = new File(
				"C:\\Program Files\\BitShares\\bin");
		private final String host = CommandJson.HOST;
		private final int port = CommandJson.PORT;

		private void inheritIO(final InputStream src, final PrintStream dest) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Scanner sc = new Scanner(src);
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						if (dest != null)
							dest.println(line);
					}
					sc.close();
				}
			}).start();
		}

		public void start() {
			HttpURLConnection connection = null;
			try {
				String url = String.format("http://%s:%d", host , port);
				connection = (HttpURLConnection) new URL(url).openConnection();
				connection.connect();
				connection.disconnect();
			} catch (Exception e) {
				ProcessBuilder builder = new ProcessBuilder(
						new String[] { command });
				if (workingDir != null)
					builder.directory(workingDir);
				PrintStream dest = System.out;
				try {
					bitsharesBackgroundProcess = builder.start();
					inheritIO(bitsharesBackgroundProcess.getInputStream(), dest);
					inheritIO(bitsharesBackgroundProcess.getErrorStream(), dest);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				boolean isConnected = false;
				do
					try {
						connection = (HttpURLConnection) new URL("http://localhost:"
								+ String.valueOf(port)).openConnection();
						connection.connect();
						isConnected = true;
					} catch (Exception e1) {
						System.out.println("waiting for connection ...");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e2) {
						}
					}
				while (!isConnected);
				connection.disconnect();
			}
		}
	}

	
	private static class PreloadingTask extends Task<Void>  {
		@Override
		protected Void call() throws InterruptedException {
			updateProgress(0.1, 1.0);
			updateMessage(String.format("%s..", t("AAXA.D.a5b2_4c9e_9641")));
			if (CommandJson.USE_CLIENT_STARTER)
				new BitsharesClientStarter().start();
			updateProgress(0.9, 1);
			updateMessage(String.format("%s..", t("AAXA.D.1e4c_46bb_bd63")));
			//DatabaseManager.getDatabase();
			return null;
		}
	}
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("ResourceBundle");
	
	private static Main instance;
	private static Process bitsharesBackgroundProcess;
	
	private Stage mainStage;
	private Screen screen;
	
	public Main() {
		logger.info("Starting main..");
		instance = this;
	}
	
	public static Main getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Task<Void> preloadingTask = new PreloadingTask();
		Splash splash = new Splash(preloadingTask);
		Scene scene = new Scene(loadPane(splash));
		scene.setFill(null);
		scene.getStylesheets().add("/styles/Splash.css");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
		mainStage = new Stage(StageStyle.DECORATED);
		mainStage.setTitle("Neura.sx");
		mainStage.getIcons().add(new Image("BitsharesXGui.png"));
		preloadingTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends Worker.State> observableValue,
					Worker.State oldState, Worker.State newState) {
				if (newState == Worker.State.SUCCEEDED) {
					//splash.getProgressBar().progressProperty().unbind();
					//splash.getProgessText().textProperty().unbind();
					screen = Home.getInstance();
					Scene scene = new Scene(loadPage(screen));
					scene.getStylesheets().add("/styles/Modena.css");
					scene.getStylesheets().add("/styles/Luna.css");
					scene.getStylesheets().add("/styles/Topsy.css");
					scene.getStylesheets().add("/styles/Turvx.css");
					scene.getStylesheets().add("/styles/Turvy.css");
					scene.getStylesheets().add("/styles/Flow.css");
					scene.getStylesheets().add("/styles/Chart.css");
					mainStage.setScene(scene);
					mainStage.show();
					primaryStage.hide();
					screen.loadData();
				}
			}
		});
		new Thread(preloadingTask).start();
	}
	
	@Override
	public void stop() {
		SearchBox.stop();
		screen.unloadData();
	}
	
	public static String t(String id) {
		return resources.getString(id);
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
	
	public static Pane loadPage(Screen page) {
		Pane pane = loadPane(page);
		page.setPane(pane);
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
		logger.debug(fxml);
		FXMLLoader loader = new FXMLLoader(root.getClass().getResource(fxml));
		loader.setResources(resources);
		return loader;
	}

    public Stage getMainStage() {
        return mainStage;
    }
	
}
