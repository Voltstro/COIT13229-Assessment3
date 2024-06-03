package au.edu.cqu.jhle.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DeliverySchedulesController implements Initializable {

    @FXML
    private TableView<Product> deliverySchedulesTable;
    
    @FXML
    private TableColumn<Product, Integer> idColumn;
    
    @FXML
    private TableColumn<Product, Integer> postcodeColumn;
    
    @FXML
    private TableColumn<Product, String> dayColumn;
    
    @FXML
    private TableColumn<Product, Double> costColumn;
    
    private ArrayList<Product> deliverySchedulesList = new ArrayList<>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private void populateTable() {
        
        //Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        postcodeColumn.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        //Convert ArrayList to an ObservableList
//        ObservableList<
        
        //Set the items for the TableView
        
        //Add listener on row click
    }
    
}
