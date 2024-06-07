package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.shared.models.DeliverySchedule;
import au.edu.cqu.jhle.shared.requests.GetSchedulesRequest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    private ClientRequestManager requestManager;
    
    private LinkedList<DeliverySchedule> deliverySchedulesList = new LinkedList<>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();
        
        //Get schedules
        try {
            GetSchedulesRequest getSchedulesRequest = new GetSchedulesRequest();
            //send request
            GetSchedulesRequest response = requestManager.sendGetSchedulesRequest(getSchedulesRequest);
            if (response.isValid()) {
                deliverySchedulesList = response.getDeliverySchedulesList();
            }
            
            populateTable();
            
        } catch (IOException e) {
            System.out.println("exception" + e);
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
    
    private void openDeliveryScheduleDetailsPage(DeliverySchedule deliverySchedule) throws IOException {
        //open details
        DeliveryScheduleDetailController controller = ClientApp.setRoot("deliveryScheduleDetail");
        //set selected schedule
        controller.setDeliverySchedule(deliverySchedule);
    }
    
}
