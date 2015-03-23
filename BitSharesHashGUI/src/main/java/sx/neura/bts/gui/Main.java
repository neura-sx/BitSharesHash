package sx.neura.bts.gui;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sx.neura.bts.gui.view.Application;
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
		
		private String path;
		
		@Override
		protected Void call() throws Exception {
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
				//URL u = Main.class.getResource(String.format("/%s", COMMAND));
				File baseFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
				if (baseFile.isFile())
					baseFile = baseFile.getParentFile();
				path = String.format("%s\\%s", baseFile.getPath(), COMMAND);
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
				int count = 0;
				do
					try {
						connection = (HttpURLConnection) new URL(url).openConnection();
						connection.connect();
						isConnected = true;
					} catch (Exception e1) {
						count++;
						System.out.println("waiting for connection..");
						updateMessage(count % 2 == 0 ? "waiting for connection.." : "");
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
	
	private static Process bitsharesBackgroundProcess;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static void redirectInputStream(InputStream is) {
		new Thread(() -> {
			Scanner sc = new Scanner(is);
			while (sc.hasNextLine())
				Model.getInstance().getPrintStream().println(sc.nextLine());
			sc.close();
		}).start();
	}
	
	@Override
	public void start(Stage primaryStage) {
		Model.getInstance().setPrintStream(System.out);
		Image icon = new Image("BitsharesHash.png");
		ConnectionTask connectionTask = new ConnectionTask();
		PreloadingTask preloadingTask = new PreloadingTask();
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
						splash.setError(e.getError().getCommand() != null ? 
								String.format("%s: %s", "Fatal error", "Failed to create or open wallet") : e.getError().getMessage());
						return;
					}
					new Thread(preloadingTask).start();
				} else if (newState == Worker.State.FAILED) {
					splash.getIntro().setVisible(false);
					splash.setError(String.format("%s\n%s", "Failed to start bts background process", connectionTask.path));
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
					loadColorSet(scene, Model.getInstance().getColorSet());
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

}
