package sx.neura.btsx.gui.view.components.flow;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.Page;

public abstract class FlowRenderer<T> extends AnchorPane implements Initializable {
	
	@FXML
	protected Node backgroundUI;
	
	private boolean selectable;
	private boolean diselectable;
	private boolean isSelected;
	
	private Selector selector;
	private Actor<T> actor;
	private Jumper jumper;
	
	public interface Selector {
		public void diselect();
	}
	public static interface Actor<T> {
		public void act(T item);
	}
	public static interface Jumper {
		public void jump(Page page);
	}
	
	public void setSelector(Selector selector) {
		this.selector = selector;
	}
	public void setActor(Actor<T> actor) {
		this.actor = actor;
	}
	public void setJumper(Jumper jumper) {
		this.jumper = jumper;
	}
	
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	public void setDiselectable(boolean diselectable) {
		this.diselectable = diselectable;
	}
	
	public void select() {
		if (!selectable)
			return;
		if (!isSelected) {
			isSelected = true;
			backgroundUI.setId("selected");
		}
	}
	
	public void unselect() {
		if (!selectable)
			return;
		if (isSelected) {
			isSelected = false;
			backgroundUI.setId("unselected");
		}
	}
	
	protected T item;
	
	public void setItem(T item) {
		this.item = item;
	}
	public T getItem() {
		return item;
	}
	
	public void updateItem(T item) {
		if (!item.equals(getItem())) {
			setItem(item);
			applyItem();
		}
	}
	
	protected abstract void applyItem();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isSelected = false;
		applyItem();
		backgroundUI.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isSelected) {
					if (diselectable)
						unselect();
					if (actor != null)
						actor.act(null);
				} else {
					if (selectable) {
						selector.diselect();
						select();
					}
					if (actor != null)
						actor.act(item);
				}
			}
		});
	}
	
	protected void jump(Page page) {
		jumper.jump(page);
	}
	
	protected static String[] getAmount(Amount amount) {
		return Model.getInstance().getAmountPair(amount);
	}
	
	protected static String getAmount(Asset asset, long amount) {
		return Model.getInstance().getAmount(asset, amount);
	}
	
	protected static Asset getAssetById(int assetId) {
		return Model.getInstance().getAssetById(assetId);
	}
	
	protected Image getAvatarImage(String name) {
		return Model.getInstance().getAvatarImage(name);
	}
}
