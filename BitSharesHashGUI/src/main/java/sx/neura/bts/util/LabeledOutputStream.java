package sx.neura.bts.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class LabeledOutputStream extends OutputStream {
	
	private static final long TIMER_DELAY = 5000;

	public static void redirect(Label output, boolean threadSafe) {
		System.setOut(getPrintStream(output, threadSafe));
	}
	
	public static void redirect() {
		System.setOut(System.out);
	}
	
	public static PrintStream getPrintStream(Label output, boolean threadSafe) {
		return new PrintStream(new LabeledOutputStream(output, threadSafe), true);
	}
	
	private static Timer timer = new Timer();
	
	public static void stop() {
		timer.cancel();
		timer.purge();
		timer = null;
		redirect();
		System.out.println(String.format("%s timer is now closed", LabeledOutputStream.class.getSimpleName()));
	}
	
	private String text;
	private String post;
	private Label output;
	private TimerTask timerTask;
	private boolean threadSafe;
	
	{
		text = "";
	}
	
	private Runnable runnable = () -> {
		output.setText("");
	};
	
	public LabeledOutputStream(Label output, boolean threadSafe) {
		this.output = output;
		this.threadSafe = threadSafe;
	}
	
    @Override
    public void write(int b) throws IOException {
        appendText(String.valueOf((char) b));
    }
    
    private void appendText(String s) {
		if (s.equals("\r") || s.equals("\n")) {
			if (!text.isEmpty()) {
				if (timerTask != null)
            		timerTask.cancel();
            	timerTask = new TimerTask() {
        			@Override
        			public void run() {
        				Platform.runLater(runnable);
        			}
                };
                if (timer != null)
                	timer.schedule(timerTask, TIMER_DELAY);
                post = new String(text);
                if (threadSafe)
                	Platform.runLater(() -> output.setText(post));
                else
                	output.setText(post);
                text = "";
			}
    		return;
    	}
		text += s;
	}

}
