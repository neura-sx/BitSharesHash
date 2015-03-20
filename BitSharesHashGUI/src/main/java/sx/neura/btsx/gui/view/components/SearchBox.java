package sx.neura.btsx.gui.view.components;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class SearchBox extends Region {
	
	private static final long TIMER1 = 500;
	private static final long TIMER2 = 5000;
	
	private static final double OPACITY_FULL = 1.0;
	private static final double OPACITY_SEMI = 0.4;
	private static final double OPACITY_NONE = 0;
	
	private static Timer timer = new Timer();
	
	public static void stop() {
		timer.cancel();
		System.out.println("SearchBox timer is closed");
	}
	
	public interface Target {
		public void applySearch(String[] phraseList);
		public void cancelSearch();
	}
	
    private TextField textBox;
    private Button clearButton;
    private Target target;
    private TimerTask timerTask1;
    private TimerTask timerTask2;
    private boolean isMouseOn;
    private String phrase;

	private Runnable runnable1 = new Runnable() {
		@Override
		public void run() {
			if (target == null)
				return;
			clearButton.setOpacity(phrase.length() > 0 ? OPACITY_FULL : OPACITY_NONE);
            if (phrase.length() == 0) {
            	target.cancelSearch();
            } else {
            	target.applySearch(phrase.split(" "));
            }
		}
	};
	
	private Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			clearButton.requestFocus();
			setOpacity(OPACITY_SEMI);
		}
	};

    public SearchBox() {
        setMinHeight(24);
        setMaxHeight(24);
        setPrefSize(150, 24);
        
        textBox = new TextField();
        textBox.textProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	phrase = textBox.getText();
            	if (timerTask1 != null)
            		timerTask1.cancel();
            	timerTask1 = new TimerTask() {
        			@Override
        			public void run() {
        				Platform.runLater(runnable1);
        			}
                };
            	timer.schedule(timerTask1, TIMER1);
            	if (!isMouseOn) {
	            	if (timerTask2 != null)
	            		timerTask2.cancel();
	            	timerTask2 = new TimerTask() {
	        			@Override
	        			public void run() {
	        				Platform.runLater(runnable2);
	        			}
	                };
	            	timer.schedule(timerTask2, TIMER2);
            	}
            }
        });
        
        clearButton = new Button();
        clearButton.setOpacity(OPACITY_NONE);
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent actionEvent) {
                textBox.setText("");
                textBox.requestFocus();
            }
        });
        
        getChildren().addAll(textBox, clearButton);
        setOpacity(OPACITY_SEMI);
        
        setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				isMouseOn = true;
				if (timerTask2 != null)
            		timerTask2.cancel();
				setOpacity(OPACITY_FULL);
				textBox.requestFocus();
			}
		});
        setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				isMouseOn = false;
				if (timerTask2 != null)
            		timerTask2.cancel();
            	timerTask2 = new TimerTask() {
        			@Override
        			public void run() {
        				Platform.runLater(runnable2);
        			}
                };
            	timer.schedule(timerTask2, TIMER2);
			}
		});
    }
    
    @Override 
    protected void layoutChildren() {
        textBox.resize(getWidth(), getHeight());
        clearButton.resizeRelocate(getWidth() - 18, 6, 12, 13);
    }
    
	public void setTarget(Target target) {
		this.target = target;
	}
	
    public String getPromptText() {
		return textBox.getPromptText();
	}
	public void setPromptText(String promptText) {
		textBox.setPromptText(promptText);
	}
	
}
