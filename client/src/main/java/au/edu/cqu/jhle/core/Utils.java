package au.edu.cqu.jhle.core;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class Utils {
    /**
     * Creates and shows an alert
     *
     * @param title
     * @param description
     * @param alertType
     */
    public static ButtonType createAndShowAlert(String title, String description, Alert.AlertType alertType) {
        //creates the interface of confirmation to the user
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(description);
        alert.showAndWait();

        return alert.getResult();
    }

    /**
     * Check to make sure a String is empty
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        return text.isEmpty() || text.isBlank();
    }

    /**
     * Check to make sure a text field is empty
     *
     * @param textField
     * @return
     */
    public static boolean isEmpty(TextField textField) {
        return isEmpty(textField.getText());
    }
}
