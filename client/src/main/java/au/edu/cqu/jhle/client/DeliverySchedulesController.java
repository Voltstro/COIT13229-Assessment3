package au.edu.cqu.jhle.client;

import au.edu.cqu.jhle.shared.models.DeliverySchedule;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
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
    
    private ArrayList<DeliverySchedule> deliverySchedulesList = new ArrayList<>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Sample data to test
        deliverySchedulesList.add(new DeliverySchedule(1, 4870, "Monday", 25.0));
        deliverySchedulesList.add(new DeliverySchedule(2, 4868, "Tuesday", 10.0));
        
        populateTable();
    }
    
    private void populateTable() {
        
        //Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        postcodeColumn.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        //Convert ArrayList to an ObservableList
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
