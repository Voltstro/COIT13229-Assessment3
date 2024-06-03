package au.edu.cqu.jhle.client;

import au.edu.cqu.jhle.shared.models.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ProductsController implements Initializable {
    
    @FXML
    private TableView<Product> productsTable;
    
    @FXML
    private TableColumn<Product, Integer> idColumn;
    
    @FXML
    private TableColumn<Product, String> nameColumn;
    
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    
    @FXML
    private TableColumn<Product, String> unitColumn;
    
    @FXML
    private TableColumn<Product, Double> unitPriceColumn;
    
    private ArrayList<Product> productsList = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // Sample data to test
        productsList.add(new Product(1, "test", 5, "Litres", 25.50, "ingredients"));
        productsList.add(new Product(2, "another product", 250, "ml", 13.0, "test"));
        
        populateTable();
    }
    
    private void populateTable() {
        System.out.println(productsList.toString());
        
        // Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Convert the ArrayList to an ObservableList
        ObservableList<Product> observableProductsList = FXCollections.observableArrayList(productsList);
        
        // Set the items for the TableView
        productsTable.setItems(observableProductsList);
        
        // Add listener on row click
        productsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();               
                if (selectedProduct != null) {
                    try {
                        openProductDetailsPage(selectedProduct);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    @FXML
    private void onAddNew() throws IOException {
        ClientApp.setRoot("productDetail");
    }
    
    private void openProductDetailsPage(Product product) throws IOException {
        //open product details
        ProductDetailController controller = ClientApp.setRoot("productDetail");
        //set selected product
        controller.setProduct(product);
    }
    
}
