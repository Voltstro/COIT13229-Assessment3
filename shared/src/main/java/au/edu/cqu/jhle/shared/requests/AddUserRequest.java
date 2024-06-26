package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.User;

public class AddUserRequest extends Request {
    private User user;

    public AddUserRequest(User user) {
        this.user = user;
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
            //Need to check if username is in use if the request is for adding a new user
            if (user.getId() == 0) {
                User userCheck = databaseUtility.getUserByUsername(user.getUsername());
                if (userCheck != null) {
                    throw new Exception("Username already in use");
                }
            }

            databaseUtility.upsertUser(user);
        } catch (Exception ex) {
            setErrorMessage("Failed to upsert user: " + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
