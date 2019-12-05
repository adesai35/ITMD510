package Common;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Common {

	public void NotificationBox(String tital, String des) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(tital);
		alert.setHeaderText(null);
		alert.setContentText(des);

		Optional<ButtonType> result = alert.showAndWait();
	}
	
	public boolean isNumeric(String str) {
		// null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
	}
	
}
