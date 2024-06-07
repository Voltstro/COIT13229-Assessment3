package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.Order;
import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.GetOrdersRequest;
import au.edu.cqu.jhle.shared.requests.GetUserByIdRequest;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    ClientRequestManager requestManager;
    @FXML
    private TableView<OrderWithDetails> ordersTable;
    @FXML
    private TableColumn<OrderWithDetails, Integer> idColumn;
    @FXML
    private TableColumn<OrderWithDetails, String> nameColumn;
    @FXML
    private TableColumn<OrderWithDetails, String> deliveryTimeColumn;
    @FXML
    private TableColumn<OrderWithDetails, Double> costColumn;
    @FXML
    private TableColumn<OrderWithDetails, String> statusColumn;
    @FXML
    private Button newOrderBtn;
    private List<OrderWithDetails> orderWithDetailsList = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();

        //TODO handle user role

        try {
            //Get all orders
            GetOrdersRequest getOrdersRequest = requestManager.getOrdersRequest(new GetOrdersRequest());

            //Convert orders into custom OrdersWithDetails object
            for (Order order : getOrdersRequest.getOrderList()) {
                OrderWithDetails orderWithDetails = new OrderWithDetails(order);
                orderWithDetailsList.add(orderWithDetails);
            }
        } catch (Exception ex) {
            System.out.println("Failed to get orders!");
            ex.printStackTrace();

            Utils.createAndShowAlert("Failed getting orders", "Failed to get orders!", Alert.AlertType.ERROR);
            try {
                ClientApp.setRoot("home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        populateTable();
    }

    private void populateTable() {
        //Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        deliveryTimeColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusName"));

        //Convert the ArrayList to an ObservableList
        ObservableList<OrderWithDetails> observableOrdersList = FXCollections.observableArrayList(orderWithDetailsList);

        //Set the items for the TableView
        ordersTable.setItems(observableOrdersList);

        //Add listener on row click
        ordersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                OrderWithDetails selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    try {
                        openOrderDetailPage(selectedOrder.order, selectedOrder.customerName);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void openOrderDetailPage(Order order, String customerName) throws IOException {
        //open order details
        OrderDetailController controller = ClientApp.setRoot("orderDetail");
        //set selected order
        controller.setOrder(order, customerName, false);
    }

    @FXML
    private void onAddNew() throws IOException {
        ClientApp.setRoot("orderDetail");
    }

    @FXML
    private void onBack() throws IOException {
        ClientApp.setRoot("home");
    }

    public class OrderWithDetails {
        private int id;
        private String customerName;
        private String statusName;
        private String deliveryTime;
        private Double totalCost;
        private Order order;

        public OrderWithDetails(Order order) {
            this.id = order.getId();
            this.deliveryTime = order.getDeliveryTime();
            this.totalCost = order.getTotalCost();

            //Status ID names are hardcoded. Quicker to do this than fetch from DB again, but would be an improvement.
            int statusId = order.getStatusId();
            switch (statusId) {
                case 1:
                    this.statusName = "Submitted";
                    break;
                case 2:
                    this.statusName = "Paid";
                    break;
                case 3:
                    this.statusName = "Received";
                    break;
            }

            //get customer name from id
            try {
                //Get user by id
                GetUserByIdRequest getUserByIdRequest = requestManager.getUserByIdRequest(new GetUserByIdRequest(order.getCustomerId()));
                User customer = getUserByIdRequest.getUser();

                this.customerName = customer.getFirstName() + " " + customer.getLastName();

            } catch (Exception ex) {
                System.out.println("Failed to get user for order!");
                ex.printStackTrace();

                Utils.createAndShowAlert("Failed getting user for order!", "Failed to get user for order!", Alert.AlertType.ERROR);
                try {
                    ClientApp.setRoot("home");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            this.order = order;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Double totalCost) {
            this.totalCost = totalCost;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

    }
}

