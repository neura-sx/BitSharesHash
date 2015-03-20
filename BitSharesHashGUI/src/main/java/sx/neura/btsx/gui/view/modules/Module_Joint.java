package sx.neura.btsx.gui.view.modules;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import sx.neura.btsx.gui.view.Screen;
import sx.neura.btsx.gui.view.Module;
import sx.neura.btsx.gui.view.components.NavigationListView;
import test.neura.bts.gui.util.SampleCharts;
import test.neura.bts.gui.view.pages.Chart;
import test.neura.bts.gui.view.pages.Web;

public class Module_Joint extends Module {

	private static Module_Joint instance;

	public static Module_Joint getInstance() {
		if (!isInstance())
			instance = new Module_Joint();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Module_Joint() {
	}
	
	@FXML
	private Accordion accordionUI;

	@FXML
	private Accordion accordion01UI;
	@FXML
	private Accordion accordion02UI;
	@FXML
	private Accordion accordion03UI;

	@FXML
	private TitledPane choice01UI;
	@FXML
	private TitledPane choice02UI;
	@FXML
	private TitledPane choice03UI;
	@FXML
	private TitledPane choice04UI;

	@FXML
	private TitledPane choice0101UI;
	@FXML
	private TitledPane choice0102UI;
	@FXML
	private TitledPane choice0103UI;

	@FXML
	private TitledPane choice0201UI;
	@FXML
	private TitledPane choice0202UI;
	@FXML
	private TitledPane choice0203UI;

	@FXML
	private TitledPane choice0301UI;
	@FXML
	private TitledPane choice0302UI;
	@FXML
	private TitledPane choice0303UI;

	@FXML
	private NavigationListView navigation0101UI;
	@FXML
	private NavigationListView navigation0102UI;
	@FXML
	private NavigationListView navigation0103UI;

	@FXML
	private NavigationListView navigation0201UI;
	@FXML
	private NavigationListView navigation0202UI;
	@FXML
	private NavigationListView navigation0203UI;

	@FXML
	private NavigationListView navigation0301UI;
	@FXML
	private NavigationListView navigation0302UI;
	@FXML
	private NavigationListView navigation0303UI;

	@FXML
	private NavigationListView navigation04UI;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}

	@Override
	protected void initialize() {

		screens = new ArrayList<Screen>();

		screens.add(new Chart(SampleCharts.createLineChart()));
		screens.add(new Web("Budda"));
		screens.add(new Web("Energy"));

		screens.add(new Web("Power"));
		screens.add(new Web("Brazil"));
		screens.add(new Web("Ocean"));
		screens.add(new Web("Sky"));

		screens.add(new Web("Submarine"));
		screens.add(new Web("Sun"));

		screens.add(new Web("Pluto"));
		screens.add(new Web("Moon"));

		screens.add(new Web("Tiger"));
		screens.add(new Web("Jaguar"));
		screens.add(new Web("Mount_Everest"));

		screens.add(new Web("Vietnam"));
		screens.add(new Web("Brisbane"));

		screens.add(new Web("Antarctica"));
		screens.add(new Web("Bee"));
		screens.add(new Web("Dolphin"));

		screens.add(new Web("Neapoli"));
		screens.add(new Web("Palermo"));

		screens.add(new Web("Desert"));
		screens.add(new Web("Diving"));

		screens.add(new Web("Apollo"));
		screens.add(new Web("Neptun"));
		screens.add(new Web("Paris"));
		screens.add(new Web("London"));

		screensUI.getChildren().add(loadPage(screens.get(0)));
		screensUI.getChildren().add(loadPage(screens.get(1)));
		screensUI.getChildren().add(loadPage(screens.get(2)));

		screensUI.getChildren().add(loadPage(screens.get(3)));
		screensUI.getChildren().add(loadPage(screens.get(4)));
		screensUI.getChildren().add(loadPage(screens.get(5)));
		screensUI.getChildren().add(loadPage(screens.get(6)));

		screensUI.getChildren().add(loadPage(screens.get(7)));
		screensUI.getChildren().add(loadPage(screens.get(8)));

		screensUI.getChildren().add(loadPage(screens.get(9)));
		screensUI.getChildren().add(loadPage(screens.get(10)));

		screensUI.getChildren().add(loadPage(screens.get(11)));
		screensUI.getChildren().add(loadPage(screens.get(12)));
		screensUI.getChildren().add(loadPage(screens.get(13)));

		screensUI.getChildren().add(loadPage(screens.get(14)));
		screensUI.getChildren().add(loadPage(screens.get(15)));

		screensUI.getChildren().add(loadPage(screens.get(16)));
		screensUI.getChildren().add(loadPage(screens.get(17)));
		screensUI.getChildren().add(loadPage(screens.get(18)));

		screensUI.getChildren().add(loadPage(screens.get(19)));
		screensUI.getChildren().add(loadPage(screens.get(20)));

		screensUI.getChildren().add(loadPage(screens.get(21)));
		screensUI.getChildren().add(loadPage(screens.get(22)));

		screensUI.getChildren().add(loadPage(screens.get(23)));
		screensUI.getChildren().add(loadPage(screens.get(24)));
		screensUI.getChildren().add(loadPage(screens.get(25)));
		screensUI.getChildren().add(loadPage(screens.get(26)));

		ObservableList<Screen> list0101 = FXCollections.observableArrayList();
		list0101.add(screens.get(0));
		list0101.add(screens.get(1));
		list0101.add(screens.get(2));

		ObservableList<Screen> list0102 = FXCollections.observableArrayList();
		list0102.add(screens.get(3));
		list0102.add(screens.get(4));
		list0102.add(screens.get(5));
		list0102.add(screens.get(6));

		ObservableList<Screen> list0103 = FXCollections.observableArrayList();
		list0103.add(screens.get(7));
		list0103.add(screens.get(8));

		ObservableList<Screen> list0201 = FXCollections.observableArrayList();
		list0201.add(screens.get(9));
		list0201.add(screens.get(10));

		ObservableList<Screen> list0202 = FXCollections.observableArrayList();
		list0202.add(screens.get(11));
		list0202.add(screens.get(12));
		list0202.add(screens.get(13));

		ObservableList<Screen> list0203 = FXCollections.observableArrayList();
		list0203.add(screens.get(14));
		list0203.add(screens.get(15));

		ObservableList<Screen> list0301 = FXCollections.observableArrayList();
		list0301.add(screens.get(16));
		list0301.add(screens.get(17));
		list0301.add(screens.get(18));

		ObservableList<Screen> list0302 = FXCollections.observableArrayList();
		list0302.add(screens.get(19));
		list0302.add(screens.get(20));

		ObservableList<Screen> list0303 = FXCollections.observableArrayList();
		list0303.add(screens.get(21));
		list0303.add(screens.get(22));

		ObservableList<Screen> list04 = FXCollections.observableArrayList();
		list04.add(screens.get(23));
		list04.add(screens.get(24));
		list04.add(screens.get(25));
		list04.add(screens.get(26));

		navigation0101UI.setItems(list0101);
		navigation0101UI.getSelectionModel().select(0);

		navigation0102UI.setItems(list0102);
		navigation0102UI.getSelectionModel().select(0);

		navigation0103UI.setItems(list0103);
		navigation0103UI.getSelectionModel().select(0);

		navigation0201UI.setItems(list0201);
		navigation0201UI.getSelectionModel().select(0);

		navigation0202UI.setItems(list0202);
		navigation0202UI.getSelectionModel().select(0);

		navigation0203UI.setItems(list0203);
		navigation0203UI.getSelectionModel().select(0);

		navigation0301UI.setItems(list0301);
		navigation0301UI.getSelectionModel().select(0);

		navigation0302UI.setItems(list0302);
		navigation0302UI.getSelectionModel().select(0);

		navigation0303UI.setItems(list0303);
		navigation0303UI.getSelectionModel().select(0);

		navigation04UI.setItems(list04);
		navigation04UI.getSelectionModel().select(0);

		accordionUI.setExpandedPane(choice01UI);
		accordion01UI.setExpandedPane(choice0101UI);
		choice0101UI.setCollapsible(false);
		accordion02UI.setExpandedPane(choice0201UI);
		choice0201UI.setCollapsible(false);
		accordion03UI.setExpandedPane(choice0301UI);
		choice0301UI.setCollapsible(false);

		initialize(0);
	}
	
	@Override
	protected void addEventListeners() {
		addListenerForNavigation(navigation0101UI);
		addListenerForNavigation(navigation0102UI);
		addListenerForNavigation(navigation0103UI);

		addListenerForNavigation(navigation0201UI);
		addListenerForNavigation(navigation0202UI);
		addListenerForNavigation(navigation0203UI);

		addListenerForNavigation(navigation0301UI);
		addListenerForNavigation(navigation0302UI);
		addListenerForNavigation(navigation0303UI);

		addListenerForNavigation(navigation04UI);

		accordionUI.expandedPaneProperty().addListener(
				new ChangeListener<TitledPane>() {
					@Override
					public void changed(
							ObservableValue<? extends TitledPane> property,
							final TitledPane oldPane, final TitledPane newPane) {
						if (oldPane != null)
							oldPane.setCollapsible(true);
						if (newPane != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									newPane.setCollapsible(false);
								}
							});
							if (newPane == choice01UI) {
								if (accordion01UI.getExpandedPane() == choice0101UI)
									showScreen(navigation0101UI);
								else if (accordion01UI.getExpandedPane() == choice0102UI)
									showScreen(navigation0102UI);
								else if (accordion01UI.getExpandedPane() == choice0103UI)
									showScreen(navigation0103UI);
							} else if (newPane == choice02UI) {
								if (accordion02UI.getExpandedPane() == choice0201UI)
									showScreen(navigation0201UI);
								else if (accordion02UI.getExpandedPane() == choice0202UI)
									showScreen(navigation0202UI);
								else if (accordion02UI.getExpandedPane() == choice0203UI)
									showScreen(navigation0203UI);
							} else if (newPane == choice03UI) {
								if (accordion03UI.getExpandedPane() == choice0301UI)
									showScreen(navigation0301UI);
								else if (accordion03UI.getExpandedPane() == choice0302UI)
									showScreen(navigation0302UI);
								else if (accordion03UI.getExpandedPane() == choice0303UI)
									showScreen(navigation0303UI);
							} else if (newPane == choice04UI) {
								showScreen(navigation04UI);
							}
						}
					}
				});

		accordion01UI.expandedPaneProperty().addListener(
				new ChangeListener<TitledPane>() {
					@Override
					public void changed(
							ObservableValue<? extends TitledPane> property,
							final TitledPane oldPane, final TitledPane newPane) {
						if (oldPane != null) 
		        			oldPane.setCollapsible(true);
						if (newPane != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									newPane.setCollapsible(false);
								}
							});
							if (newPane == choice0101UI)
								showScreen(navigation0101UI);
							else if (newPane == choice0102UI)
								showScreen(navigation0102UI);
							else if (newPane == choice0103UI)
								showScreen(navigation0103UI);
						}
					}
				});

		accordion02UI.expandedPaneProperty().addListener(
				new ChangeListener<TitledPane>() {
					@Override
					public void changed(
							ObservableValue<? extends TitledPane> property,
							final TitledPane oldPane, final TitledPane newPane) {
						if (oldPane != null) 
		        			oldPane.setCollapsible(true);
						if (newPane != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									newPane.setCollapsible(false);
								}
							});
							if (newPane == choice0201UI)
								showScreen(navigation0201UI);
							else if (newPane == choice0202UI)
								showScreen(navigation0202UI);
							else if (newPane == choice0203UI)
								showScreen(navigation0203UI);
						}
					}
				});

		accordion03UI.expandedPaneProperty().addListener(
				new ChangeListener<TitledPane>() {
					@Override
					public void changed(
							ObservableValue<? extends TitledPane> property,
							final TitledPane oldPane, final TitledPane newPane) {
						if (oldPane != null) 
		        			oldPane.setCollapsible(true);
						if (newPane != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									newPane.setCollapsible(false);
								}
							});
							if (newPane == choice0301UI)
								showScreen(navigation0301UI);
							else if (newPane == choice0302UI)
								showScreen(navigation0302UI);
							else if (newPane == choice0303UI)
								showScreen(navigation0303UI);
						}
					}
				});
	}

	@Override
	protected void loadStaticData() {

	}

	@Override
	protected boolean unlock(String password) {
		return true;
	}

	@Override
	protected void lock() {

	}

	@Override
	protected String getHack() {
		return "";
	}
}
