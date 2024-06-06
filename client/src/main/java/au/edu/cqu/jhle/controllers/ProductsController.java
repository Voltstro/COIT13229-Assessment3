package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.GetProductsRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    @FXML
    private Button newProductBtn;
    
    private List<Product> productsList = new ArrayList<>();

    private boolean isCustomer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClientRequestManager requestManager = ClientApp.getClientRequestManager();
        User user = requestManager.getLoggedInUser();
        if(user.getRoleId() == 1) {
            newProductBtn.setDisable(true);
            isCustomer = true;
        }

        try {
            GetProductsRequest  getProductsRequest = requestManager.getProductsRequest(new GetProductsRequest());
            productsList = getProductsRequest.getProductList();
        } catch (Exception ex) {
            System.out.println("Failed to get products!");
            ex.printStackTrace();

            Utils.createAndShowAlert("Failed getting products", "Failed to get products!", Alert.AlertType.ERROR);
            try {
                ClientApp.setRoot("home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        
        populateTable();
    }
    
    private void populateTable() {
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

    @FXML
    private void onBack() throws IOException {
        ClientApp.setRoot("home");
    }
    
    private void openProductDetailsPage(Product product) throws IOException {
        if(isCustomer) return;

        //open product details
        ProductDetailController controller = ClientApp.setRoot("productDetail");
        //set selected product
        controller.setProduct(product);
    }
    
}
