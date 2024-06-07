package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.Order;
import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.AddOrderRequest;
import au.edu.cqu.jhle.shared.requests.GetOrderByIdRequest;
import au.edu.cqu.jhle.shared.requests.GetUserByIdRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    
    @FXML
    private Button openOrderLines;
    
    ClientRequestManager requestManager;
    private Order orderDetails;
    private String customerName;
    
    private int customerId;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();
        orderDetails = null;
        
        //populate customer with current user details
        User user = requestManager.getLoggedInUser();
        customerInput.setText(user.getFirstName() + " " + user.getLastName());
        customerId = user.getId();
        openOrderLines.setDisable(true);
        
        this.customerInput.setDisable(true);
        this.totalCostInput.setDisable(true);
        this.totalCostInput.setText("0.0");
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
        // open order details
        OrderLinesController controller = ClientApp.setRoot("orderLines");
        //set order
        controller.setOrder(orderDetails, customerName);
    }
    
    public void setOrder(Order order, String customerName, boolean isFromOrderLines) {
        openOrderLines.setDisable(false);
        if (isFromOrderLines) {
            refreshOrder(order.getId());
            return;
        }
        this.orderDetails = order;
        this.customerName = customerName;
        populateFields();
    }
    
    private void saveOrder() throws IOException {
        //ensure fields are not empty
        if (Utils.isEmpty(deliveryTimeInput)) {
            Utils.createAndShowAlert("Invalid fields", "Fields cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        //get field details
        String deliveryTime = deliveryTimeInput.getText();
        Double cost = Double.parseDouble(totalCostInput.getText());

        boolean newOrder = true;
        if (orderDetails == null) {
            int statusId = 1; //submitted order
            orderDetails = new Order(customerId, statusId, deliveryTime, cost);
        } else {
            orderDetails.setDeliveryTime(deliveryTime);
            orderDetails.setTotalCost(cost);
            newOrder = false;
        }

        //send response
        AddOrderRequest response = requestManager.upsertOrderRequest(new AddOrderRequest(orderDetails));
        if (response.isValid()) {
            //Update order object and customer name from db
            refreshOrder(response.getId());
            
            //Display message saying order was updated/added successfully
            if (newOrder) {
                this.openOrderLines.setDisable(false);
                Utils.createAndShowAlert("Successfully created order", "Order was successfully created!", Alert.AlertType.INFORMATION);
            } else {
                Utils.createAndShowAlert("Successfully updated order", "Order was successfully updated!", Alert.AlertType.INFORMATION);
            }
            return;
        }

        //Failed
        Utils.createAndShowAlert("Failed  updating order", response.getErrorMessage(), Alert.AlertType.ERROR);
        
    }
    
    private void populateFields() {
        orderTitleLabel.setText("Order Details (ID: %s)".formatted(orderDetails.getId()));
        
        customerInput.setText(customerName);
        customerId = orderDetails.getCustomerId();
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
            case 2:
                statusName = "Paid";
                break;
            case 3:
                statusName = "Received";
                break;
        }
        
        return statusName;
    }
    
    private void refreshOrder(int id) {
        try {
            GetOrderByIdRequest getOrderByIdRequest = requestManager.getOrderByIdRequest(new GetOrderByIdRequest(id));
            orderDetails = getOrderByIdRequest.getOrder();
        } catch (Exception ex) {
            System.out.println("Failed to get order by id!");
            ex.printStackTrace();

            Utils.createAndShowAlert("Failed getting order by id!", "Failed to get order by id!", Alert.AlertType.ERROR);
            try {
                ClientApp.setRoot("orders");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        //get customer name from id
        try {
            //Get user by id
            GetUserByIdRequest getUserByIdRequest = requestManager.getUserByIdRequest(new GetUserByIdRequest(orderDetails.getCustomerId()));
            User customer = getUserByIdRequest.getUser();

            this.customerName = customer.getFirstName() + " " + customer.getLastName();

        } catch (Exception ex) {
            System.out.println("Failed to get user for order!");
            ex.printStackTrace();

            Utils.createAndShowAlert("Failed getting user for order!", "Failed to get user for order!", Alert.AlertType.ERROR);
            try {
                ClientApp.setRoot("orders");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        populateFields();
    }
    
}
