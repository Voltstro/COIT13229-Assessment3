package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.DeliverySchedule;
import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.GetSchedulesRequest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class DeliverySchedulesController implements Initializable {

    @FXML
    private TableView<DeliverySchedule> deliverySchedulesTable;
    
    @FXML
    private TableColumn<DeliverySchedule, Integer> idColumn;
    
    @FXML
    private TableColumn<DeliverySchedule, Integer> postcodeColumn;
    
    @FXML
    private TableColumn<DeliverySchedule, String> dayColumn;
    
    @FXML
    private TableColumn<DeliverySchedule, Double> costColumn;
    
    @FXML
    private Button newScheduleBtn;
    
    private ArrayList<DeliverySchedule> deliverySchedulesList = new ArrayList<>();
    
    private boolean isCustomer;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClientRequestManager requestManager = ClientApp.getClientRequestManager();
        
        User user = requestManager.getLoggedInUser();
        if (user.getRoleId() == 1) {
            newScheduleBtn.setDisable(true);
            isCustomer = true;
        }
        //Get schedules
        try {
            GetSchedulesRequest getSchedulesRequest = new GetSchedulesRequest();
            //send request
            GetSchedulesRequest response = requestManager.sendGetSchedulesRequest(getSchedulesRequest);
            if (response.isValid()) {
                deliverySchedulesList = response.getDeliverySchedulesList();
            }
            
            populateTable();
            
        } catch (Exception ex) {
            System.out.println("Failed to get schedules!");
            ex.printStackTrace();
            
            Utils.createAndShowAlert("Failed getting schedules", "Failed to get schedules", Alert.AlertType.ERROR);
            
            try {
                ClientApp.setRoot("home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private void populateTable() {
        
        //Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        postcodeColumn.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        //Convert LinkedList to an ObservableList
        ObservableList<DeliverySchedule> observableDeliverySchedulesList = FXCollections.observableArrayList(deliverySchedulesList);
        
        //Set the items for the TableView
        deliverySchedulesTable.setItems(observableDeliverySchedulesList);
        
        //Add listener on row click
        deliverySchedulesTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                DeliverySchedule selectedSchedule = deliverySchedulesTable.getSelectionModel().getSelectedItem();               
                if (selectedSchedule != null) {
                    try {
                        openDeliveryScheduleDetailsPage(selectedSchedule);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    @FXML
    private void onAddNew() throws IOException {
        ClientApp.setRoot("deliveryScheduleDetail");
    }
    
    @FXML
    private void onBack() throws IOException {
        ClientApp.setRoot("home");
    }
    
    private void openDeliveryScheduleDetailsPage(DeliverySchedule deliverySchedule) throws IOException {
        if (isCustomer) return;
        //open details
        DeliveryScheduleDetailController controller = ClientApp.setRoot("deliveryScheduleDetail");
        //set selected schedule
        controller.setDeliverySchedule(deliverySchedule);
    }
    
}
