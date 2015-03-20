package sx.neura.btsx.gui.view.components.packer;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PackerChoice<T> extends Packer {
	
	@FXML
	private ComboBox<T> textUI;
	
	private Renderer<T> renderer;
	
	public interface Renderer<T> {
		public String render(T item);
	}
	
	public interface Responder<T> {
		public void onChange(T oldValue, T newValue);
	}
	
	public void setList(List<T> list) {
		//textUI.setItems(list);
		textUI.getItems().setAll(list);
	}
	
	public List<T> getList() {
		return textUI.getItems();
	}
	
	public void setRenderer(Renderer<T> renderer) {
		this.renderer = renderer;
		textUI.setButtonCell(new PackerListCell());
		textUI.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
			@Override
			public ListCell<T> call(ListView<T> param) {
				return new PackerListCell();
			}
		});
	}
	
	public void setResponder(final Responder<T> responder) {
		textUI.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
			@Override
			public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
				responder.onChange(oldValue, newValue);
			}
		});
	}
	
	public void setEnabled(boolean enabled) {
		textUI.setDisable(!enabled);
	}
	
	public T getValue() {
		return textUI.getValue();
	}
	
	public void setValue(T value) {
		textUI.setValue(value);
	}
	
	public ObjectProperty<T> getValueProperty() {
		return textUI.valueProperty();
	}
	
	private class PackerListCell extends ListCell<T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null)
            	setText(renderer.render(item));
        }
    }

}
