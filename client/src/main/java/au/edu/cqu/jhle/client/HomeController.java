package au.edu.cqu.jhle.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void onOrdersClick() throws IOException {
        ClientApp.setRoot("home");
    }
    
    @FXML
    private void onProductsClick() throws IOException {
        ClientApp.setRoot("products");
    }
    
    @FXML
    private void onDeliverySchedulesClick() throws IOException {
        ClientApp.setRoot("deliverySchedules");
    }
    
}
