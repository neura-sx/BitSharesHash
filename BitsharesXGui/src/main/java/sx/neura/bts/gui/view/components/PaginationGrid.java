package sx.neura.bts.gui.view.components;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class PaginationGrid extends Pagination {
	
	private GridPane space;
	private int numberOfColumns;
	private int numberOfRows;
	
	@Override
	protected Pane getSpace() {
		if (space == null)
			space = new GridPane();
		return space;
	}
	
	@Override
	protected int getNumberOfPages() {
		numberOfColumns = (int) (getWidth() / host.getPrefColumnWidth());
		numberOfRows = (int) (getHeight() / host.getPrefRowHeight());
		final int itemsPerPage = numberOfColumns * numberOfRows;
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
		space.getColumnConstraints().clear();
		space.getRowConstraints().clear();
	}
	
	@Override
	protected void populateSpace(Integer pageIndex) {
		for (int i = 0; i < numberOfColumns; i++) {
			ColumnConstraints c = new ColumnConstraints();
			c.setPercentWidth(1.0 / numberOfColumns * 100);
			c.setHgrow(Priority.ALWAYS);
			c.setMinWidth(10);
			space.getColumnConstraints().add(c);
		}
		for (int i = 0; i < numberOfRows; i++) {
			RowConstraints c = new RowConstraints();
			c.setPercentHeight(1.0 / numberOfRows * 100);
			c.setVgrow(Priority.ALWAYS);
			c.setMinHeight(10);
			space.getRowConstraints().add(c);
		}
		int colIndex = 0;
		int rowIndex = 0;
		final int itemsPerPage = numberOfColumns * numberOfRows;
	    int page = pageIndex * itemsPerPage;
	    for (int i = page; i < page + itemsPerPage; i++) {
	        Pane pane = host.getTilePane(i);
	        GridPane.setColumnIndex(pane, colIndex);
			GridPane.setRowIndex(pane, rowIndex);
	        GridPane.setHalignment(pane, HPos.LEFT);
	        GridPane.setValignment(pane, VPos.CENTER);
	        space.getChildren().add(pane);
	        colIndex++;
			if (colIndex > numberOfColumns - 1) {
				colIndex = 0;
				rowIndex++;
				if (rowIndex > numberOfRows - 1)
					break;
			}
	    }
	}

}
