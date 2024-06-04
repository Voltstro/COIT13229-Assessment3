package au.edu.cqu.jhle.shared.database;

public class DatabaseManager {
    
//    May be removed but here to test for now
    public static void main(String [] args) {
        DatabaseUtility utility = new DatabaseUtility();
        
        boolean wasTablesCreated = utility.createDBtables();
        
        if (wasTablesCreated) {
            //read from file & save
        }
        
//        test upsert user
//        utility.upsertUser(1, "userName1", "root", "test@tr", "+61407123456", "Justin", "Hastings", "test address", 4870, 1);

//        utility.upsertUser(2, "admin_login", "root", "test@tr", "+61407553456", "New", "Admin", "address", 4870, 2);
    }
}
