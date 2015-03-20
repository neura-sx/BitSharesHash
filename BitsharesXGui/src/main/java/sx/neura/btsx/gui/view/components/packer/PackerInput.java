package sx.neura.btsx.gui.view.components.packer;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public class PackerInput extends Packer {
	
	@FXML
	private TextField textUI;
	
	public interface Responder {
		public void onChange();
	}
	
	public void setText(String text) {
		textUI.setText(text);
	}
	public String getText() {
		return textUI.getText();
	}
	
	public void setPrompt(String prompt) {
		textUI.setPromptText(prompt);
	}
	public String getPrompt() {
		return textUI.getPromptText();
	}
	
	public void setAlignment(Pos pos) {
		textUI.setAlignment(pos);
	}
	public Pos getAlignment() {
		return textUI.getAlignment();
	}
	
	public void setEnabled(boolean enabled) {
		textUI.setDisable(!enabled);
	}

	public StringProperty getTextProperty() {
		return textUI.textProperty();
	}
	
	public void setResponder(final Responder responder) {
//		textUI.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				if (responder != null)
//					responder.onChange();
//			}
//		});
		textUI.textProperty().addListener(new ChangeListener<String>() {
			@Override 
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (responder != null)
					responder.onChange();
			}
		});
	}

}
