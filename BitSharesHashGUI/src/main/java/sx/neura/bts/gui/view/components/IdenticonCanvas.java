package sx.neura.bts.gui.view.components;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;

import org.apache.commons.codec.digest.DigestUtils;

import sx.neura.bts.util.Jdenticon;

public class IdenticonCanvas extends Canvas {
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name == null || name.isEmpty()) {
			this.name = null;
			this.hash = null;
			Jdenticon.clear(this);
			return;
		}
		this.name = name;
		this.hash = DigestUtils.sha256Hex(name);
		reset();
	}
	
	private Double padding;
	public Double getPadding() {
		return padding;
	}
	public void setPadding(Double padding) {
		this.padding = padding;
		reset();
	}
	
	private boolean isInitialized;
	private String hash;
	
	public IdenticonCanvas() {
		super();
		isInitialized = false;
		padding = 0.0;
		boundsInParentProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) -> {
			//System.out.println(String.format("%s %f %f", name, getWidth(), getHeight()));
			if (!isInitialized)
				reset();
		});
		
	}
	
	private void reset() {
		if (hash != null && !hash.isEmpty() && padding != null && getWidth() > 0 && getHeight() > 0) {
			System.out.println(String.format("jdenticon: %s", name));
			Jdenticon.make(this, hash, padding);
			isInitialized = true;
		}
	}
}
