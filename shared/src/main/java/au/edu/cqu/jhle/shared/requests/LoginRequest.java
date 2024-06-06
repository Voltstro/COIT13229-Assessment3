package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.User;

/**
 * Request for login.
 * This is a bit of a special request, as it doesn't implement IRequest, since it has some special logic
 */
public class LoginRequest extends Request {
    public LoginRequest(String username, byte[] password) {
        this.username = username;
        this.password = password;
    }

    private String username;
    private byte[] password;

    private User user;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
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
            user = databaseUtility.getUserByUsername(username);
            if(user == null) {
                return;
            }
        } catch (Exception ex) {
            return;
        }

        setValid(true);
    }
}
