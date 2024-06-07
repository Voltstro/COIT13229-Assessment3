package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.User;

public class GetUserByIdRequest extends Request {
    public GetUserByIdRequest(int id) {
        this.id = id;
    }
    
    private int id;
    
    private User user;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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
            //Fetch user from db
            user = databaseUtility.getUserById(id);
        } catch (Exception ex) {
            setErrorMessage("Could not get user from database\n" + ex.getMessage());
            return;
        }
        
        setValid(true);
    }
}
