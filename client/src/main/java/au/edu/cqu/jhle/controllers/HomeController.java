package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.shared.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button usersBtn;

    @FXML
    private Label welcomeLabel;

    private ClientRequestManager requestManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();

        User user = requestManager.getLoggedInUser();

        //Disable users button if user is customer
        if (user.getRoleId() == 1)
            usersBtn.setDisable(true);

        welcomeLabel.setText("Welcome, " + user.getFirstName() + " " + user.getLastName());
    }

    @FXML
    private void onOrdersClick() throws IOException {
        ClientApp.setRoot("orders");
    }

    @FXML
    private void onProductsClick() throws IOException {
        ClientApp.setRoot("products");
    }

    @FXML
    private void onDeliverySchedulesClick() throws IOException {
        ClientApp.setRoot("deliverySchedules");
    }

    @FXML
    void onQuitClick() {
        ClientApp.exit();
    }

    @FXML
    private void onUsersClick() throws IOException {
        ClientApp.setRoot("users");
    }

}
