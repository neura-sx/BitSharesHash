package sx.neura.bts.gui;

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

import sx.neura.bts.gui.view.Screen;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.SearchBox;
import sx.neura.bts.gui.view.modules.Module_Bts;
import sx.neura.bts.gui.view.stages.Splash;
import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.LabeledOutputStream;

public class Main extends Application {
	
	private static class ConnectionTask extends Task<Void>  {
		
		private final String COMMAND = "bitshares_client.exe";
		
		@Override
		protected Void call() throws IOException {
			final String url = String.format("http://%s:%d", CommandJson.HOST , CommandJson.PORT);
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) new URL(url).openConnection();
				System.out.println("attempting connection..");
				updateMessage("attempting connection..");
				connection.connect();
				System.out.println("connection successful, bts process is already running");
				connection.disconnect();
			} catch (Exception e) {
				System.out.println("connection failed, trying to start bts process..");
				updateMessage("connection failed, trying to start bts process..");
				String path = Main.class.getResource(String.format("/%s", COMMAND)).getPath();
				System.out.println(path);
				ProcessBuilder builder = new ProcessBuilder(new String[] { path,
						"--server",
						"--rpcuser", CommandJson.USER_NAME,
						"--rpcpassword", CommandJson.PASSWORD,
						"--httpport", String.valueOf(CommandJson.PORT) });
				bitsharesBackgroundProcess = builder.start();
				redirectInputStream(bitsharesBackgroundProcess.getInputStream());
				redirectInputStream(bitsharesBackgroundProcess.getErrorStream());
				boolean isConnected = false;
				do
					try {
						connection = (HttpURLConnection) new URL(url).openConnection();
						connection.connect();
						isConnected = true;
					} catch (Exception e1) {
						System.out.println("waiting for connection..");
						updateMessage("waiting for connection..");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e2) {
						}
					}
				while (!isConnected);
				System.out.println("connection successful after starting bts process");
				connection.disconnect();
			}
			return null;
		}
	}
	
	private static class PreloadingTask extends Task<Void>  {
		@Override
		protected Void call() throws InterruptedException {
			//updateProgress(0.1, 1.0);
			//updateMessage(String.format("%s..", t("AAXA.D.a5b2_4c9e_9641")));
			
			//DatabaseManager.getDatabase();
			
			if (CommandJson.LIVE_MODE) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					
				}
			}
			return null;
		}
	}
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("ResourceBundle");
	
	private static Process bitsharesBackgroundProcess;
	
	private static Stage mainStage;
	private static Screen screen;
	private static PrintStream printStream;
	
	public Main() {
		logger.info("Starting main..");
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private static void redirectInputStream(InputStream is) {
		new Thread(() -> {
			Scanner sc = new Scanner(is);
			while (sc.hasNextLine())
				printStream.println(sc.nextLine());
			sc.close();
		}).start();
	}
	
	@Override
	public void start(Stage primaryStage) {
		printStream = System.out;
		Image icon = new Image("BitsharesHash.png");
		Task<Void> connectionTask = new ConnectionTask();
		Task<Void> preloadingTask = new PreloadingTask();
		Splash splash = new Splash();
		Scene scene = new Scene(loadPane(splash));
		scene.setFill(null);
		scene.getStylesheets().add("/styles/Splash.css");
		primaryStage.getIcons().add(icon);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
		mainStage = new Stage(StageStyle.UNDECORATED);
		mainStage.setMinWidth(760.0);
		mainStage.setMinHeight(720.0);
		mainStage.setMaxWidth(1000.0);
		mainStage.setTitle("Neura.sx");
		mainStage.getIcons().add(icon);
		
		splash.getProgressText().textProperty().bind(connectionTask.messageProperty());
		connectionTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends Worker.State> observableValue,
					Worker.State oldState, Worker.State newState) {
				if (newState == Worker.State.SUCCEEDED) {
					splash.getProgressText().textProperty().unbind();
					try {
						Model.getInstance().openWallet();
					} catch (BTSSystemException e) {
						splash.getIntro().setVisible(false);
						if (e.getError().getCommand() != null)
							splash.setError("Fatal error: Failed to create or open wallet");
						else
							splash.setError(String.format("%s\n%s", e.getError().getMessage(),
									"Make sure the BitShares client is running and JSON port is configured properly"));
						return;
					}
					new Thread(preloadingTask).start();
				}
			}
		});
		
		preloadingTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends Worker.State> observableValue,
					Worker.State oldState, Worker.State newState) {
				if (newState == Worker.State.SUCCEEDED) {
					screen = Module_Bts.getInstance();
					Scene scene = new Scene(loadScreen(screen));
					scene.getStylesheets().add("/styles/_Dark_Icons.css");
					scene.getStylesheets().add("/styles/_Dark_Hash.css");
					scene.getStylesheets().add("/styles/_Dark_Pop.css");
					scene.getStylesheets().add("/styles/_Dark_Wiz.css");
					scene.getStylesheets().add("/styles/_Dark_Chart.css");
					scene.getStylesheets().add("/styles/_Dark_ShadesOfGray.css");
					loadColorSet(scene, Model.getInstance().getColorSetName());
					mainStage.setScene(scene);
					mainStage.show();
					primaryStage.hide();
					screen.loadData();
				}
			}
		});
		
		if (CommandJson.LIVE_MODE) {
			splash.getIntroText().setIndex(1, () -> {
				splash.getIntroLogo().setIndex(1, () -> {
					new Thread(connectionTask).start();
				});
			});
		} else {
			new Thread(preloadingTask).start();
		}
	}
	
	@Override
	public void stop() {
		LabeledOutputStream.stop();
		SearchBox.stop();
		Pagination.stop();
		if (screen != null)
			screen.unloadData();
		if (bitsharesBackgroundProcess != null)
			bitsharesBackgroundProcess.destroy();
	}
	
	public static void setPrintStream(PrintStream ps) {
		printStream = ps;
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
	
	private static void loadColorSet(Scene scene, String name) {
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
		logger.debug(fxml);
		FXMLLoader loader = new FXMLLoader(root.getClass().getResource(fxml));
		loader.setResources(resources);
		return loader;
	}

}
