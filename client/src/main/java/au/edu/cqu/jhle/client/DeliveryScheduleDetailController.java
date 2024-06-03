package au.edu.cqu.jhle.client;

import au.edu.cqu.jhle.shared.models.DeliverySchedule;

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
    
    private DeliverySchedule deliveryScheduleDetails;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void onReturnToList() throws IOException {
        ClientApp.setRoot("deliverySchedules");
    }
    
    @FXML
    private void onSaveDeliverySchedule() throws IOException {
        saveDeliverySchedule();
    }
    
    @FXML
    private void onRemoveDeliverySchedule() throws IOException {
        removeDeliverySchedule();
    }
    
    public void setDeliverySchedule(DeliverySchedule deliverySchedule) {
        this.deliveryScheduleDetails = deliverySchedule;
        populateFields();
    }
    
    private void populateFields() {
        idInput.setText(String.valueOf(deliveryScheduleDetails.getId()));
        postcodeInput.setText(String.valueOf(deliveryScheduleDetails.getPostcode()));
        dayInput.setText(deliveryScheduleDetails.getDay());
        costInput.setText(deliveryScheduleDetails.getCost().toString());
    }
    
    private void saveDeliverySchedule() {
        
    }
    
    private void removeDeliverySchedule() {
        
    }
}
