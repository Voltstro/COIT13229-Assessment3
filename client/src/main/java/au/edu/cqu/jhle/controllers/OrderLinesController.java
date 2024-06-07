package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.Order;
import au.edu.cqu.jhle.shared.models.OrderLine;
import au.edu.cqu.jhle.shared.models.Product;
import au.edu.cqu.jhle.shared.requests.GetOrderLinesForOrderRequest;
import au.edu.cqu.jhle.shared.requests.GetProductByIdRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class OrderLinesController implements Initializable {
    
    @FXML
    private TableView<OrderLineWithDetails> orderLinesTable;
    
    @FXML
    private TableColumn<OrderLineWithDetails, Integer> idColumn;
    
    @FXML
    private TableColumn<OrderLineWithDetails, String> productNameColumn;
    
    @FXML
    private TableColumn<OrderLineWithDetails, Integer> quantityColumn;
    
    @FXML
    private TableColumn<OrderLineWithDetails, Double> costColumn;
    
    @FXML
    private Label orderLinesTitle;
    
    @FXML
    private Button newOrderLineBtn;
    
    private List<OrderLineWithDetails> orderLineWithDetailsList = new ArrayList<>();
    
    private Order order;
    private String customerName;
    ClientRequestManager requestManager;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();

    }
    
    @FXML
    private void onAddNew() throws IOException {
        //open order line details
        OrderLineDetailController controller = ClientApp.setRoot("orderLineDetail");
        //set selected order line
        controller.setOrder(order, customerName);
    }
    
    @FXML void onBack() throws IOException {
        returnToOrder();
    }
    
    public void setOrder(Order order, String customerName) {
        this.order = order;
        this.customerName = customerName;
        
        orderLinesTitle.setText("Order Lines for Order (ID: %s)".formatted(order.getId()));
        
        try {
            //Get all order lines for order
            GetOrderLinesForOrderRequest getOrderLinesRequest = requestManager.getOrderLinesForOrderRequest(new GetOrderLinesForOrderRequest(order.getId()));

            //Convert order lines into custom OrderLineWithDetails object
            for (OrderLine orderLine: getOrderLinesRequest.getOrderLinesList()) {
                OrderLineWithDetails orderLineWithDetails = new OrderLineWithDetails(orderLine);
                orderLineWithDetailsList.add(orderLineWithDetails);
            }
            
        } catch (Exception ex) {
            System.out.println("Failed to get order lines!");
            ex.printStackTrace();
            
            Utils.createAndShowAlert("Failed getting order lines", "Failed to get order lines!", Alert.AlertType.ERROR);
            try {
                returnToOrder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            return;
        }
        
        populateTable();
    }
    
    private void returnToOrder() throws IOException {
        // open order details
        OrderDetailController controller = ClientApp.setRoot("orderDetail");
        //set order
        controller.setOrder(order, customerName, true);
    }
    
    private void populateTable() {
        //Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        //Convert the ArrayList to an ObservableList
        ObservableList<OrderLineWithDetails> observableOrderLinesList = FXCollections.observableArrayList(orderLineWithDetailsList);
        
        //Set the items for the TableView
        orderLinesTable.setItems(observableOrderLinesList);
        
        //Add listener on row click
        orderLinesTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                OrderLineWithDetails selectedOrderLine = orderLinesTable.getSelectionModel().getSelectedItem();
                if (selectedOrderLine != null) {
                    try {
                        openOrderLineDetailPage(selectedOrderLine.orderLine);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    public void openOrderLineDetailPage(OrderLine orderLine) throws IOException {
        //open order line details
        OrderLineDetailController controller = ClientApp.setRoot("orderLineDetail");
        //set selected order line
        controller.setOrderLine(orderLine, order, customerName);
    }
    
    public class OrderLineWithDetails {
        public OrderLineWithDetails(OrderLine orderLine) {
            this.id = orderLine.getId();
            this.orderId = orderLine.getOrderId();
            this.quantity = orderLine.getQuantity();
            this.cost = orderLine.getCost();
            this.orderLine = orderLine;
            
            //Get product name from db
            try {
                GetProductByIdRequest getProductByIdRequest = requestManager.getProductByIdRequest(new GetProductByIdRequest(orderLine.getProductId()));
                Product product = getProductByIdRequest.getProduct();
                
                this.productName = product.getName();
            } catch (Exception ex) {
                System.out.println("Failed to get product for order line!");
                ex.printStackTrace();
                
                Utils.createAndShowAlert("Failed getting product for order line!", "Failed to get product for order line!", Alert.AlertType.ERROR);
                try {
                    returnToOrder();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        private int id;

        private int orderId;

        private int quantity;

        private Double cost;
        
        private String productName;
        
        private OrderLine orderLine;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public OrderLine getOrderLine() {
            return orderLine;
        }

        public void setOrderLine(OrderLine orderLine) {
            this.orderLine = orderLine;
        }
        
    }
    
}
