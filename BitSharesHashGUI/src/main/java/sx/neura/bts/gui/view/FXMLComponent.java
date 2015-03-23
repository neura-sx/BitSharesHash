package sx.neura.bts.gui.view;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Main;
import sx.neura.bts.util.Util;

public abstract class FXMLComponent extends AnchorPane implements Initializable {

	public FXMLComponent() {
		loadComponent(this);
	}
	
	protected static String t(String id) {
		return Main.t(id);
	}
	
	protected static boolean c(String s, String i) {
		return Util.c(s, i);
	}
	
	protected static Pane loadComponent(Initializable root) {
		return Main.loadComponent(root);
	}
	
}
