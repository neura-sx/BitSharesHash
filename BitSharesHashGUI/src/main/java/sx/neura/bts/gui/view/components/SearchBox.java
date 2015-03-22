package sx.neura.bts.gui.view.components;

import java.util.List;
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

public class SearchBox<T> extends Region {
	
	private static final long TIMER_DELAY_1 = 500;
	private static final long TIMER_DELAY_2 = 5000;
	
	private static final double OPACITY_FULL = 1.0;
	private static final double OPACITY_SEMI = 0.4;
	private static final double OPACITY_NONE = 0;
	
	private static Timer timer = new Timer();
	
	public static void stop() {
		timer.cancel();
		timer.purge();
		timer = null;
		System.out.println(String.format("%s timer is now closed", SearchBox.class.getSimpleName()));
	}
	
	public interface Host<T> {
		public List<T> getList();
		public boolean isSearchMatch(T item, String[] phraseList);
		public void apply();
	}
	
    private TextField textBox;
    private Button clearButton;
    private Host<T> host;
    private TimerTask timerTask1;
    private TimerTask timerTask2;
    private boolean isMouseOn;
    private String phrase;
    private String[] phraseList;
    private List<T> items;

	private Runnable runnable1 = () -> {
        if (phrase.length() > 0) {
        	phraseList = phrase.split(" ");
        	applySearch();
        	clearButton.setOpacity(OPACITY_FULL);
        } else {
        	cancelSearch();
        	clearButton.setOpacity(OPACITY_NONE);
        }
	};
	
	private Runnable runnable2 = () -> {
		turnOff();
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
            	if (phrase.length() > 0) {
	            	timerTask1 = new TimerTask() {
	        			@Override
	        			public void run() {
	        				Platform.runLater(runnable1);
	        			}
	                };
	                if (timer != null)
	                	timer.schedule(timerTask1, TIMER_DELAY_1);
            	} else {
            		Platform.runLater(runnable1);
            	}
            	if (!isMouseOn) {
	            	if (timerTask2 != null)
	            		timerTask2.cancel();
	            	timerTask2 = new TimerTask() {
	        			@Override
	        			public void run() {
	        				Platform.runLater(runnable2);
	        			}
	                };
	                if (timer != null)
	                	timer.schedule(timerTask2, TIMER_DELAY_2);
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
				turnOn();
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
            	timer.schedule(timerTask2, TIMER_DELAY_2);
			}
		});
    }
    
    private void applySearch() {
		if (phraseList == null)
			return;
		final int size = host.getList().size();
		for (int i = size - 1; i >= 0; i--) {
			if (!host.isSearchMatch(host.getList().get(i), phraseList))
				host.getList().remove(host.getList().get(i));
		}
		for (T item : items) {
			if (!host.getList().contains(item)
					&& host.isSearchMatch(item, phraseList)) {
				host.getList().add(item);
			}
		}
		host.apply();
	}
	
	private void cancelSearch() {
		for (T item : items) {
			if (!host.getList().contains(item))
				host.getList().add(item);
		}
		host.apply();
	}
    
    @Override 
    protected void layoutChildren() {
        textBox.resize(getWidth(), getHeight());
        clearButton.resizeRelocate(getWidth() - 18, 6, 12, 13);
    }
    
	public void setHost(Host<T> host) {
		this.host = host;
	}
	
	public void setItems(List<T> items) {
		this.items = items;
		if (items.size() > 0) {
			applySearch();
			setVisible(true);
		} else {
			textBox.setText("");
			setVisible(false);
		}
	}
	public List<T> getItems() {
		return items;
	}
	
    public String getPromptText() {
		return textBox.getPromptText();
	}
	public void setPromptText(String promptText) {
		textBox.setPromptText(promptText);
	}
	
	public boolean isSearchOn() {
		return (items != null && items.size() > host.getList().size());
	}
	
	public void turnOn() {
		setOpacity(OPACITY_FULL);
		textBox.requestFocus();
	}
	public void turnOff() {
		clearButton.requestFocus();
		setOpacity(OPACITY_SEMI);
	}
}
