package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.requests.LoginRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    
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
    private void onLogin() throws IOException {

        String username = usernameInput.getText();
        String password = passwordInput.getText();

        if(Utils.isEmpty(username) || Utils.isEmpty(password) ) {
            Utils.createAndShowAlert("Invalid username and/or password", "User and/or password cannot be empty or blank!", Alert.AlertType.ERROR);
            return;
        }

        byte[] encryptedPassword = requestManager.encrypt(password);
        LoginRequest loginRequest = new LoginRequest(username, encryptedPassword);


        LoginRequest response = requestManager.sendLoginRequest(loginRequest);
        if (response.isValid()) {
            ClientApp.setRoot("home");
        } else {
            Utils.createAndShowAlert("Invalid username and/or password", "User and/or password is invalid!", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void onSignup() throws IOException {
        ClientApp.setRoot("signup");
    }
    
}
