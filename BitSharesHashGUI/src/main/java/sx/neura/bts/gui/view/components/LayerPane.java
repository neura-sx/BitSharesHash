package sx.neura.bts.gui.view.components;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class LayerPane extends StackPane {
	
	public enum Mode {
		VANILLA,
		OPACITY,
		SCALE_X,
		SCALE_Y,
		TRANSLATE_X,
		TRANSLATE_Y,
		ESCALATE_X,
		ESCALATE_Y,
	}

	public interface Responder {
		public void respond();
	}

	private int index;
	private IntegerProperty indexProperty;
	private boolean initialized;
	private boolean ascending;
	private boolean alwaysAscending;
	private Mode mode;
	private boolean simultaneous;
	private boolean reverse;
	private boolean caching;
	private int duration;
	private int durationPerSection;

	private Responder responder;

	{
		index = 0;
		indexProperty = new SimpleIntegerProperty(index);
		initialized = false;
		ascending = false;
		mode = Mode.VANILLA;
		simultaneous = false;
		setReverse(false);
		caching = false;
		duration = 0;
		durationPerSection = 0;
	}
	
	public Node getSelectedNode() {
		return getChildren().get(getIndex());
	}
	
	public void setIndex(int index, Responder responder) {
		this.responder = responder;
		setIndex(index);
	}

	public void setIndex(int index) {
		if (index == this.index) {
			if (responder != null) {
				responder.respond();
				responder = null;
			}
			return;
		}
		boolean isEmptyStart = (this.index < 0);
		this.ascending = (alwaysAscending ||index > this.index);
		this.index = index;
		if (!initialized) {
			if (responder != null)
				responder = null;
			return;
		}
		if (isEmptyStart || mode.equals(Mode.VANILLA))
			swap();
		else
			play();
	}
	public int getIndex() {
		return indexProperty.get();
	}

	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public boolean isSimultaneous() {
		return simultaneous;
	}
	public void setSimultaneous(boolean simultaneous) {
		this.simultaneous = simultaneous;
	}
	
	public boolean isReverse() {
		return reverse;
	}
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	
	public boolean isAlwaysAscending() {
		return alwaysAscending;
	}
	public void setAlwaysAscending(boolean alwaysAscending) {
		this.alwaysAscending = alwaysAscending;
	}
	
	public boolean isCaching() {
		return caching;
	}
	public void setCaching(boolean caching) {
		this.caching = caching;
	}
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getDurationPerSection() {
		return durationPerSection;
	}
	public void setDurationPerSection(int durationPerSection) {
		this.durationPerSection = durationPerSection;
	}

	public IntegerProperty getIndexProperty() {
		return indexProperty;
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		if (initialized || getChildren().size() == 0)
			return;
		ObservableList<Node> nodes = getChildren();
		for (int i = 0; i < nodes.size(); i++)
			nodes.get(i).setVisible(i == index);
		if (index >= 0) {
			Node node = getChildren().get(index);
			if (node instanceof ScrollPane) {
				Region region = (Region) ((ScrollPane) node).getContent();
				prefWidthProperty().bind(region.widthProperty());
			}
		}
		applyIndex();
		initialized = true;
	}
	
	private void applyIndex() {
		indexProperty.set(index);
	}
	
	private DoubleProperty getTransitionProperty(Node node) {
		if (mode.equals(Mode.OPACITY))
			return node.opacityProperty();
		if (mode.equals(Mode.SCALE_X))
			return node.scaleXProperty();
		if (mode.equals(Mode.SCALE_Y))
			return node.scaleYProperty();
		if (mode.equals(Mode.TRANSLATE_X))
			return node.translateXProperty();
		if (mode.equals(Mode.TRANSLATE_Y))
			return node.translateYProperty();
		if (mode.equals(Mode.ESCALATE_X))
			return node.translateXProperty();
		if (mode.equals(Mode.ESCALATE_Y))
			return node.translateYProperty();
		return null;
	}
	
	private double getTransitionValueBefore() {
		if (mode.equals(Mode.TRANSLATE_X))
			return (reverse ? -1 : 1) * getDeltaX();
		if (mode.equals(Mode.TRANSLATE_Y))
			return (reverse ? -1 : 1) * getDeltaY();
		if (mode.equals(Mode.ESCALATE_X))
			return (reverse ? -1 : 1) * (ascending ? 1 : -1) * getDeltaX();
		if (mode.equals(Mode.ESCALATE_Y))
			return (reverse ? -1 : 1) * (ascending ? 1 : -1) * getDeltaY();
		return 0;
	}
	
	private double getTransitionValueOn() {
		if (mode.equals(Mode.TRANSLATE_X))
			return 0;
		if (mode.equals(Mode.TRANSLATE_Y))
			return 0;
		if (mode.equals(Mode.ESCALATE_X))
			return 0;
		if (mode.equals(Mode.ESCALATE_Y))
			return 0;
		return 1;
	}
	
	private double getTransitionValueAfter() {
		if (mode.equals(Mode.TRANSLATE_X))
			return (reverse ? -1 : 1) * getDeltaX();
		if (mode.equals(Mode.TRANSLATE_Y))
			return (reverse ? -1 : 1) * getDeltaY();
		if (mode.equals(Mode.ESCALATE_X))
			return (reverse ? -1 : 1) * (ascending ? -1 : 1) * getDeltaX();
		if (mode.equals(Mode.ESCALATE_Y))
			return (reverse ? -1 : 1) * (ascending ? -1 : 1) * getDeltaY();
		return 0;
	}
	
	private double getDeltaX() {
		return getWidth();
	}
	
	private double getDeltaY() {
		return getHeight();
	}
	
	private void swap() {
		if (index >= 0) {
			Node node = getChildren().get(index);
			if (node instanceof ScrollPane) {
				Region region = (Region) ((ScrollPane) node).getContent();
				double delta = getPrefWidth() - region.getWidth();
				if (delta > 0) {
					prefWidthProperty().unbind();
					prefWidthProperty().bind(region.widthProperty());
				}
			}
		}
		if (getIndex() >= 0) {
			Node node = getChildren().get(getIndex());
			node.setVisible(false);
		}
		applyIndex();
		if (getIndex() >= 0) {
			Node node = getChildren().get(getIndex());
			node.setVisible(true);
			if (node instanceof ScrollPane) {
				Region region = (Region) ((ScrollPane) node).getContent();
				double delta = region.getWidth() - getPrefWidth();
				if (delta > 0) {
					prefWidthProperty().unbind();
					prefWidthProperty().bind(region.widthProperty());
				}
			}
		}
		if (responder != null) {
			responder.respond();
			responder = null;
		}
	}
	
	private void play() {
		if (getIndex() < 0) {
			play2();
			return;
		}
		if (index >= 0) {
			Node node = getChildren().get(index);
			if (node instanceof ScrollPane) {
				final Region region = (Region) ((ScrollPane) node).getContent();
				final double delta = getPrefWidth() - region.getWidth();
				if (delta > 0) {
					prefWidthProperty().unbind();
					Timeline timeline = new Timeline(
						new KeyFrame(Duration.millis(getDuration(delta)),
								new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
											prefWidthProperty().bind(region.widthProperty());
											play1();
									}
								},
								new KeyValue(prefWidthProperty(),
										region.getWidth(), Interpolator.EASE_BOTH)));
					timeline.play();
					return;
				}
			}
		}
		play1();
	}
	
	private void play1() {
		final Node node = getChildren().get(getIndex());
		final double valueAfter = getTransitionValueAfter();
		final double distance = Math.abs(getTransitionProperty(node).get() - valueAfter);
		if (distance > 0) {
			if (caching)
				node.setCache(true);
			Timeline timeline = new Timeline(
					new KeyFrame(Duration.millis(getDuration(distance)),
							new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									if (caching)
										node.setCache(false);
									node.setVisible(false);
									getTransitionProperty(node).set(getTransitionValueOn());
									if (!simultaneous || index < 0)
										play2();
								}
							},
							new KeyValue(getTransitionProperty(node), valueAfter, Interpolator.EASE_BOTH)));
			timeline.play();
		} else {
			node.setVisible(false);
			getTransitionProperty(node).set(getTransitionValueOn());
			if (!simultaneous || index < 0)
				play2();
		}
		if (simultaneous && index >= 0)
			play2();
	}
	
	private void play2() {
		applyIndex();
		if (getIndex() < 0) {
			if (responder != null) {
				responder.respond();
				responder = null;
			}
			return;
		}
		final Node node = getChildren().get(getIndex());
		final double valueBefore = getTransitionValueBefore();
		final double valueOn = getTransitionValueOn();
		final double distance = Math.abs(valueBefore - valueOn);
		getTransitionProperty(node).set(valueBefore);
		node.setVisible(true);
		if (distance > 0) {
			if (caching)
				node.setCache(true);
			Timeline timeline = new Timeline(
					new KeyFrame(Duration.millis(getDuration(distance)),
							new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									if (caching)
										node.setCache(false);
									if (node instanceof ScrollPane) {
										final Region region = (Region) ((ScrollPane) node).getContent();
										final double delta = region.getWidth() - getPrefWidth();
										if (delta > 0) {
											prefWidthProperty().unbind();
											Timeline timeline = new Timeline(
												new KeyFrame(Duration.millis(getDuration(delta)),
														new EventHandler<ActionEvent>() {
															@Override
															public void handle(ActionEvent event) {
																prefWidthProperty().bind(region.widthProperty());
																if (responder != null) {
																	responder.respond();
																	responder = null;
																}
															}
														}, new KeyValue(prefWidthProperty(),
																region.getWidth(), Interpolator.EASE_BOTH)));
											timeline.play();
											return;
										}
									}
									if (responder != null) {
										responder.respond();
										responder = null;
									}
								}
							},
							new KeyValue(getTransitionProperty(node), valueOn, Interpolator.EASE_BOTH)));
			timeline.play();
		} else {
			if (node instanceof ScrollPane) {
				final Region region = (Region) ((ScrollPane) node).getContent();
				final double delta = region.getWidth() - getPrefWidth();
				if (delta > 0) {
					prefWidthProperty().unbind();
					Timeline timeline = new Timeline(
						new KeyFrame(Duration.millis(getDuration(delta)),
								new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										prefWidthProperty().bind(region.widthProperty());
										if (responder != null) {
											responder.respond();
											responder = null;
										}
									}
								}, new KeyValue(prefWidthProperty(),
										region.getWidth(), Interpolator.EASE_BOTH)));
					timeline.play();
					return;
				}
			}
			if (responder != null) {
				responder.respond();
				responder = null;
			}
		}
	}
	
	private int getDuration(double distance) {
		if (durationPerSection > 0)
			return (int) Math.max((Math.sqrt(distance / 250) * durationPerSection), 100);
		return duration;
	}
}
