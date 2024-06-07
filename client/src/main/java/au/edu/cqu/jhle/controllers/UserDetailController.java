package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.AddUserRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserDetailController implements Initializable {
    @FXML
    private TextField addressInput;

    @FXML
    private TextField emailInput;

    @FXML
    private TextField firstNameInput;

    @FXML
    private TextField lastNameInput;

    @FXML
    private TextField mobileInput;

    @FXML
    private Button onSaveProduct;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private TextField postCodeInput;

    @FXML
    private ComboBox<Role> roleCombo;

    @FXML
    private Label userTitleLabel;

    @FXML
    private TextField usernameInput;

    private ClientRequestManager requestManager;
    private User user;
    private List<Role> roles;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestManager = ClientApp.getClientRequestManager();

        roles = new ArrayList<>();
        roles.add(new Role(1, "customer"));
        roles.add(new Role(2, "admin"));
        roles.add(new Role(3, "staff"));

        //Load roles into role combo
        ObservableList<Role> observableRoles = FXCollections.observableArrayList(roles);
        roleCombo.setItems(observableRoles);
        roleCombo.getSelectionModel().select(0);

        this.user = null;
    }

    public void setUser(User user) {
        this.user = user;

        this.usernameInput.setText(user.getUsername());
        this.usernameInput.setDisable(true);

        this.passwordInput.setText(user.getPassword());
        this.firstNameInput.setText(user.getFirstName());
        this.lastNameInput.setText(user.getLastName());
        this.mobileInput.setText(user.getMobile());
        this.emailInput.setText(user.getEmail());
        this.addressInput.setText(user.getAddress());
        this.postCodeInput.setText(user.getPostcode());

        //Find matching role
        for(Role role : roles) {
            if (role.id == user.getRoleId()) {
                roleCombo.getSelectionModel().select(role);
                break;
            }
        }

        userTitleLabel.setText("User (ID: %s)".formatted(user.getId()));
    }


    @FXML
    private void onReturnToList() throws IOException {
        ClientApp.setRoot("users");
    }

    @FXML
    private void onSaveUser() throws IOException {
        String username = this.usernameInput.getText();
        String password = this.passwordInput.getText();
        String firstName = this.firstNameInput.getText();
        String lastName = this.lastNameInput.getText();
        String mobile = this.mobileInput.getText();
        String email = this.emailInput.getText();
        String address = this.addressInput.getText();
        String postcode = this.postCodeInput.getText();
        Role role = this.roleCombo.getValue();

        //Valid all fields are not empty
        if(Utils.isEmpty(username) || Utils.isEmpty(password) || Utils.isEmpty(firstName) || Utils.isEmpty(lastName) || Utils.isEmpty(mobile) || Utils.isEmpty(email) || Utils.isEmpty(address) || Utils.isEmpty(postcode)) {
            Utils.createAndShowAlert("Invalid fields", "Fields cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        boolean newUser = true;
        if(user == null) {
            user = new User(username, password, email, mobile, firstName, lastName, address, postcode, role.id);
        } else {
            user.setUsername(username);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMobile(mobile);
            user.setEmail(email);
            user.setAddress(address);
            user.setPostcode(postcode);
            user.setRoleId(role.id);
            newUser = false;
        }

        AddUserRequest response = requestManager.addUserRequest(new AddUserRequest(user));
        if(response.isValid()) {
            //Display message saying products was updated/added successfully
            if(newUser) {
                Utils.createAndShowAlert("Successfully created user", "User was successfully created!", Alert.AlertType.INFORMATION);
            } else {
                Utils.createAndShowAlert("Successfully updated User", "User was successfully updated!", Alert.AlertType.INFORMATION);
            }

            ClientApp.setRoot("users");
            return;
        }

        //Failed
        if(newUser)
            user = null;
        Utils.createAndShowAlert("Failed Updating User", response.getErrorMessage(), Alert.AlertType.ERROR);
    }

    private class Role {
        public Role(int id, String roleName) {
            this.id = id;
            this.roleName = roleName;
        }

        private int id;

        private String roleName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        @Override
        public String toString() {
            return roleName;
        }
    }


}
