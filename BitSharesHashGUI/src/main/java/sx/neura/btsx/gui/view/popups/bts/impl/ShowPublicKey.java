package sx.neura.btsx.gui.view.popups.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import sx.neura.btsx.gui.view.popups.bts.Popup_Bts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ShowPublicKey extends Popup_Bts {
	
	@FXML
	private Label nameUI;
	
	@FXML
	private TextField publicKeyUI;
	
	private String name;
	private String publicKey;
	
	public void setName(String name) {
		this.name = name;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		setTitle("Public Key");
		nameUI.setText(name);
		publicKeyUI.setText(publicKey);
	}
	
	@FXML
	private void onConfirm(ActionEvent event) {
		hideItself();
	}
	
}
