package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.shared.models.Order;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class OrderDetailController implements Initializable {
    
    @FXML
    private Label orderTitleLabel;
    
    @FXML
    private TextField customerInput;
    
    @FXML
    private TextField deliveryTimeInput;
    
    @FXML
    private TextField totalCostInput;
    
    @FXML
    private Label orderStatusLabel;
    
    ClientRequestManager requestManager;
    private Order orderDetails;
    private String customerName;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();
        orderDetails = null;
        
        //populate customer with current user details
    }
    
    @FXML
    private void onReturnToList() throws IOException {
        ClientApp.setRoot("orders");
    }
    
    @FXML
    private void onSaveOrder() throws IOException {
        saveOrder();
    }
    
    @FXML
    private void onOpenOrderLines() throws IOException {
        System.out.println("Opening order lines");
    }
    
    public void setOrder(Order order, String customerName) {
        this.orderDetails = order;
        this.customerName = customerName;
        populateFields();
    }
    
    private void saveOrder() throws IOException {
        
    }
    
    private void populateFields() {
        orderTitleLabel.setText("Order Details (ID: %s)".formatted(orderDetails.getId()));
        
        customerInput.setText(customerName);
        customerInput.setDisable(true);
        deliveryTimeInput.setText(orderDetails.getDeliveryTime());
        totalCostInput.setText(orderDetails.getTotalCost().toString());
        totalCostInput.setDisable(true);
        orderStatusLabel.setText("Order Status: %s".formatted(getOrderStatus(orderDetails.getStatusId())));
    }
    
    private String getOrderStatus(int statusId) {
        String statusName = "";
        switch (statusId) {
            case 1:
                statusName = "Submitted";
                break;
        }
        
        return statusName;
    }
    
}
