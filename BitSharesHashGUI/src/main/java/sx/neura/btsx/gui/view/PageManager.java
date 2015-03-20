package sx.neura.btsx.gui.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.view.components.LayerPane;

public class PageManager  extends Screen {
	
	@FXML
	private LayerPane stackUI;
	
	private Module module;
	private Page initialPage;
	private List<Page> stack;
	
	public PageManager(Module module, Page initialPage) {
		this.module = module;
		this.initialPage = initialPage;
		this.stack = new ArrayList<Page>();
	}
	
	private Pane footer;
	public Pane getFooter() {
		if (footer == null) {
			footer = new AnchorPane();
			AnchorPane.setTopAnchor(footer, 0.0);
			AnchorPane.setBottomAnchor(footer, 0.0);
			AnchorPane.setLeftAnchor(footer, 0.0);
			AnchorPane.setRightAnchor(footer, 0.0);
		}
		return footer;
	}
	
	@Override
	public String getName() {
		return initialPage.getName();
	}
	
	@Override
	public void loadData() {
		if (module.getFooter() != null)
			module.getFooter().getChildren().add(getFooter());
		super.loadData();
	}
	
	@Override
	public void ping() {
		for (Page page : stack)
			page.ping();
	}
	
	@Override
	public void reloadData() {
		for (Page page : stack)
			page.reloadData();
	}
	
	@Override
	public void unloadData() {
		module.getFooter().getChildren().remove(getFooter());
		super.unloadData();
	}
	
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialPage.setPageManager(this);
		addNextLevel(initialPage, 0);
	}
	
	public void addNextLevel(Page page, int index) {
		page.setIndex(index);
		if (!stack.isEmpty())
			stack.get(stack.size() - 1).unloadUI();
		stack.add(page);
		stackUI.getChildren().add(loadPage(page));
		stackUI.setIndex(index, () -> {
			page.loadUI();
			page.loadData();
		});
	}
	
	public void reduceLevel(int index) {
		if (!stack.isEmpty())
			stack.get(stack.size() - 1).unloadUI();
		stackUI.setIndex(index, () -> {
			stack.get(index).loadUI();
			stack.get(index).loadData();
			for (int i = stack.size() - 1; i > index; i--) {
				stack.remove(i);
				stackUI.getChildren().remove(i);
			}
		});
	}
}
