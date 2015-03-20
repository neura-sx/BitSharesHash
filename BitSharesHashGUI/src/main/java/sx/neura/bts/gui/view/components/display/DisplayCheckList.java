package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Main;

public abstract class DisplayCheckList<T> extends Display {
	
	public interface Callback<T> {
		public void onChange(T item);
	}
	public interface Renderer<T> {
		public String render(T item);
	}
	
	@FXML
	private Pane boxUI;
	
	private List<T> items;
	private List<DisplayCheck> checks;
	
	private Callback<T> callback;
	
	public void setCallback(Callback<T> callback) {
		this.callback = callback;
	}
	
	public void setItems(List<T> items) {
		setItems(items, null);
	}
	
	public void setItems(List<T> items, Renderer<T> renderer) {
		this.items = items;
		this.checks = new ArrayList<DisplayCheck>();
		for (T item : items) {
			DisplayCheck check = createCheck();
			checks.add(check);
			Pane pane = loadComponent(check);
			check.setText(renderer != null ? renderer.render(item) : item.toString());
			check.setRunnable(() -> {
				if (callback != null)
					callback.onChange(items.get(checks.indexOf(check)));
			});
			boxUI.getChildren().add(pane);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
	public void selectCheck(int index, boolean selected) {
		DisplayCheck check = checks.get(index);
		if (check != null)
			check.setSelected(selected);
	}
	
	public void selectItem(T item, boolean selected) {
		selectCheck(items.indexOf(item), selected);
	}
	
	public boolean isSelectItem(T item) {
		int index = items.indexOf(item);
		if (index >= 0)
			return checks.get(index).isSelected();
		return false;
	}
	
	public List<T> getSelectedItems() {
		List<T> items = new ArrayList<T>();
		for (DisplayCheck check : checks)
			if (check.isSelected())
				items.add(this.items.get(checks.indexOf(check)));
		return items;
	}
	
	public DisplayCheck getCheck(int index) {
		return checks.get(index);
	}
	
	public void setEnabled(boolean enabled) {
		for (Node node : boxUI.getChildren())
			node.setDisable(!enabled);
	}
	
	protected abstract DisplayCheck createCheck();
	
	private static Pane loadComponent(Initializable root) {
		return Main.loadComponent(root);
	}

}
