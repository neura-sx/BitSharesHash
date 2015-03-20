package test.neura.bts.gui.util;

import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class SampleCharts {
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public static LineChart<Double, Double> createLineChart() {
	        NumberAxis xAxis = new NumberAxis("Values for X-Axis", 0, 3, 1);
	        NumberAxis yAxis = new NumberAxis("Values for Y-Axis", 0, 3, 1);
	        ObservableList<XYChart.Series<Double,Double>> lineChartData = FXCollections.observableArrayList(
	            new LineChart.Series<Double,Double>("Series 1", FXCollections.observableArrayList(
	                new XYChart.Data<Double,Double>(0.0, 1.0),
	                new XYChart.Data<Double,Double>(1.2, 1.4),
	                new XYChart.Data<Double,Double>(2.2, 1.9),
	                new XYChart.Data<Double,Double>(2.7, 2.3),
	                new XYChart.Data<Double,Double>(2.9, 0.5)
	            )),
	            new LineChart.Series<Double,Double>("Series 2", FXCollections.observableArrayList(
	                new XYChart.Data<Double,Double>(0.0, 1.6),
	                new XYChart.Data<Double,Double>(0.8, 0.4),
	                new XYChart.Data<Double,Double>(1.4, 2.9),
	                new XYChart.Data<Double,Double>(2.1, 1.3),
	                new XYChart.Data<Double,Double>(2.6, 0.9)
	            ))
	        );
	        return new LineChart(xAxis, yAxis, lineChartData);
	    }
	    
	 	public static PieChart createPieChart() {
	         ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
	             new PieChart.Data("Sun", 20),
	             new PieChart.Data("IBM", 12),
	             new PieChart.Data("HP", 25),
	             new PieChart.Data("Dell", 22),
	             new PieChart.Data("Apple", 30)
	         );
	        return new PieChart(pieChartData);
	    }
	    
	 	@SuppressWarnings({ "rawtypes", "unchecked" })
		public static ScatterChart createScatterChart() {
	        final Random RANDOM = new Random(29782198273l);
	        NumberAxis xAxis = new NumberAxis("X-Axis", 0, 8, 1);
	        NumberAxis yAxis = new NumberAxis("Y-Axis", -5, 5, 1);
	        ObservableList<XYChart.Series> data = FXCollections.observableArrayList();
	        for (int s=0; s<8; s++) {
	            ObservableList<ScatterChart.Data> seriesData = FXCollections.<ScatterChart.Data>observableArrayList();
	            for (int d=0; d<(8*(RANDOM.nextDouble()*10)); d++) {
	                seriesData.add(new XYChart.Data(8*RANDOM.nextDouble(), -5+(10*RANDOM.nextDouble())));
	            }
	            data.add(new ScatterChart.Series("Product "+(s+1),seriesData));
	        }
	        return new ScatterChart(xAxis, yAxis, data);
	    }

}
