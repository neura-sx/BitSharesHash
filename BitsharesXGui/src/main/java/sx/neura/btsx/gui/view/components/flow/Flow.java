package sx.neura.btsx.gui.view.components.flow;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import sx.neura.btsx.gui.Main;

public class Flow<T> extends FlowPane {
	private List<T> items;
	private CellFactory<T> cellFactory;
	
	public void setItems(List<T> items) {
		this.items = items;
	}
	public void setCellFactory(CellFactory<T> cellFactory) {
		this.cellFactory = cellFactory;
	}
	
	public interface CellFactory<T> {
		public FlowRenderer<T> createRenderer(T item);
	}
	
	public void applyItems() {
		if (items == null) {
			getChildren().clear();
			return;
		}
		int size = Math.min(getChildren().size(), items.size());
		int i = 0;
		for (; i < size; i++) {
			@SuppressWarnings("unchecked")
			FlowRenderer<T> renderer = (FlowRenderer<T>) getChildren().get(i);
			renderer.setItem(items.get(i));
			renderer.applyItem();
		}
		if (items.size() > size) {
			size = items.size();
			for (int j = i; j < size; j++) {
				FlowRenderer<T> renderer = cellFactory.createRenderer(items.get(j));
				renderer.setSelector(() -> {
					diselect();
				});
				getChildren().add(Main.loadComponent(renderer));
			}
		} else if (getChildren().size() > size) {
			size = getChildren().size();
			for (int j = size - 1; j >= i; j--) {
				getChildren().remove(j);
			}
		}
	}
	
	public void select(int index) {
		@SuppressWarnings("unchecked")
		FlowRenderer<T> renderer = (FlowRenderer<T>) getChildren().get(index);
		renderer.select();
	}
	
	private void diselect() {
		for (Node node : getChildren()) {
			@SuppressWarnings("unchecked")
			FlowRenderer<T> renderer = (FlowRenderer<T>) node;
			renderer.unselect();
		}
	}
}
