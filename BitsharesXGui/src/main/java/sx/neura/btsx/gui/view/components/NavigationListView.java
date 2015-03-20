package sx.neura.btsx.gui.view.components;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import sx.neura.btsx.gui.view.Screen;

public class NavigationListView extends ListView<Screen> {

	public NavigationListView() {
		setCellFactory(new Callback<ListView<Screen>, ListCell<Screen>>() {
            @Override 
            public ListCell<Screen> call(ListView<Screen> list) {
                return new NavigationCell();
            }
        });
	}
	
	private static class NavigationCell extends ListCell<Screen> {
        @Override
        protected void updateItem(Screen item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null)
            	setText(item.getName());
        }
    }
}
