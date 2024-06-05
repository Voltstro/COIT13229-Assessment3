package au.edu.cqu.jhle.shared.database;

import au.edu.cqu.jhle.shared.models.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtility {
    private final String DB_USERNAME = "mdhs";
    private final String DB_PASSWORD = "Testing123";
    private final String DB_DATABASE = "mdhs";
    private final String DB_CONNECTION_STRING = "jdbc:mysql://localhost:3306";

    /**
     * Creation scripts. Could use one big String file, but it is easier to maintain if
     * everything is in its own separate string in an array
     */
    private final String[] creationScripts = new String[] {
            //role Table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`role` (
                `id` bigint NOT NULL AUTO_INCREMENT,
                `name` varchar(255) DEFAULT NULL,
                PRIMARY KEY (`id`),
                UNIQUE KEY `UK_name` (`name`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //users Table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`users` (
            	`id` bigint NOT NULL AUTO_INCREMENT,
            	`username` varchar(255) NOT NULL,
            	`password` varchar(255) NOT NULL,
            	`email` varchar(255) NOT NULL,
            	`mobile` varchar(255) NOT NULL,
            	`first_name` varchar(255) NOT NULL,
            	`last_name` varchar(255) NOT NULL,
            	`address` varchar(255) DEFAULT NULL,
            	`postcode` integer DEFAULT NULL,
            	`role_id` bigint NOT NULL,
            	 PRIMARY KEY (`id`),
            	CONSTRAINT `FK_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
            	UNIQUE KEY `UK_username` (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //products table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`products` (
            	`id` bigint(20) NOT NULL AUTO_INCREMENT,
            	`name` varchar(255) NOT NULL,
            	`quantity` bigint(20) NOT NULL,
            	`unit` varchar(255) NOT NULL,
            	`unit_price` double NOT NULL,
            	`ingredients` varchar(255) DEFAULT NULL,
            	PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //delivery_schedule table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`delivery_schedule` (
            	`id` bigint(20) NOT NULL AUTO_INCREMENT,
            	`postcode` integer NOT NULL,
            	`day` varchar(10) NOT NULL,
            	`cost` double NOT NULL,
            	PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //order_status table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`order_status` (
            	`id` bigint(20) NOT NULL AUTO_INCREMENT,
            	`name` varchar(255) NOT NULL,
            	PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //orders table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`orders` (
            	`id` bigint(20) NOT NULL AUTO_INCREMENT,
            	`customer_id` bigint(20) NOT NULL,
            	`status_id` bigint(20) NOT NULL,
            	`preferred_delivery_time` varchar(255) DEFAULT NULL,
            	`total_cost` double NOT NULL,
            	PRIMARY KEY (`id`),
            	CONSTRAINT `FK_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
            	CONSTRAINT `FK_status_id` FOREIGN KEY (`status_id`) REFERENCES `order_status` (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //order_lines table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`order_lines` (
            	`id` bigint(20) NOT NULL AUTO_INCREMENT,
            	`product_id` bigint(20) NOT NULL,
            	`order_id` bigint(20) NOT NULL,
            	`quantity` bigint(20) NOT NULL,
            	`cost` double NOT NULL,
            	PRIMARY KEY (`id`),
            	CONSTRAINT `FK_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
            	CONSTRAINT `FK_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //Seed data for role
            """
            INSERT IGNORE INTO mdhs.`role`
            (name)
            VALUES('customer'), ('admin'), ('staff');
            """
    };

    private Connection connection;
    
    public DatabaseUtility() {
        //Attempt to create connection to DB
        try {
            connection = creationConnection(DB_DATABASE);
            System.out.println("Connected to " + DB_DATABASE + " database.");
        } catch (SQLException ex) {
            //Fail, assume DB doesn't exist and that we need to migrate
            try {
                //Create new connection to create new DB
                Connection creationConnection = creationConnection(null);

                //Ideally be using parameters when using SQL queries, but this is using a hardcoded value,
                //so it's fine
                PreparedStatement createDatabaseStatement = creationConnection.prepareStatement("CREATE DATABASE IF NOT EXISTS `%s`;".formatted(DB_DATABASE));
                createDatabaseStatement.execute();

                //Connect again to new database
                creationConnection.close();
                connection = creationConnection(DB_DATABASE);
                System.out.println("Running setup scripts on new " + DB_DATABASE + " database...");

                //Run creation scripts
                Statement creationStatement = connection.createStatement();
                for (String script : creationScripts) {
                    creationStatement.execute(script);
                }
                creationStatement.close();
            } catch (SQLException exx) {
                throw new RuntimeException("Failed to create connection", exx);
            }
        }
    }

    public void upsertProduct(Product product) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("""
INSERT INTO products (id, name, quantity, unit, unit_price, ingredients)
	VALUES(?, ?, ?, ?, ?, ?) AS insert_values
ON DUPLICATE KEY UPDATE\s
	name = insert_values.name,
	quantity = insert_values.quantity,
	unit = insert_values.unit,
	unit_price = insert_values.unit_price,
	ingredients = insert_values.ingredients;
""");

            statement.setInt(1, product.getId());
            statement.setString(2, product.getName());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getUnit());
            statement.setDouble(5, product.getPrice());
            statement.setString(6, product.getIngredients());

            statement.execute();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Failed to upsert product!");
            ex.printStackTrace();

            throw new Exception("Failed to upsert product!");
        }

    }

    /*
    public synchronized void upsertUser(Integer id, String username, String password, String email, String mobile, String fName, String lName, String address, int postcode, int roleId) {
        PreparedStatement insertUser;
        PreparedStatement updateUser;
        
        try {
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
            if (id == null) {
                //insert
                //define query
                insertUser = dbConnection.prepareStatement("INSERT INTO mdhs.users\n" +
                    "(username, password, email, mobile, first_name, last_name, address, postcode, role_id)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

                //populate prepared statement
                insertUser.setString(1, username);
                insertUser.setString(2, password);
                insertUser.setString(3, email);
                insertUser.setString(4, mobile);
                insertUser.setString(5, fName);
                insertUser.setString(6, lName);
                insertUser.setString(7, address);
                insertUser.setInt(8, postcode);
                insertUser.setInt(9, roleId);

                //query prepared statement
                insertUser.executeUpdate();
            } else {
                //update
                updateUser = dbConnection.prepareStatement("UPDATE mdhs.users\n" +
                    "SET username = ?, password = ?, email = ?, mobile = ?, first_name = ?,\n" + 
                    "last_name = ?, address = ?, postcode = ?, role_id = ?\n" + 
                    "WHERE id = ?");
                
                //populate prepared statement
                updateUser.setString(1, username);
                updateUser.setString(2, password);
                updateUser.setString(3, email);
                updateUser.setString(4, mobile);
                updateUser.setString(5, fName);
                updateUser.setString(6, lName);
                updateUser.setString(7, address);
                updateUser.setInt(8, postcode);
                updateUser.setInt(9, roleId);
                updateUser.setInt(10, id);
                
                //query prepared statement
                updateUser.executeUpdate();
            }
            
            System.out.println("user upserted");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return;
        }
    }

    public synchronized void upsertProducts(Integer id, String name, Integer quantity, String unit, Double unitPrice, String ingredients) {
        PreparedStatement insertProduct;
//        PreparedStatement updateProduct
        
        try {
            if (dbConnection == null)
                dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            
            if (id == null) {
                //insert
                //define query
                insertProduct = dbConnection.prepareStatement("INSERT INTO mdhs.products\n" +
                    "(name, quantity, unit, unit_price, ingredients)\n" +
                    "VALUES (?, ?, ?, ?, ?)");
                
                //populate prepared statement
                insertProduct.setString(0, name);
                insertProduct.setInt(1, quantity);
                insertProduct.setString(2, unit);
                insertProduct.setDouble(3, unitPrice);
                insertProduct.setString(4, ingredients);
                
                //query prepared statment
                insertProduct.executeUpdate();
                
            } else {
                //update
                //TODO
            }
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return;
        }
    }
    */

    private Connection creationConnection(String dbName) throws SQLException {
        String connectionUrl = DB_CONNECTION_STRING;
        if (dbName != null)
            connectionUrl += "/" + dbName;

        return DriverManager.getConnection(connectionUrl, DB_USERNAME, DB_PASSWORD);
    }
}
