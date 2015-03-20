package sx.neura.btsx.gui.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class ModalDimmer implements Initializable {
	
	@FXML
	private void onMouseClicked(MouseEvent event) {
		event.consume();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
