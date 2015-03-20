package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public abstract class DisplayInput extends Display {
	
	public interface Responder {
		public void onChange();
	}
	
	@FXML
	private TextField textUI;
	
	private Pos alignment;
	
	public Node getTextUI() {
		return textUI;
	}
	
	public String getText() {
		return textUI.getText();
	}
	public void setText(String text) {
		textUI.setText(text);
	}
	
	public void setPrompt(String prompt) {
		textUI.setPromptText(prompt);
	}
	public String getPrompt() {
		return textUI.getPromptText();
	}
	
	public void setAlignment(Pos alignment) {
		this.alignment = alignment;
		manageBindings();
	}
	public Pos getAlignment() {
		return alignment;
	}
	
	public StringProperty textProperty() {
		return textUI.textProperty();
	}
	
	public void setEnabled(boolean enabled) {
		textUI.setDisable(!enabled);
	}
	
	public void setResponder(final Responder responder) {
		textUI.textProperty().addListener(new ChangeListener<String>() {
			@Override 
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (responder != null && textUI.isFocused())
					responder.onChange();
			}
		});
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		manageBindings();
	}
	
	private void manageBindings() {
		if (textUI != null && alignment != null) {
			textUI.alignmentProperty().unbind();
			textUI.alignmentProperty().bind(Bindings.createObjectBinding(new Callable<Pos>() {
				@Override
				public Pos call() throws Exception {
					return (textUI.getText().isEmpty() ? Pos.CENTER_LEFT : alignment);
				}
			}, textUI.textProperty()));
		}
	}
}
