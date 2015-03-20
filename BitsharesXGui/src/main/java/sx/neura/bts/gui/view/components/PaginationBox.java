package sx.neura.bts.gui.view.components;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PaginationBox extends Pagination {
	
	private VBox space;
	private int itemsPerPage;
	
	private boolean alternateEven;
	private double spacing;
	
	public boolean isAlternateEven() {
		return alternateEven;
	}
	public void setAlternateEven(boolean alternateEven) {
		this.alternateEven = alternateEven;
	}
	public double getSpacing() {
		return spacing;
	}
	public void setSpacing(double spacing) {
		this.spacing = spacing;
	}
	
	{
		setAlternateEven(true);
		setSpacing(0);
	}
	
	@Override
	protected Pane getSpace() {
		if (space == null)
			space = new VBox();
		return space;
	}
	
	@Override
	protected int getNumberOfPages() {
		itemsPerPage = (int) (getHeight() / host.getPrefRowHeight());
		if (itemsPerPage == 0)
			return 0;
		int num = (host.getListSize() / itemsPerPage);
		if (num == 0 || host.getListSize() % itemsPerPage > 0)
			num ++;
		return num;
	}
	
	@Override
	protected void clearSpace(boolean visible) {
		space.setVisible(visible);
		space.getChildren().clear();
	}
	
	@Override
	protected void populateSpace(Integer pageIndex) {
		space.setSpacing(spacing);
	    int page = pageIndex * itemsPerPage;
	    int count = 0;
	    for (int i = page; i < page + itemsPerPage; i++) {
	        Pane pane = host.getTilePane(i);
	        if (i < host.getListSize()) {
	        	pane.getStyleClass().add("sx-list-tile-itemized");
	        	if (alternateEven) {
		        	if (count % 2 != 0)
		        		pane.getStyleClass().add("sx-list-tile-itemized-uneven");
		        	count++;
	        	}
	        }
	        VBox.setVgrow(pane, Priority.ALWAYS);
	        space.getChildren().add(pane);
	    }
	}

}
