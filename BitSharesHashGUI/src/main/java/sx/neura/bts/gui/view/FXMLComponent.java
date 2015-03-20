package sx.neura.bts.gui.view;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import sx.neura.bts.gui.Main2;
import sx.neura.bts.util.Util;

public abstract class FXMLComponent extends AnchorPane implements Initializable {

	public FXMLComponent() {
		Main2.loadComponent(this);
	}
	
	protected static String t(String id) {
		return Main2.t(id);
	}
	
	protected static boolean c(String s, String i) {
		return Util.c(s, i);
	}
	
}
