package sx.neura.bts.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class Util {
	
	public static boolean c(String s, String i) {
		return s.toLowerCase().contains(i.toLowerCase());
	}

	public static String repeat(String s, int times) {
		String retval = "";
		for (int i = 0; i < times; i++)
			retval += s;
		return retval;
	}

	public static boolean isValidString(String s) {
		return !((s == null) || s.isEmpty());
	}

	public static String crop(String s, int limit) {
		if (s.length() <= limit)
			return s;
		return String.format("%s..", s.substring(0, limit - 2));
	}
	
	public static String addAttribute(String s, String name) {
		if (!s.isEmpty())
			s += ", ";
		s += name;
		return s;
	}

	public static String readStringFromPath(Path path) throws Exception {
		FileReader fr = new FileReader(path.toString());
		BufferedReader br = new BufferedReader(fr);
		String content;
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			content = sb.toString();
		} finally {
			br.close();
		}
		return content;
	}
	
	public static String capitalizeFirstChar(String s) {
		if (!isValidString(s))
			return s;
		if (s.length() > 1)
			return Character.toUpperCase(s.charAt(0)) + s.substring(1);
		return s.toUpperCase();
	}
	
	public static void resetTableViewScroll(TableView<?> tv) {
		tv.scrollTo(tv.getItems().size() - 1);
		Platform.runLater(() -> {
			tv.scrollTo(0);
		});
	}
    
    public static String getSubstring(String s, int lenght) {
		if (s.length() < lenght)
			return s;
		return s.substring(0, lenght);
	}
    
    public static void manageToggleGroup(ToggleGroup group) {
		manageToggleGroup(group, null);
	}
	
    public static void manageToggleGroup(ToggleGroup group, Runnable runnable) {
		for (Toggle toggle : group.getToggles()) {
			((Node) toggle).addEventFilter(
					MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
						if (toggle.equals(group.getSelectedToggle())) {
							event.consume();
							if (runnable != null)
								runnable.run();
						}
					});
		}
	}
}
