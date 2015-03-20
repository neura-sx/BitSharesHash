package test.neura.bts.gui.view;

import sx.neura.btsx.gui.Main;
import sx.neura.btsx.gui.view.Screen;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class Dialog extends Screen {
	
	private Stage stage;
	protected boolean response;
	
	protected abstract String getTitle();
	
	public void show() {
        stage = new Stage();
        stage.setTitle(getTitle());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.getInstance().getMainStage());
        stage.setScene(new Scene(Main.loadPage(this)));
        initialize();
        stage.show();
    }
	
	public boolean showAndWait() {
    	response = false;
        stage = new Stage();
        stage.setTitle(getTitle());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.getInstance().getMainStage());
        stage.setScene(new Scene(Main.loadPage(this)));
        initialize();
        stage.showAndWait();
        return response;
    }
	
	protected void hide() {
        stage.close();
        stage = null;
    }

	protected void initialize() {
		
	}
}
