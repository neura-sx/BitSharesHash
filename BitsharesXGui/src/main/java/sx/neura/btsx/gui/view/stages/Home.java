package sx.neura.btsx.gui.view.stages;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import sx.neura.bts.Version;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.btsx.gui.view.Screen;
import sx.neura.btsx.gui.view.Module;
import sx.neura.btsx.gui.view.modules.Module_Bts;
import sx.neura.btsx.gui.view.modules.Module_Dns;
import sx.neura.btsx.gui.view.modules.Module_Joint;
import sx.neura.btsx.gui.view.modules.Module_Otxs;
import sx.neura.btsx.gui.view.popups.Info;
import sx.neura.btsx.gui.view.popups.ReadBoolean;

public class Home extends Screen {
	
	private static Home instance;
	public static Home getInstance() {
		if (!isInstance())
			instance = new Home();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private Home() {
	}
	
	private static final int INDEX_NULL = -1;
	private static final int INDEX_A = 0;
	private static final int INDEX_B = 1;
	private static final int INDEX_C = 2;
	private static final int INDEX_D = 3;
	
	private ToggleGroup toggleGroup;
	private List<Module> modules;
	
	@FXML
	private ToggleButton aUI;
	@FXML
	private ToggleButton bUI;
	@FXML
	private ToggleButton cUI;
	@FXML
	private ToggleButton dUI;
	
	@FXML
	private LayerPane introUI;

	@FXML
	private LayerPane modulesUI;
	
	@FXML
	private Label versionUI;
	

	@FXML
	private void onA() {
		if (aUI.isSelected()) {
			hideIntro(INDEX_A);
		} else {
			showIntro();
		}
	}
	@FXML
	private void onB() {
		if (bUI.isSelected()) {
			hideIntro(INDEX_B);
		} else {
			showIntro();
		}
	}
	@FXML
	private void onC() {
		if (cUI.isSelected()) {
			hideIntro(INDEX_C);
		} else {
			showIntro();
		}
	}
	@FXML
	private void onD() {
		if (dUI.isSelected()) {
			hideIntro(INDEX_D);
		} else {
			showIntro();
		}
	}
	

	public void showA() {
		toggleGroup.selectToggle(aUI);
		onA();
	}
	public void showB() {
		toggleGroup.selectToggle(bUI);
		onB();
	}
	public void showC() {
		toggleGroup.selectToggle(cUI);
		onC();
	}
	public void showD() {
		toggleGroup.selectToggle(dUI);
		onD();
	}
	
	private void showIntro() {
		modulesUI.setIndex(INDEX_NULL, () -> introUI.setIndex(0));
	}
	
	private void hideIntro(int index) {
		Module module = modules.get(index);
		introUI.setIndex(1, () -> modulesUI.setIndex(index, () -> module.loadData()));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		toggleGroup = new ToggleGroup();
		aUI.setToggleGroup(toggleGroup);
		bUI.setToggleGroup(toggleGroup);
		cUI.setToggleGroup(toggleGroup);
		dUI.setToggleGroup(toggleGroup);
		aUI.setCursor(Cursor.HAND);
		bUI.setCursor(Cursor.HAND);
		cUI.setCursor(Cursor.HAND);
		dUI.setCursor(Cursor.HAND);
		versionUI.setText(String.format("Version %s (%s)", Version.id, Version.name));
	}
	
	@Override
	public void setPane(Pane pane) {
		super.setPane(pane);
		modules = new ArrayList<Module>();
		modules.add(Module_Bts.getInstance());
		modules.add(Module_Dns.getInstance());
		modules.add(Module_Otxs.getInstance());
		modules.add(Module_Joint.getInstance());
		modulesUI.getChildren().add(loadPageAsChild(modules.get(0)));
		modulesUI.getChildren().add(loadPageAsChild(modules.get(1)));
		modulesUI.getChildren().add(loadPageAsChild(modules.get(2)));
		modulesUI.getChildren().add(loadPageAsChild(modules.get(3)));
		showIntro();
	}
	
	@FXML
    private void onCheckForUpdates() {
		featureNotImplemented();
	}
	
	@FXML
    private void onLockAll() {
		Module_Bts.getInstance().onLock();
		Module_Dns.getInstance().onLock();
		Module_Otxs.getInstance().onLock();
		Module_Joint.getInstance().onLock();
		Info info = new Info();
		info.setTitle("Lock All");
		info.setMessage("All wallets are locked");
		info.show(pane);
	}
	
	@FXML
    private void onExit() {
		ReadBoolean readBoolean = new ReadBoolean();
		readBoolean.setTitle("Exit");
		readBoolean.setMessage("Are you sure you want to exit?");
		readBoolean.setConfirm("Yes");
		readBoolean.setCancel("Cancel");
		readBoolean.setCallback(new ReadBoolean.Callback() {
			@Override
			public void onConfirm() {
				System.exit(0);
			}
			@Override
			public void onCancel() {
			}
		});
		readBoolean.show(pane);
	}
	
	@Override
	public void unloadData() {
		for (Module module : modules)
			module.unloadData();
	}
}
