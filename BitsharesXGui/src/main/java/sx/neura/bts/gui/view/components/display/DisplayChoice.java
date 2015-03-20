package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public abstract class DisplayChoice<T> extends Display {
	
	public interface Renderer<T> {
		public String render(T item);
	}
	
	public interface Responder<T> {
		public void onChange(T oldValue, T newValue);
	}
	
	@FXML
	private ComboBox<T> textUI;
	
	private Renderer<T> buttonRenderer;
	private Renderer<T> expandedRenderer;
	private boolean firstItemEmpty;
	
	public ComboBox<T> getTextUI() {
		return textUI;
	}
	
	public void setList(List<T> list) {
		textUI.getItems().setAll(list);
	}
	public List<T> getList() {
		return textUI.getItems();
	}
	
	public T getItem() {
		return textUI.getValue();
	}
	public void setItem(T item) {
		textUI.setValue(item);
	}
	
	public void setPrompt(String prompt) {
		textUI.setPromptText(prompt);
	}
	public String getPrompt() {
		return textUI.getPromptText();
	}
	
	public ObjectProperty<T> valueProperty() {
		return textUI.valueProperty();
	}
	
	public void setEnabled(boolean enabled) {
		textUI.setDisable(!enabled);
	}
	
	public void setRenderer(Renderer<T> renderer) {
		setRenderers(renderer, renderer);
	}
	
	public void setRenderers(Renderer<T> buttonRenderer, Renderer<T> expandedRenderer) {
		this.buttonRenderer = buttonRenderer;
		this.expandedRenderer = expandedRenderer;
		textUI.setButtonCell(new ButtonCell());
		textUI.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
			@Override
			public ListCell<T> call(ListView<T> param) {
				return new ExpandedCell();
			}
		});
	}
	
	public void setResponder(final Responder<T> responder) {
		textUI.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
			if (firstItemEmpty && newValue != null && textUI.getItems().indexOf(newValue) == 0) {
				Platform.runLater(() -> textUI.getSelectionModel().clearSelection());
				return;
			}
			responder.onChange(oldValue, newValue);
		});
	}
	
	public void isFirstItemEmpty(boolean firstItemEmpty) {
		this.firstItemEmpty = firstItemEmpty;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
	private class ButtonCell extends ListCell<T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
            	setId(null);
                setText(buttonRenderer.render(item));
            } else {
            	setId("sx-text-prompt");
            }
        }
    }
	
	private class ExpandedCell extends ListCell<T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty)
            	setText(expandedRenderer.render(item));
            
        }
    }
}
