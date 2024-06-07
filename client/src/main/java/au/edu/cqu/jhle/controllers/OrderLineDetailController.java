package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.Order;
import au.edu.cqu.jhle.shared.models.OrderLine;
import au.edu.cqu.jhle.shared.models.Product;
import au.edu.cqu.jhle.shared.requests.GetProductsRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;


public class OrderLineDetailController implements Initializable {
    
    @FXML
    private TextField quantityInput;
    
    @FXML
    private TextField costInput;
    
    @FXML
    private Button onSaveOrderLine;
    
    @FXML
    private ComboBox<Product> productCombo;
    
    @FXML
    private Label orderLineTitleLabel;
    
    private ClientRequestManager requestManager;
    private OrderLine orderLine;
    private Order order;
    private String customerName;
    private List<Product> productsList = new ArrayList<>();
    
    /**
     * Initializes the controller class
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();
        
        //get list of products
        getProducts();
        
        this.orderLine = null;
    }
    
    public void setOrder(Order order, String customerName) {
        this.order = order;
        this.customerName = customerName;
        
        orderLineTitleLabel.setText("New Order for Order (ID: %s)".formatted(order.getId()));
    }
    
    public void setOrderLine(OrderLine orderLine, Order order, String customerName) {
        this.orderLine = orderLine;
        
        this.quantityInput.setText(String.valueOf(orderLine.getQuantity()));
        this.costInput.setText(orderLine.getCost().toString());
        this.costInput.setDisable(true);
        
        //Find matching product
        for (Product product : productsList) {
            if (product.getId() == orderLine.getProductId()) {
                productCombo.getSelectionModel().select(product);
                break;
            }
        }
        
        orderLineTitleLabel.setText("Order Line (ID: %s) for Order (ID: %s)".formatted(orderLine.getId(), order.getId()));

        this.order = order;
        this.customerName = customerName;
    }
    
    @FXML
    private void onReturnToList() throws IOException {
        returnToList();
    }
    
    @FXML
    private void onSaveOrderLine() throws IOException {
        
    }
    
    private void returnToList() throws IOException {
        //open order lines list
        OrderLinesController controller = ClientApp.setRoot("orderLines");
        //set order
        controller.setOrder(order, customerName);
    }
    
    private void getProducts() {
        try {
            GetProductsRequest getProductsRequest = requestManager.getProductsRequest(new GetProductsRequest());
            productsList = getProductsRequest.getProductList();
            
            ObservableList<Product> observableProducts = FXCollections.observableArrayList(productsList);
            productCombo.setItems(observableProducts);
            productCombo.setConverter(new StringConverter<>() {
                @Override
                public String toString(Product product) {
                    return product.getName();
                }
                
                //Not needed for combobox but needed for compilation
                @Override
                public Product fromString(String string) {
                    return null;
                }
            });
            productCombo.getSelectionModel().select(0);
        } catch (Exception ex) {
            System.out.println("Failed to get products!");
            ex.printStackTrace();
            
            Utils.createAndShowAlert("Failed getting products", "Failed to get products!", Alert.AlertType.ERROR);
            try {
                returnToList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}