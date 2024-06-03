package au.edu.cqu.jhle.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    
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
    private void onLogin() throws IOException {
        ClientApp.setRoot("home");
    }
    
    @FXML
    private void onSignup() throws IOException {
        ClientApp.setRoot("signup");
    }
    
}
