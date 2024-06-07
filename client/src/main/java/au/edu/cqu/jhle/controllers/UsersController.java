package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.GetUsersRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    @FXML
    private TableView<UserWithRole> usersTable;

    @FXML
    private TableColumn<UserWithRole, Integer> idColumn;

    @FXML
    private TableColumn<UserWithRole, String> nameColumn;

    @FXML
    private TableColumn<UserWithRole, String> roleColumn;

    @FXML
    private TableColumn<UserWithRole, String> firstNameColumn;

    @FXML
    private TableColumn<UserWithRole, String> lastNameColumn;

    private List<UserWithRole> userWithRoles = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Get all users
            GetUsersRequest getUsersRequest = ClientApp.getClientRequestManager().getUsersRequest(new GetUsersRequest());

            //Convert Users into custom UserWithRole object
            for (User user : getUsersRequest.getUsers()) {
                UserWithRole userWithRole = new UserWithRole(user);
                userWithRoles.add(userWithRole);
            }

        } catch (Exception ex) {
            System.out.println("Failed to get users!");
            ex.printStackTrace();

            Utils.createAndShowAlert("Failed getting users", "Failed to get users!", Alert.AlertType.ERROR);
            try {
                ClientApp.setRoot("home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        populateTable();
    }

    private void populateTable() {
        // Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        // Convert the ArrayList to an ObservableList
        ObservableList<UserWithRole> observableUsersList = FXCollections.observableArrayList(userWithRoles);

        // Set the items for the TableView
        usersTable.setItems(observableUsersList);

        // Add listener on row click
        usersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                UserWithRole selectedUser = usersTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    try {
                        openUserDetailPage(selectedUser.user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    private void onAddNew() throws IOException {
        ClientApp.setRoot("userDetail");
    }

    @FXML
    private void onBack() throws IOException {
        ClientApp.setRoot("home");
    }

    public void openUserDetailPage(User user) throws IOException {
        //open user details
        UserDetailController controller = ClientApp.setRoot("userDetail");
        //set selected user
        controller.setUser(user);
    }

    public class UserWithRole {
        private int id;
        private String firstName;
        private String lastName;
        private String username;
        private String roleName;
        private User user;

        public UserWithRole(User user) {
            this.id = user.getId();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.username = user.getUsername();

            //Role ID names are hardcoded, it easier to do this, then to fetch from DB again
            int roleId = user.getRoleId();
            switch (roleId) {
                case 1:
                    this.roleName = "customer";
                    break;
                case 2:
                    this.roleName = "admin";
                    break;
                case 3:
                    this.roleName = "staff";
                    break;
            }

            this.user = user;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
