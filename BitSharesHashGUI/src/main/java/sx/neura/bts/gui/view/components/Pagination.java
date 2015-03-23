package sx.neura.bts.gui.view.components;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import sx.neura.bts.gui.view.Page;

public abstract class Pagination extends javafx.scene.control.Pagination {
	
	private static final long TIMER_DELAY = 500;
	private static Timer timer = new Timer();
	
	public static void stop() {
		timer.cancel();
		timer.purge();
		timer = null;
		System.out.println(String.format("%s timer is now closed", Pagination.class.getSimpleName()));
	}
	
	protected Host host;
	private TimerTask timerTask;
	
	private StackPane stack;
	private Label label;
	
	
	public interface Host {
		public Page getPage();
		public Label getStatusUI();
		public Node getFwdUI();
		public Node getBckUI();
		public double getPrefColumnWidth();
		public double getPrefRowHeight();
		public int getListSize();
		public Pane getTilePane(int index);
		public boolean isSearchOn();
		public String getEmptyText();
	}
	
	{
		label = new Label();
        label.getStyleClass().add("sx-text-small");
        label.visibleProperty().bind(getSpace().visibleProperty().not());
		stack = new StackPane(label, getSpace());
		stack.getStyleClass().add("sx-list-background");
	}
	
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		if (host != null && getPageFactory() == null) {
			setPageFactory((Integer pageIndex) -> {
				return buildPage(pageIndex);
			});
			doReset();
			host.getPage().getPane().layoutBoundsProperty().addListener(
					(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) -> {
						doResetWithTimerTask();
			});
		}
	}
	
	public void setHost(Host host) {
		this.host = host;
	}
	
	public void turnPageBck() {
		turnPage(-1);
	}
	public void turnPageFwd() {
		turnPage(1);
	}
	
	public void reset() {
		if (getPageFactory() == null)
			return;
		doReset();
	}
	
	private void turnPage(int step) {
		setCurrentPageIndex(getCurrentPageIndex() + step);
		updateStatus();
	}
	
	private void updateStatus() {
		host.getStatusUI().setText(String.format("%d of %d", getCurrentPageIndex() + 1, getPageCount()));
		host.getBckUI().setDisable(getCurrentPageIndex() <= 0);
		host.getFwdUI().setDisable(getCurrentPageIndex() >= getPageCount() - 1);
	}
	
	private void doResetWithTimerTask() {
		if (!getSpace().getChildren().isEmpty()) {
			clearSpace(false);
			label.setText("resizing..");
		}
		if (timerTask != null)
			timerTask.cancel();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> doReset());
			}
        };
        if (timer != null)
        	timer.schedule(timerTask, TIMER_DELAY);
	}
	
	private void doReset() {	
		boolean hasItems = (host.getListSize() > 0);
		host.getStatusUI().setVisible(hasItems);
		host.getBckUI().setVisible(hasItems);
		host.getFwdUI().setVisible(hasItems);
		int pageCount = getNumberOfPages();
		if (pageCount != getPageCount()) {
			System.out.println(String.format(">>>>>>> page count reset <<<<<<<< %s", host.getPage().getClass().getName()));
			setPageCount(pageCount);
			setCurrentPageIndex(0);
		} else {
			System.out.println(String.format(">>>>>>> space population reset <<<<<<<< %s", host.getPage().getClass().getName()));
			buildPage(getCurrentPageIndex());
		}
		updateStatus();
	}
	
	private Pane buildPage(Integer pageIndex) {
		if (host.getListSize() > 0) {
			clearSpace(true);
			populateSpace(pageIndex);
		} else {
			clearSpace(false);
			label.setText(host.isSearchOn() ? "The search result is empty" :
				(host.getEmptyText() != null ? host.getEmptyText() : "The list is empty"));
		}
        return stack;
	}
	
	protected abstract Pane getSpace();
	protected abstract void clearSpace(boolean visible);
	protected abstract void populateSpace(Integer pageIndex);
	protected abstract int getNumberOfPages();
	
}