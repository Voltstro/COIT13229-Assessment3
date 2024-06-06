package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.User;

public class RegisterUserRequest extends Request {
    public RegisterUserRequest(String username, String password, String email, String phone, String firstName, String lastName, String address, String postcode) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
    }

    public String username;

    private String password;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String address;

    private String postcode;

    private User user;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            User user = databaseUtility.getUserByUsername(username);
            if(user != null) {
                setErrorMessage("Username is already in use.");
                return;
            }

            //Role Id 1 is customer
            databaseUtility.upsertUser(new User(username, password, email, phone, firstName, lastName, address, postcode, 1));

            //Fetch new user from DB again
            user = databaseUtility.getUserByUsername(username);
            setValid(true);
            return;
        } catch (Exception ex) {

        }

        setValid(false);
        setErrorMessage("Failed to register user.");
    }
}
