package au.edu.cqu.jhle.shared.database;

import au.edu.cqu.jhle.shared.models.DeliverySchedule;
import au.edu.cqu.jhle.shared.models.Product;
import au.edu.cqu.jhle.shared.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

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
            	`postcode` varchar(255) DEFAULT NULL,
            	`role_id` bigint NOT NULL,
            	 PRIMARY KEY (`id`),
            	CONSTRAINT `FK_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
            	UNIQUE KEY `UK_username` (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //products table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`products` (
            	`id` bigint NOT NULL AUTO_INCREMENT,
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
            	`id` bigint NOT NULL AUTO_INCREMENT,
            	`postcode` varchar(255) NOT NULL,
            	`day` varchar(10) NOT NULL,
            	`cost` double NOT NULL,
            	PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //order_status table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`order_status` (
            	`id` bigint NOT NULL AUTO_INCREMENT,
            	`name` varchar(255) NOT NULL,
            	PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
            """,

            //orders table
            """
            CREATE TABLE IF NOT EXISTS `mdhs`.`orders` (
            	`id` bigint NOT NULL AUTO_INCREMENT,
            	`customer_id` bigint NOT NULL,
            	`status_id` bigint NOT NULL,
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
            	`id` bigint NOT NULL AUTO_INCREMENT,
            	`product_id` bigint NOT NULL,
            	`order_id` bigint NOT NULL,
            	`quantity` bigint NOT NULL,
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
            """,
            
            //Seed data for delivery_schedule
            """
            INSERT INTO mdhs.delivery_schedule
            (postcode, day, cost)
            VALUES
            	('4552', 'Monday', 5),
            	('4553', 'Tuesday', 6),
            	('4554', 'Wednesday', 8),
            	('4551', 'Thursday', 4),
            	('4550', 'Friday', 7);
            """
    };
    
    //Queries
    private final String SELECT_ALL_PRODUCTS_QUERY = "SELECT id, name, quantity, unit, unit_price, ingredients FROM mdhs.products;";
    private final String SELECT_ALL_DELIVERY_SCHEDULES_QUERY = "SELECT id, postcode, `day`, cost FROM mdhs.delivery_schedule;";

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
                
                //read from products file to populate table
                DataFile readFiles = new DataFile();
                readFiles.readFromFile(this);
            } catch (SQLException exx) {
                throw new RuntimeException("Failed to create connection", exx);
            }
        }
    }

    /**
     * Upserts a Product
     */
    public void upsertProduct(Product product) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("""
INSERT INTO products (id, name, quantity, unit, unit_price, ingredients)
	VALUES(?, ?, ?, ?, ?, ?) AS insert_values
ON DUPLICATE KEY UPDATE
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

    /**
     * Upserts a user
     */
    public void upsertUser(User user) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("""
INSERT INTO users
(id, username, password, email, mobile, first_name, last_name, address, postcode, role_id)
VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) as insert_values
ON DUPLICATE KEY UPDATE
    username = insert_values.username,
    password = insert_values.password,
    email = insert_values.email,
    mobile = insert_values.mobile,
    first_name = insert_values.first_name,
    last_name = insert_values.last_name,
    address = insert_values.address,
    postcode = insert_values.postcode,
    role_id = insert_values.role_id;
""");
            statement.setInt(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getMobile());
            statement.setString(6, user.getFirstName());
            statement.setString(7, user.getLastName());
            statement.setString(8, user.getAddress());
            statement.setString(9, user.getPostcode());
            statement.setInt(10, user.getRoleId());

            statement.execute();

        } catch (SQLException ex) {
            System.out.println("Failed to upsert user!");
            ex.printStackTrace();

            throw new Exception("Failed to upsert user!");
        }
    }
    
    /**
     * Upserts a delivery schedule
     */
    public synchronized void upsertDeliverySchedule(DeliverySchedule deliverySchedule) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                INSERT INTO delivery_schedule
                (id, postcode, `day`, cost)
                VALUES(?, ?, ?, ?) AS insert_values
                ON DUPLICATE KEY UPDATE
                    postcode = insert_values.postcode,
                    `day` = insert_values.`day`,
                    cost = insert_values.cost;                       
            """);
            
            statement.setInt(1, deliverySchedule.getId());
            statement.setString(2, deliverySchedule.getPostcode());
            statement.setString(3, deliverySchedule.getDay());
            statement.setDouble(4, deliverySchedule.getCost());
            
            statement.execute();
        } catch (SQLException ex) {
            System.out.println("Failed to upsert delivery schedule!");
            ex.printStackTrace();

            throw new Exception("Failed to upsert deliver schedule!");
        }
    }
    
    /**
     * Gets list of all products 
     */
    public LinkedList<Product> getProducts() {
        int id = 0;
        String name = "";
        int quantity = 0;
        String unit = "";
        Double unitPrice = 0.0;
        String ingredients = "";
        
        LinkedList<Product> products = new LinkedList<>();
        
        try {
            Statement statement = connection.createStatement();
            
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_PRODUCTS_QUERY);
            
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                name = resultSet.getString(2);
                quantity = resultSet.getInt(3);
                unit = resultSet.getString(4);
                unitPrice = resultSet.getDouble(5);
                ingredients = resultSet.getString(6);
                
                products.add(new Product(id, name, quantity, unit, unitPrice, ingredients));

            }
            
            System.out.println(products);
            return products;
            
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
	    return null;
        }
    }
    
    /**
     * Gets list of all delivery schedules 
     */
    public LinkedList<DeliverySchedule> getDeliverySchedules() {
        int id = 0;
        String postcode = "";
        String day = "";
        Double cost = 0.0;
        
        LinkedList<DeliverySchedule> deliverySchedules = new LinkedList<>();
        
        try {
            Statement statement = connection.createStatement();
            
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_DELIVERY_SCHEDULES_QUERY);
            
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                postcode = resultSet.getString(2);
                day = resultSet.getString(3);
                cost = resultSet.getDouble(4);
                
                deliverySchedules.add(new DeliverySchedule(id, postcode, day, cost));

            }
            
            System.out.println(deliverySchedules);
            return deliverySchedules;
            
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
	    return null;
        }
    }

    private Connection creationConnection(String dbName) throws SQLException {
        String connectionUrl = DB_CONNECTION_STRING;
        if (dbName != null)
            connectionUrl += "/" + dbName;

        return DriverManager.getConnection(connectionUrl, DB_USERNAME, DB_PASSWORD);
    }
}
