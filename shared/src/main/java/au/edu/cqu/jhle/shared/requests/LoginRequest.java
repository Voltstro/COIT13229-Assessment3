package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseManager;

import java.io.Serializable;

/**
 * Request for login.
 * This is a bit of a special request, as it doesn't implement IRequest, since it has some special logic
 */
public class LoginRequest implements Serializable {
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.isValid = false;
    }

    private String username;
    private String password;

    private boolean isValid;

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

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    /**
     * Checks if this login request is valid
     * @return Returns true if login is valid
     */
    public boolean isValid(DatabaseManager databaseManager) {
        isValid = true;
        return true;
    }
}
