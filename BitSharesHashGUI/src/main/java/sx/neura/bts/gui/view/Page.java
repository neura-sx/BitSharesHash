package sx.neura.bts.gui.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.popups.ReadBoolean;

public abstract class Page extends Screen {
	
	@FXML
	protected Node leftWingUI;
	@FXML
	protected Node rightWingUI;
	@FXML
	protected Pane workspaceUI;
	
	private Toggle toggle;
	private Image snapshot;
	
	protected List<Page> peers;
	
	public void setToggle(Toggle toggle) {
		this.toggle = toggle;
	}
	public Toggle getToggle() {
		return toggle;
	}
	
	public void makeSnapshot() {
		if (getHomePage() != null) {
			getHomePage().makeSnapshot();
			this.snapshot = getHomePage().getSnapshot();
		} else if (workspaceUI != null) {
			SnapshotParameters params = new SnapshotParameters();
			params.setViewport(new Rectangle2D(workspaceUI.getLayoutX(), 0, workspaceUI.getWidth(), workspaceUI.getHeight()));
			this.snapshot = pane.snapshot(params, null);
		} else {
			SnapshotParameters params = new SnapshotParameters();
			params.setViewport(new Rectangle2D(pane.getLayoutX(), 0, pane.getWidth(), pane.getHeight()));
			this.snapshot = pane.snapshot(params, null);
		}
	}
	
	public Image getSnapshot() {
		return snapshot;
	}
	
	public String getTitle() {
		return null;
	}
	
	public Page getPeer() {
		return null;
	}
	
	public void setPeer(Page peer, Runnable runnable) {
		
	}
	
	public void setPeers(List<Page> peers) {
		this.peers = peers;
	}

	
	public void loadUI() {
		if (leftWingUI != null)
			leftWingUI.setVisible(true);
		if (rightWingUI != null)
			rightWingUI.setVisible(true);
	}
	
	public void unloadUI() {
//		if (leftWingUI != null)
//			leftWingUI.setVisible(false);
//		if (rightWingUI != null)
//			rightWingUI.setVisible(false);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
	@Override
	public void setPane(Pane pane) {
		super.setPane(pane);
		if (pane.isVisible())
			loadData();
	}
	
	protected Page getHomePage() {
		return null;
	}
	
	@Override
	protected Pane getModalPane() {
		return module.getPane();
	}
	
	@FXML
	protected void onCancel() {
		if (!module.isPageOnMiniStack(this)) {
			module.removePageFromStack();
			return;
		}
		ReadBoolean readBoolean = new ReadBoolean();
		readBoolean.setMessage("Are you sure you want to do close this page?");
		readBoolean.setConfirm("Yes");
		readBoolean.setCancel("Cancel");
		readBoolean.setCallback(new ReadBoolean.Callback() {
			@Override
			public void onConfirm() {
				module.removePageFromStack();
			}
			@Override
			public void onCancel() {
			}
		});
		readBoolean.show(getModalPane(), module, isOverlay);
	}
	@FXML
	protected void onMinimize() {
		module.minimizePage();
	}
	
	protected <T> Pane createTilePane(Tile_Bts<T> tile, List<T> list, Region region, int index) {
        if (index < list.size()) {
        	tile.setItem(list.get(index));
        	tile.setModule(module);
        	tile.setRegion(region);
        }
        return loadComponent(tile);
	}
	
	protected static Pane loadComponent(Initializable root) {
		return Application.loadComponent(root);
	}
}
