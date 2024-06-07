package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.DeliverySchedule;
import au.edu.cqu.jhle.shared.requests.AddScheduleRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeliveryScheduleDetailController implements Initializable {

    ClientRequestManager requestManager;
    @FXML
    private Label scheduleTitleLabel;
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
        requestManager = ClientApp.getClientRequestManager();
        deliveryScheduleDetails = null;
    }

    @FXML
    private void onReturnToList() throws IOException {
        ClientApp.setRoot("deliverySchedules");
    }

    @FXML
    private void onSaveDeliverySchedule() throws IOException {
        saveDeliverySchedule();
    }

    public void setDeliverySchedule(DeliverySchedule deliverySchedule) {
        this.deliveryScheduleDetails = deliverySchedule;
        populateFields();
    }

    private void populateFields() {
        scheduleTitleLabel.setText("Schedule Details (ID: %s)".formatted(deliveryScheduleDetails.getId()));
        postcodeInput.setText(String.valueOf(deliveryScheduleDetails.getPostcode()));
        dayInput.setText(deliveryScheduleDetails.getDay());
        costInput.setText(deliveryScheduleDetails.getCost().toString());
    }

    private void saveDeliverySchedule() throws IOException {
        try {
            //Ensure fields are not empty
            if (Utils.isEmpty(postcodeInput) || Utils.isEmpty(dayInput) || Utils.isEmpty(costInput)) {
                Utils.createAndShowAlert("Invalid fields", "Fields cannot be empty!", Alert.AlertType.ERROR);
                return;
            }

            //Get field details
            String postcode = postcodeInput.getText();
            String day = dayInput.getText();
            Double cost = Double.parseDouble(costInput.getText());

            boolean newSchedule = true;
            if (deliveryScheduleDetails == null) {
                deliveryScheduleDetails = new DeliverySchedule(postcode, day, cost);
            } else {
                deliveryScheduleDetails.setPostcode(postcode);
                deliveryScheduleDetails.setDay(day);
                deliveryScheduleDetails.setCost(cost);
                newSchedule = false;
            }

            //Send response
            AddScheduleRequest response = requestManager.upsertScheduleRequest(new AddScheduleRequest(deliveryScheduleDetails));
            if (response.isValid()) {
                //Display message saying schedule was updated/added successfully
                if (newSchedule) {
                    Utils.createAndShowAlert("Successfully created schedule", "Schedule was successfully created!", Alert.AlertType.INFORMATION);
                } else {
                    Utils.createAndShowAlert("Successfully updated schedule", "Schedule was successfully updated!", Alert.AlertType.INFORMATION);
                }

                ClientApp.setRoot("deliverySchedules");
                return;
            }

            //Failed
            Utils.createAndShowAlert("Failed updating schedule", response.getErrorMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException ex) {
            //Number formatting error
            Utils.createAndShowAlert("Invalid fields", "Cost must be a valid number!", Alert.AlertType.INFORMATION);
        }
    }
}
