package sx.neura.bts.gui.view.components.depth;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.shape.Line;

public class MarketDepthChart extends AreaChart<Number, Number> {
	
	public MarketDepthChart(@NamedArg("xAxis") Axis<Number> xAxis, @NamedArg("yAxis") Axis<Number> yAxis) {
        super(xAxis,yAxis, FXCollections.<Series<Number,Number>>observableArrayList());
    }
	
	private Line line;
	private Double feedPrice;

	@Override
	protected void layoutPlotChildren() { 
		super.layoutPlotChildren();
		if (line == null) {
			line = new Line();
			line.getStyleClass().add("sx-feed-price");
			line.setStartY(0);
		}
		line.setEndY(getYAxis().getDisplayPosition(0));
		if (feedPrice != null)
			line.setLayoutX(((int) getXAxis().getDisplayPosition(feedPrice)) + 0.5);
		if (!getPlotChildren().contains(line))
			getPlotChildren().add(line);
		
	}
	
	public void setFeedPrice(Double feedPrice) {
		this.feedPrice = feedPrice;
		if (line != null)
			line.setLayoutX(getXAxis().getDisplayPosition(feedPrice));
	}

}
