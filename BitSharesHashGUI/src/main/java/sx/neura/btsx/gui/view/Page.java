package sx.neura.btsx.gui.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sx.neura.btsx.gui.view.components.SearchBox;

public abstract class Page extends Screen {
	
	private static final double OPACITY_OFF = 0.8;
	private static final double OPACITY_ON = 1.0;
	
	protected PageManager pageManager;
	protected int index;
	
	@FXML
	protected Node leftWingUI;
	@FXML
	protected Node rightWingUI;
	
	@FXML
	protected Node homeUI;
	@FXML
	protected Node backUI;
	
	@FXML
	protected Node action01UI;
	@FXML
	protected Node action02UI;
	@FXML
	protected Node action03UI;
	@FXML
	protected Node action04UI;
	@FXML
	protected Node action05UI;
	
	protected SearchBox searchBoxUI;
	
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	protected SearchBox createSearchBox() {
		return null;
	}
	
	protected SearchBox createSearchBox(String prompt, double offset, double width) {
		SearchBox searchBox = new SearchBox();
		searchBox.getStyleClass().add("searchBox");
		searchBox.setFocusTraversable(false);
		searchBox.setMinWidth(width);
		searchBox.setMaxWidth(width);
		searchBox.setPromptText(prompt);
		AnchorPane.setBottomAnchor(searchBox, 2.0);
		AnchorPane.setLeftAnchor(searchBox, offset);
		return searchBox;
	}
	
	public void loadUI() {
		if (leftWingUI != null)
			leftWingUI.setVisible(true);
		if (rightWingUI != null)
			rightWingUI.setVisible(true);
		if (searchBoxUI != null)
			pageManager.getFooter().getChildren().add(searchBoxUI);
	}
	
	public void unloadUI() {
		if (leftWingUI != null)
			leftWingUI.setVisible(false);
		if (rightWingUI != null)
			rightWingUI.setVisible(false);
		if (searchBoxUI != null)
			pageManager.getFooter().getChildren().remove(searchBoxUI);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		searchBoxUI = createSearchBox();
		
		manageButton(homeUI, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pageManager.reduceLevel(0);
			}
		});
		manageButton(backUI, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pageManager.reduceLevel(index - 1);
			}
		});
		manageButton(action01UI);
		manageButton(action02UI);
		manageButton(action03UI);
		manageButton(action04UI);
		manageButton(action05UI);
	}
	
	@Override
	public void setPane(Pane pane) {
		super.setPane(pane);
		if (pane.isVisible())
			loadData();
	}
	
	public void jump(Page page) {
		page.setPageManager(pageManager);
		page.setIndex(index + 1);
		pageManager.addNextLevel(page, index + 1);
	}
	
	protected void manageButton(Node node, EventHandler<MouseEvent> handler) {
		if (node != null) {
			node.setCursor(Cursor.HAND);
			node.setOpacity(OPACITY_OFF);
			node.setOnMouseEntered((MouseEvent event) -> {
				node.setOpacity(OPACITY_ON);
			});
			node.setOnMouseExited((MouseEvent event) -> {
				node.setOpacity(OPACITY_OFF);
			});
			node.setOnMouseClicked(handler);
		}
	}
	
	private void manageButton(Node node) {
		manageButton(node, (MouseEvent event) -> {
			featureNotImplemented();
		});
	}
	
}
