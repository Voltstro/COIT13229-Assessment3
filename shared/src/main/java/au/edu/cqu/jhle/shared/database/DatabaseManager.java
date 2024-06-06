package au.edu.cqu.jhle.shared.database;

import au.edu.cqu.jhle.shared.models.Product;
import au.edu.cqu.jhle.shared.models.User;

import java.sql.SQLException;

public class DatabaseManager {
    
//    May be removed but here to test for now
    public static void main(String [] args) {
        DatabaseUtility utility = new DatabaseUtility();

        try {
            utility.upsertProduct(new Product(7, "test update", 123, "test", 123.1, "test"));
            utility.upsertProduct(new Product(8, "test2 update", 123, "test", 123.1, "test"));
            utility.upsertProduct(new Product(9, "test3 update", 123, "test", 123.1, "test"));

            utility.upsertUser(new User("test.updated", "test", "test", "04 1234 1234", "Test", "Elsum", "21 test St.", "1234", 2));
            utility.upsertUser(new User(1, "test.gggg", "test", "test", "04 1234 1234", "Test", "Elsum", "21 test St.", "1234", 2));


            var user = utility.getUserByUsername("test.new");
            System.out.println(user);

        } catch (Exception ex) {

        }

//        test upsert user
//        utility.upsertUser(1, "userName1", "root", "test@tr", "+61407123456", "Justin", "Hastings", "test address", 4870, 1);

//        utility.upsertUser(2, "admin_login", "root", "test@tr", "+61407553456", "New", "Admin", "address", 4870, 2);
    }
}
