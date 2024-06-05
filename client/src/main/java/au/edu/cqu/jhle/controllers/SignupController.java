package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void onReturnToLogin() throws IOException {
        ClientApp.setRoot("login");
    }
    
    @FXML
    private void onSignup() throws IOException {
        ClientApp.setRoot("home");
    }
    
}
