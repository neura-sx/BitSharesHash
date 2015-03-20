package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Main2;
import sx.neura.bts.util.Util;

public abstract class DisplayToggleList<T> extends Display {
	
	public interface Callback<T> {
		public void onChange(T item);
	}
	public interface Renderer<T> {
		public String render(T item);
	}
	
	@FXML
	private Pane boxUI;
	
	private List<T> items;
	private ToggleGroup group;
	
	private Callback<T> callback;
	
	public void setCallback(Callback<T> callback) {
		this.callback = callback;
	}
	
	public void setItems(List<T> items) {
		setItems(items, null);
	}
	
	public void setItems(List<T> items, Renderer<T> renderer) {
		this.items = items;
		this.group = new ToggleGroup();
		for (T item : items) {
			DisplayToggle toggle = createToggle();
			Pane pane = loadComponent(toggle);
			toggle.setToggleGroup(group);
			toggle.setText(renderer != null ? renderer.render(item) : item.toString());
			boxUI.getChildren().add(pane);
		}
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (callback != null)
					callback.onChange(items.get(group.getToggles().indexOf(newValue)));
			}
		});
		Util.manageToggleGroup(group);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
	public void selectToggle(int index) {
		Toggle toggle = group.getToggles().get(index);
		if (toggle != null)
			group.selectToggle(toggle);
	}
	
	public void selectItem(T item) {
		selectToggle(items.indexOf(item));
	}
	
	public Toggle getToggle(int index) {
		return group.getToggles().get(index);
	}
	
	public Integer getSelectedIndex() {
		return group.getToggles().indexOf(group.getSelectedToggle());
	}
	
	public T getSelectedItem() {
		int index = getSelectedIndex();
		if (index >= 0)
			return items.get(index);
		return null;
	}
	
	public void setEnabled(boolean enabled) {
		for (Node node : boxUI.getChildren())
			node.setDisable(!enabled);
	}
	
	public ReadOnlyObjectProperty<Toggle> selectedToggleProperty() {
		return group.selectedToggleProperty();
	}
	
	protected abstract DisplayToggle createToggle();
	
	private static Pane loadComponent(Initializable root) {
		return Main2.loadComponent(root);
	}

}
