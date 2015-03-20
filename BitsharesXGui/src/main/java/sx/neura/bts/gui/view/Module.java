package sx.neura.bts.gui.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sx.neura.bts.Version;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.popups.ReadBoolean;
import sx.neura.bts.json.CommandJson;
import sx.neura.bts.util.Util;

import com.fxexperience.javafx.animation.ShakeTransition;

public abstract class Module extends Screen {
	
	private static final int SNAPSHOT_COLUMNS = 3;
	private static final int SNAPSHOT_ROWS = 3;
	
	private static final int LOADING_INIT_EFFECT_DURATION = 1000;
	private static final int LOADING_PROGRESS_EFFECT_DURATION = 2000;
	
	private static final int SCALE_EFFECT_DURATION = 250;
	private static final double SCALE_FACTOR = 0.5;
	private static final double SCALE_HORIZONTAL_OFFSET = 200.0;
	private static final double SCALE_VERTICAL_OFFSET = 120.0;
	
	@FXML
	protected Label versionUI;
	@FXML
	protected Label clientVersionUI;
	
	@FXML
	protected Node progressTrackUI;
	@FXML
	protected Node progressBarUI;
	@FXML
	protected Label processingUI;
	
	@FXML
	protected LayerPane introUI;
	@FXML
	protected LayerPane workspaceUI;
	@FXML
	protected LayerPane stackUI;
	@FXML
	protected LayerPane pagesUI;
	
	@FXML
	protected Pane miniStackUI;

	@FXML
	protected Node passwordBoxUI;
	@FXML
	protected PasswordField passwordUI;
	
	@FXML
	protected Node navigationUI;
	@FXML
	protected Button navigateBckUI;
	@FXML
	protected Button navigateFwdUI;
	@FXML
	protected Button exitUI;
	
	
	@FXML
	protected Pane groundUI;
	@FXML
	protected Pane groundShadowUI;
	
	@FXML
	protected ToggleButton toggleOverviewUI;
	@FXML
	protected ToggleButton toggleSnapshotsUI;
	
	@FXML
	protected Node overviewUI;
	@FXML
	protected Node menuUI;
	@FXML
	protected Node snapshotsUI;
	
	@FXML
	protected LayerPane subMenuUI;
	@FXML
	protected LayerPane subAboutUI;
	
	@FXML
	protected LayerPane overviewLeftSliderUI;
	@FXML
	protected LayerPane overviewRightSliderUI;
	@FXML
	protected LayerPane menuSliderUI;
	@FXML
	protected LayerPane aboutSliderUI;
	@FXML
	protected LayerPane watermarkSliderUI;
	
	@FXML
	protected LayerPane snapshotsSliderUI;
	
	@FXML
	protected GridPane snapshotsGridUI;

	@FXML
	protected Node maxiMenuUI;
	
	@FXML
	protected ToggleGroup menuToggleGroupUI;
	
	private boolean isUnlocked;
	private int index;
	private int subIndex;
	protected List<Page> pages;
	protected List<Page> subPages;
	private ObservableList<Page> recentPages;
	private ObservableList<Page> historyBck;
	private ObservableList<Page> historyFwd;
	protected List<Page> stack;
	private List<Page> miniStack;
	private BooleanProperty isProcessing;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		module = this;
		index = -1;
		subIndex = -1;
		pages = new ArrayList<Page>();
		subPages = new ArrayList<Page>();
		recentPages = FXCollections.observableArrayList();
		historyBck = FXCollections.observableArrayList();
		historyFwd = FXCollections.observableArrayList();
		stack = new ArrayList<Page>();
		miniStack = new ArrayList<Page>();
		isProcessing = new SimpleBooleanProperty(false);
		
		versionUI.setText(String.format("%s %s", "version", Version.id));
		
		passwordUI.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER))
					unlockWorkspace();
			}
		});
		
		navigateBckUI.setOnAction((ActionEvent event) -> {
			navigateBck();
        });
		navigateFwdUI.setOnAction((ActionEvent event) -> {
			navigateFwd();
        });
		
		toggleOverviewUI.setOnAction((ActionEvent event) -> {
			if (!overviewUI.isVisible())
				showOverview();
			else if (overviewUI.isVisible())
				hideOverview();
        });
		
		toggleSnapshotsUI.setOnAction((ActionEvent event) -> {
			if (!snapshotsUI.isVisible())
				showSnapshots();
			else if (snapshotsUI.isVisible())
				hideSnapshots();
        });
		
		exitUI.setOnAction((ActionEvent event) -> {
			if (isUnlocked)
	    		lockWorkspace();
	    	else
	    		exitApplication();
        });
		
		menuUI.setOnMouseClicked((MouseEvent event) -> hideMenu());
		
		Util.manageToggleGroup(menuToggleGroupUI, () -> toggleMenu());
		
		menuToggleGroupUI.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					int index = menuToggleGroupUI.getToggles().indexOf(menuToggleGroupUI.getSelectedToggle());
					subMenuUI.setIndex(index);
					subAboutUI.setIndex(index);
		});
		
		exitUI.visibleProperty().bind(introUI.getIndexProperty().greaterThan(0).or(workspaceUI.getIndexProperty().greaterThan(0)));
		exitUI.disableProperty().bind(isProcessing);
		
		passwordBoxUI.visibleProperty().bind(isOverlay.not());
		
		maxiMenuUI.visibleProperty().bind(overviewUI.visibleProperty().not().and(snapshotsUI.visibleProperty().not()));
		
		navigationUI.visibleProperty().bind(workspaceUI.getIndexProperty().isEqualTo(1)
				.and(stackUI.getIndexProperty().isEqualTo(0)
				.and(overviewUI.visibleProperty().not()
				.and(snapshotsUI.visibleProperty().not()
				.and(menuUI.visibleProperty().not())))));
		
		miniStackUI.visibleProperty().bind(workspaceUI.getIndexProperty().greaterThan(0).and(isProcessing.not()));
		
		toggleOverviewUI.visibleProperty().bind(toggleSnapshotsUI.selectedProperty().not().and(menuUI.visibleProperty().not()));
		toggleSnapshotsUI.visibleProperty().bind(toggleOverviewUI.selectedProperty().not().and(menuUI.visibleProperty().not()));
		
		toggleSnapshotsUI.disableProperty().bind(Bindings.isEmpty(recentPages));
		
		navigateBckUI.disableProperty().bind(Bindings.isEmpty(historyBck));
		navigateFwdUI.disableProperty().bind(Bindings.isEmpty(historyFwd));
		
		pagesUI.setIndex(index);
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			initialize(() -> {
				introUI.setIndex(1);
				if (!CommandJson.LIVE_MODE) {
					Model.getInstance().setWalletOpen(true);
					passwordUI.setText(getHack());
					unlockWorkspace();
				}
			});
		}
		super.loadData();
	}
	
	protected abstract Page getDefaultPage();
	
	protected boolean isInitialized() {
		return index >=0;
	}
	
	protected void showOverview() {
		if (!overviewUI.isVisible()) {
			toggleOverviewUI.setSelected(true);
			Runnable runnable = () -> {
				overviewUI.setVisible(true);
				overviewLeftSliderUI.setIndex(1);
				overviewRightSliderUI.setIndex(1);
			};
			if (menuUI.isVisible()) {
				hideMenu(runnable);
			} else if (snapshotsUI.isVisible()) {
				hideSnapshots(runnable);
			} else {
				runnable.run();
			}
		}
	}
	protected void hideOverview() {
		hideOverview(null);
	}
	protected void hideOverview(Runnable runnable) {
		if (overviewUI.isVisible()) {
			toggleOverviewUI.setSelected(false);
			if (!isInitialized())
				togglePage(getDefaultPage());
			overviewLeftSliderUI.setIndex(0, () -> {
				overviewUI.setVisible(false);
				if (runnable != null)
					runnable.run();
			});
			overviewRightSliderUI.setIndex(0);
		}
	}
	
	private void toggleMenu() {
		if (menuUI.isVisible())
			hideMenu();
		else
			showMenu();
	}
	
	private void showMenu() {
		if (!menuUI.isVisible()) {
			Runnable runnable = () -> {
				scaleDown();
				menuUI.setVisible(true);
				menuSliderUI.setIndex(1);
				aboutSliderUI.setIndex(1);
				watermarkSliderUI.setIndex(1);
			};
			if (overviewUI.isVisible()) {
				hideOverview(runnable);
			} else if (snapshotsUI.isVisible()) {
				hideSnapshots(runnable);
			} else {
				runnable.run();
			}
		}
	}
	protected void hideMenu() {
		hideMenu(null);
	}
	private void hideMenu(Runnable runnable) {
		if (menuUI.isVisible()) {
			menuSliderUI.setIndex(0, () -> {
				menuUI.setVisible(false);
				if (runnable != null)
					runnable.run();
			});
			aboutSliderUI.setIndex(0);
			watermarkSliderUI.setIndex(0);
			scaleUp();
		}
	}
	
	private void showSnapshots() {
		if (!snapshotsUI.isVisible()) {
			toggleSnapshotsUI.setSelected(true);
			Runnable runnable = () -> {
				snapshotsUI.setVisible(true);
				snapshotsSliderUI.setIndex(1, () -> {
					populateSnapshots();
				});
			};
			if (menuUI.isVisible()) {
				hideMenu(runnable);
			} else if (overviewUI.isVisible()) {
				hideOverview(runnable);
			} else {
				runnable.run();
			}
		}
	}
	private void hideSnapshots() {
		hideSnapshots(null);
	}
	private void hideSnapshots(Runnable runnable) {
		if (snapshotsUI.isVisible()) {
			toggleSnapshotsUI.setSelected(false);
			snapshotsSliderUI.setIndex(0, () -> {
				snapshotsUI.setVisible(false);
				if (runnable != null)
					runnable.run();
			});
		}
	}
	
	public void togglePage(Page page) {
		togglePage(page, null);
	}
	
	protected void togglePage(Page page, Runnable runnable) {
		if (isInitialized() && page.equals(getCurrentPage())) {
			if (runnable != null)
				runnable.run();
			return;
		}
		applyToggle(page.getToggle());
		nextPage(page, runnable);
	}
	
	private void nextPage(Page page) {
		nextPage(page, null);
	}
	
	private void nextPage(Page page, Runnable runnable) {
		addCurrentPageToHistory();
		showPage(page, runnable);
	}
	
	private void addCurrentPageToHistory() {
		if (isInitialized())
			historyBck.add(getCurrentPage());
		if (!historyFwd.isEmpty())
			historyFwd.clear();
	}
	
	private void navigateBck() {
		if (!historyBck.isEmpty()) {
			Page page = historyBck.remove(historyBck.size() - 1);
			historyFwd.add(getCurrentPage());
			showPage(page);
			applyToggle(page.getToggle());
		}
	}
	private void navigateFwd() {
		if (!historyFwd.isEmpty()) {
			Page page = historyFwd.remove(historyFwd.size() - 1);
			historyBck.add(getCurrentPage());
			showPage(page);
			applyToggle(page.getToggle());
		}
	}
	
	private void applyToggle(Toggle toggle) {
		ToggleMap map = (ToggleMap) toggle.getUserData();
		selectToggle(map.toggle);
		selectToggle(toggle);
	}
	
	private void showPage(Page page) {
		showPage(page, null);
	}
	
	private void showPage(Page page, Runnable runnable) {
		if (isInitialized()) {
			if (getCurrentPage().getSnapshot() == null)
				getCurrentPage().makeSnapshot();
			getCurrentPage().unloadData();
		}
		int nextIndex = pages.indexOf(page.getHomePage());
		Page peer = page.getHomePage().getPeer();
		if (index != nextIndex) {
			index = nextIndex;
	 		pagesUI.setIndex(index, () -> {
	 			if (page.equals(peer)) {
	 				subIndex = subPages.indexOf(page);
		 			if (runnable != null)
						runnable.run();
		 			page.loadData();
	 			} else {
		 			subIndex = subPages.indexOf(peer);
		 			addCurrentPageToHistory();
		 			subIndex = subPages.indexOf(page);
		 			page.getHomePage().setPeer(page, () -> {
			 			if (runnable != null)
							runnable.run();
			 			page.loadData();
		 			});
	 			}
	 		});
		} else {
			subIndex = subPages.indexOf(page);
			if (page.equals(peer)) {
				if (runnable != null)
					runnable.run();
	 			page.loadData();
			} else {
	 			page.getHomePage().setPeer(page, () -> {
		 			if (runnable != null)
						runnable.run();
		 			page.loadData();
	 			});
			}
		}
 		recentPages.remove(page);
 		recentPages.add(0, page);
	}
	
	@FXML
	protected void onMenu(ActionEvent event) {
		Toggle toggle = (Toggle) event.getSource();
		onMenu(toggle);
	}
	
	private void onMenu(Toggle toggle) {
		ToggleGroup group = (ToggleGroup) toggle.getUserData();
		Toggle t = group.getSelectedToggle();
		if (t == null) {
			t = group.getToggles().get(0);
			selectToggle(t);
		}
		ToggleMap map = (ToggleMap) t.getUserData();
		nextPage(subPages.get(map.index));
	}
	
	@FXML
	protected void onSubMenu(ActionEvent event) {
		Toggle toggle = (Toggle) event.getSource();
		onSubMenu(toggle);
	}
	
	private void onSubMenu(Toggle toggle) {
		ToggleMap map = (ToggleMap) toggle.getUserData();
		nextPage(subPages.get(map.index));
	}
	
	protected void animateLoadingProgress(Runnable runnable) {
		animateProgress(1.0, CommandJson.LIVE_MODE ? LOADING_INIT_EFFECT_DURATION : 100, progressTrackUI, runnable);
	}
	
	protected void animateLoadingProgress(double progress, Runnable runnable) {
		animateProgress(progress, CommandJson.LIVE_MODE ? LOADING_PROGRESS_EFFECT_DURATION : 100, progressBarUI, runnable);
	}
	
	private void scaleDown() {
		final double factor = SCALE_FACTOR * ((groundUI.getWidth() - SCALE_HORIZONTAL_OFFSET) / groundUI.getWidth());
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> groundShadowUI.setVisible(true),
				new KeyValue(groundUI.scaleXProperty(), factor, Interpolator.EASE_BOTH))).play();
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> {},
				new KeyValue(groundUI.scaleYProperty(), factor, Interpolator.EASE_BOTH))).play();
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> {},
				new KeyValue(groundUI.translateXProperty(), SCALE_HORIZONTAL_OFFSET - (groundUI.getWidth() * (1 - factor) / 2), Interpolator.EASE_BOTH))).play();
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> {},
				new KeyValue(groundUI.translateYProperty(), SCALE_VERTICAL_OFFSET - (groundUI.getHeight() * (1 - factor) / 2), Interpolator.EASE_BOTH))).play();
	}
	
	private void scaleUp() {
		groundShadowUI.setVisible(false);
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> {},
				new KeyValue(groundUI.scaleXProperty(), 1.0, Interpolator.EASE_BOTH))).play();
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> {},
				new KeyValue(groundUI.scaleYProperty(), 1.0, Interpolator.EASE_BOTH))).play();
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> {},
				new KeyValue(groundUI.translateXProperty(), 0, Interpolator.EASE_BOTH))).play();
		new Timeline(
				new KeyFrame(Duration.millis(SCALE_EFFECT_DURATION), (ActionEvent event) -> {},
				new KeyValue(groundUI.translateYProperty(), 0, Interpolator.EASE_BOTH))).play();
	}

	
	private void populateSnapshots() {
		snapshotsGridUI.getChildren().clear();
		int colIndex = 0;
		int rowIndex = 0;
		for (Page page : recentPages) {
			SnapshotTile tile = new SnapshotTile(this, page);
			GridPane.setColumnIndex(tile, colIndex);
			GridPane.setRowIndex(tile, rowIndex);
			GridPane.setHalignment(tile, HPos.CENTER);
			GridPane.setValignment(tile, VPos.CENTER);
			snapshotsGridUI.getChildren().add(tile);
			colIndex++;
			if (colIndex > SNAPSHOT_COLUMNS - 1) {
				colIndex = 0;
				rowIndex++;
				if (rowIndex > SNAPSHOT_ROWS - 1)
					break;
			}
        }
	}
	
	public Page getCurrentPage() {
		return subPages.get(subIndex);
	}
	
	private static void selectToggle(ToggleGroup group, Toggle toggle) {
		group.selectToggle(toggle);
	}
	protected static void selectToggle(Toggle toggle) {
		selectToggle(toggle.getToggleGroup(), toggle);
	}
	protected static void selectToggle(ToggleGroup group, int index) {
		selectToggle(group, group.getToggles().get(index));
	}
	
	protected abstract void initialize(Runnable runnable);
	protected abstract boolean unlock(String password);
	protected abstract void lock();
	protected abstract String getHack();
	
	public BooleanProperty isProcessing() {
		return isProcessing;
	}
	
	public void jump(Page page) {
		page.setModule(this);
		addPageToStack(page);
	}
	
	public void addPageToStack(Page page) {
		if (!stack.isEmpty())
			stack.get(stack.size() - 1).unloadUI();
		stack.add(page);
		Pane pane = (page.getPane() != null ? page.getPane() : loadScreen(page));
		stackUI.getChildren().add(pane);
		stackUI.setIndex(stack.size(), () -> {
			Platform.runLater(() -> {
				page.loadUI();
				page.loadData();
			});
		});
	}
	
	public void removePageFromStack() {
		isProcessing.set(false);
		stack.get(stack.size() - 1).unloadUI();	
		stackUI.setIndex(stack.size() - 1, () -> {
			stackUI.getChildren().remove(stack.size());
			Page page = stack.remove(stack.size() - 1);
			if (isPageOnMiniStack(page)) {
				miniStackUI.getChildren().remove(miniStack.indexOf(page));
				miniStack.remove(page);
			}
			if (stack.size() > 0) {
				stack.get(stack.size() - 1).loadUI();
				stack.get(stack.size() - 1).loadData();
			}
			recentPages.remove(page);
	 		if (snapshotsUI.isVisible())
				populateSnapshots();
		});
	}
	
	public boolean isPageOnMiniStack(Page page) {
		return miniStack.contains(page);
	}
	
	public void minimizePage() {
		Page page = stack.get(stack.size() - 1);
		page.makeSnapshot();
		page.unloadUI();
		stackUI.setIndex(stack.size() - 1, () -> {
			stackUI.getChildren().remove(stack.size());
			stack.remove(page);
			if (miniStack.contains(page)) {
				miniStackUI.getChildren().get(miniStack.indexOf(page)).setDisable(false);
			} else {
				miniStack.add(page);
				Label node = new Label(page.getTitle());
				node.getStyleClass().add("sx-ministack");
				node.setOnMouseClicked((MouseEvent event) -> { 
					maximizePage(miniStack.get(miniStackUI.getChildren().indexOf(event.getSource())));
					event.consume();
		    	});
				miniStackUI.getChildren().add(node);
			}
			if (stack.size() > 0) {
				stack.get(stack.size() - 1).loadUI();
				stack.get(stack.size() - 1).loadData();
			}
			recentPages.remove(page);
	 		recentPages.add(0, page);
	 		if (snapshotsUI.isVisible())
				populateSnapshots();
		});
	}
	
	private void maximizePage(Page page) {
		miniStackUI.getChildren().get(miniStack.indexOf(page)).setDisable(true);
		addPageToStack(page);
	}
	
	@FXML
	protected void onHack(MouseEvent event) {
		if (event.getClickCount() > 1) {
			passwordUI.setText(getHack());
			unlockWorkspace();
		}
	}
    
	private void unlockWorkspace() {
		processingUI.setText(String.format("Unlocking %s..", getName()));
		introUI.setIndex(0, () -> {
			if (unlock(passwordUI.getText())) {
				passwordUI.setText("");
				workspaceUI.setIndex(1);
				isUnlocked = true;
			} else {
				introUI.setIndex(1);
				ShakeTransition shake = new ShakeTransition(passwordUI);
				shake.setOnFinished((ActionEvent event) -> passwordUI.setText(""));
				shake.play();
			}
		});
	}
	
	protected void lockWorkspace() {
		processingUI.setText(String.format("Locking %s..", getName()));
		workspaceUI.setIndex(0, () -> {
			lock();
			isUnlocked = false;
			introUI.setIndex(1);
		});
	}
	
	private void exitApplication() {
		ReadBoolean readBoolean = new ReadBoolean();
		readBoolean.setMessage("Are you sure you want to exit?");
		readBoolean.setConfirm("Yes");
		readBoolean.setCancel("Cancel");
		readBoolean.setCallback(new ReadBoolean.Callback() {
			@Override
			public void onConfirm() {
				onApplicationExit();
				Platform.exit();
			}
			@Override
			public void onCancel() {
			}
		});
		readBoolean.show(pane, this, isOverlay);
	}
	
	protected abstract void onApplicationExit();
	
	protected static class ToggleMap {
		public ToggleMap(int index, Toggle toggle) {
			this.index = index;
			this.toggle = toggle;
		}
		public int index;
		public Toggle toggle;
	}
	
	private static class SnapshotTile extends ImageView {
		private Module module;
		private Page page;
		private SnapshotTile(Module module, Page page) {
			this.module = module;
			this.page = page;
			if (module.isInitialized() && page.equals(module.getCurrentPage()))
				page.makeSnapshot();
			setImage(page.getSnapshot());
			setFitWidth(0.90 * module.snapshotsGridUI.getWidth() / SNAPSHOT_COLUMNS);
			setFitHeight(0.90 * module.snapshotsGridUI.getHeight() / SNAPSHOT_ROWS);
			setPreserveRatio(true);
			setSmooth(true);
			setOnMouseClicked((MouseEvent event) -> {
				if (this.page.getTitle() != null) {
					module.maximizePage(this.page);
				} else {
					this.module.applyToggle(this.page.getToggle());
					if (!this.page.equals(module.getCurrentPage()))
						this.module.addCurrentPageToHistory();
					this.module.hideSnapshots(() -> {
						this.module.showPage(this.page);
					});
				}
				event.consume();
	    	});
			getStyleClass().add("sx-snapshot-tile");
		}
	}
}
