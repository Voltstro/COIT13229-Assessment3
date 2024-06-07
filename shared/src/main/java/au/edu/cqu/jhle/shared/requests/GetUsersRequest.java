package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.User;

import java.util.List;

public class GetUsersRequest extends Request {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            users = databaseUtility.getUsers();
        } catch (Exception ex) {
            //Failed
            setErrorMessage("Failed to get users");
            return;
        }

        setValid(true);
    }
}
