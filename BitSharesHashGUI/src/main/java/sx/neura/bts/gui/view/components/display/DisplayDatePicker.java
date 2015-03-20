package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public abstract class DisplayDatePicker extends Display {
	
	private final String pattern = "dd-MM-yyyy";
	
	public interface Responder {
		public void onChange(LocalDate date);
	}
	
	@FXML
	private DatePicker textUI;
	
	
	public DatePicker getTextUI() {
		return textUI;
	}
	
	public LocalDate getItem() {
		return textUI.getValue();
	}
	public void setItem(LocalDate item) {
		textUI.setValue(item);
	}
	
	public void setPrompt(String prompt) {
		textUI.setPromptText(prompt);
	}
	public String getPrompt() {
		return textUI.getPromptText();
	}
	
	public ObjectProperty<LocalDate> valueProperty() {
		return textUI.valueProperty();
	}
	
	public void setEnabled(boolean enabled) {
		textUI.setDisable(!enabled);
	}
		
	public void setResponder(final Responder responder) {
		textUI.setOnAction((ActionEvent event) -> responder.onChange(textUI.getValue()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
        textUI.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
			@Override
			public String toString(LocalDate date) {
				if (date != null)
					return dateFormatter.format(date);
				return "";
			}
			@Override
			public LocalDate fromString(String s) {
				if (s != null && !s.isEmpty())
					return LocalDate.parse(s, dateFormatter);
				return null;
			}
		});
	}
	
}
