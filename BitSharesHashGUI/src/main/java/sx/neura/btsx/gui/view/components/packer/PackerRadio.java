package sx.neura.btsx.gui.view.components.packer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public abstract class PackerRadio extends Packer {
	
	public interface Callback {
		public void onChange(Toggle oldValue, Toggle newValue);
	}
	
	@FXML
	private Pane boxUI;
	
	private ToggleGroup toggleGroup;
	private Callback callback;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		toggleGroup = new ToggleGroup();
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	public void setRadioButtons(RadioButton[] radioButtons) {
		for (RadioButton radioButton : radioButtons) {
			radioButton.setFocusTraversable(false);
			radioButton.setToggleGroup(toggleGroup);
		}
		boxUI.getChildren().addAll(radioButtons);
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (callback != null)
					callback.onChange(oldValue, newValue);
			}
		});
	}
	
	public void selectToggle(Toggle toggle) {
		toggleGroup.selectToggle(toggle);
	}
	
	public Toggle getSelectedToggle() {
		return toggleGroup.getSelectedToggle();
	}
	
	public void setEnabled(boolean enabled) {
		boxUI.setDisable(!enabled);
	}
	
	public ReadOnlyObjectProperty<Toggle> getSelectedToggleProperty() {
		return toggleGroup.selectedToggleProperty();
	}

}
