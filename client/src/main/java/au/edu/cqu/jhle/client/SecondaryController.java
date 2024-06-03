package au.edu.cqu.jhle.client;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        ClientApp.setRoot("primary");
    }
}