package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.requests.RegisterUserRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private TextField firstNameInput;

    @FXML
    private TextField lastNameInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private TextField emailInput;

    @FXML
    private TextField addressInput;

    @FXML
    private TextField postcodeInput;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    private ClientRequestManager requestManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        requestManager = ClientApp.getClientRequestManager();
    }

    @FXML
    private void onReturnToLogin() throws IOException {
        ClientApp.setRoot("login");
    }

    @FXML
    private void onSignup() throws IOException {
        String username = usernameInput.getText();
        String firstName = firstNameInput.getText();
        String lastName = lastNameInput.getText();
        String phone = phoneInput.getText();
        String email = emailInput.getText();
        String address = addressInput.getText();
        String postcode = postcodeInput.getText();
        String password = passwordInput.getText();

        //Ensure all fields are not empty/blank
        if (Utils.isEmpty(username) || Utils.isEmpty(firstName) || Utils.isEmpty(lastName) || Utils.isEmpty(phone) || Utils.isEmpty(email) || Utils.isEmpty(address) || Utils.isEmpty(postcode) || Utils.isEmpty(password)) {
            Utils.createAndShowAlert("Invalid fields!", "All fields are required! They cannot be blank and/or empty.", Alert.AlertType.ERROR);
            return;
        }

        //Send request and get response
        RegisterUserRequest response = requestManager.sendRegisterUserRequest(new RegisterUserRequest(username, password, email, phone, firstName, lastName, address, postcode));
        if (response.isValid()) {
            requestManager.setLoggedInUser(response.getUser());
            ClientApp.setRoot("home");
            return;
        }

        //Signup failed
        Utils.createAndShowAlert("Signup Failed", "User signup failed: " + response.getErrorMessage(), Alert.AlertType.ERROR);
    }

}
