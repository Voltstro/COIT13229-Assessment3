package au.edu.cqu.jhle.shared.database;

import au.edu.cqu.jhle.shared.models.Product;

import java.sql.SQLException;

public class DatabaseManager {
    
//    May be removed but here to test for now
    public static void main(String [] args) {
        DatabaseUtility utility = new DatabaseUtility();

        try {
            utility.upsertProduct(new Product(7, "test update", 123, "test", 123.1, "test"));
            utility.upsertProduct(new Product(8, "test2 update", 123, "test", 123.1, "test"));
            utility.upsertProduct(new Product(9, "test3 update", 123, "test", 123.1, "test"));
        } catch (Exception ex) {

        }

//        test upsert user
//        utility.upsertUser(1, "userName1", "root", "test@tr", "+61407123456", "Justin", "Hastings", "test address", 4870, 1);

//        utility.upsertUser(2, "admin_login", "root", "test@tr", "+61407553456", "New", "Admin", "address", 4870, 2);
    }
}
