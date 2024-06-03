package au.edu.cqu.jhle.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class DeliveryScheduleDetailController implements Initializable {
    
    @FXML
    private TextField idInput;
    
    @FXML
    private TextField postcodeInput;
    
    @FXML
    private TextField dayInput;
    
    @FXML
    private TextField costInput;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void onReturnToList() throws IOException {
        ClientApp.setRoot("products");
    }
    
    @FXML
    private void onSaveDeliverySchedule() throws IOException {
        saveDeliverySchedule();
    }
    
    @FXML
    private void onRemoveDeliverySchedule() throws IOException {
        removeDeliverySchedule();
    }
    
    private void saveDeliverySchedule() {
        
    }
    
    private void removeDeliverySchedule() {
        
    }
}
