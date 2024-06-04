package au.edu.cqu.jhle.shared.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DatabaseUtility {
    private final  String MYSQL_URL  ;
    final  String DB_URL;
    private Connection sqlConnection, dbConnection;
    private Statement  statement;
    private final String dbCreateSQL;
    private final String USER_NAME;
    private final String PASSWORD;
    private final String TABLE_USERS_QRY;
    private final String TABLE_ROLE_QRY;
    private final String TABLE_PRODUCTS_QRY;
    private final String TABLE_DELIVERYSCHEDULES_QRY;
    private final String TABLE_ORDERSTATUS_QRY;
    private final String TABLE_ORDERS_QRY;
    private final String TABLE_ORDERLINES_QRY;
    
    private final String SEED_TABLE_ROLE_QRY;

//    private final String SELECT_EMPLOYEES_QRY;
//    private final String SELECT_SUPERVISORS_QRY;
    
    public DatabaseUtility() {
        MYSQL_URL = "jdbc:mysql://localhost:3306";
        DB_URL = MYSQL_URL +"/mdhs";
        //initialise MySql usename and password 
        USER_NAME ="root";
        PASSWORD = "root";
        
        statement = null;
        //sql query to create database.
        dbCreateSQL = "CREATE DATABASE IF NOT EXISTS mdhs";
         //sql queries to create Tables
         TABLE_ROLE_QRY = "CREATE TABLE IF NOT EXISTS `mdhs`.`role` (\n" +
            "	`id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "	`name` varchar(255) DEFAULT NULL,\n" +
            "	PRIMARY KEY (`id`),\n" +
            "	UNIQUE KEY `UK_name` (`name`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci";
         
         TABLE_USERS_QRY = "CREATE TABLE IF NOT EXISTS `mdhs`.`users` (\n" +
            "	`id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "	`username` varchar(255) NOT NULL,\n" +
            "	`password` varchar(255) NOT NULL,\n" +
            "	`email` varchar(255) NOT NULL,\n" +
            "	`mobile` varchar(255) NOT NULL,\n" +
            "	`first_name` varchar(255) NOT NULL,\n" +
            "	`last_name` varchar(255) NOT NULL,\n" +
            "	`address` varchar(255) DEFAULT NULL,\n" +
            "	`postcode` integer DEFAULT NULL,\n" +
            "   `role_id` bigint(20) NOT NULL,\n" +
            "	PRIMARY KEY (`id`),\n" +
            "   CONSTRAINT `FK_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)," +
            "	UNIQUE KEY `UK_username` (`username`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci";
        TABLE_PRODUCTS_QRY = "CREATE TABLE IF NOT EXISTS `mdhs`.`products` (\n" +
            "	`id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "	`name` varchar(255) NOT NULL,\n" +
            "	`quantity` bigint(20) NOT NULL,\n" +
            "	`unit` varchar(255) NOT NULL,\n" +
            "	`unit_price` double NOT NULL,\n" +
            "	`ingredients` varchar(255) DEFAULT NULL,\n" +
            "	PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci";
        TABLE_DELIVERYSCHEDULES_QRY = "CREATE TABLE IF NOT EXISTS `mdhs`.`delivery_schedule` (\n" +
            "   `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "   `postcode` integer NOT NULL,\n" +
            "   `day` varchar(10) NOT NULL,\n" +
            "   `cost` double NOT NULL,\n" +
            "   PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci";
        TABLE_ORDERSTATUS_QRY = "CREATE TABLE IF NOT EXISTS `mdhs`.`order_status` (\n" +
            "   `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "   `name` varchar(255) NOT NULL,\n" +
            "   PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci";
        TABLE_ORDERS_QRY = "CREATE TABLE IF NOT EXISTS `mdhs`.`orders` (\n" +
            "   `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "   `customer_id` bigint(20) NOT NULL,\n" +
            "   `status_id` bigint(20) NOT NULL,\n" +
            "   `preferred_delivery_time` varchar(255) DEFAULT NULL,\n" +
            "   `total_cost` double NOT NULL,\n" +
            "   PRIMARY KEY (`id`),\n" +
            "   CONSTRAINT `FK_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),\n" +
            "   CONSTRAINT `FK_status_id` FOREIGN KEY (`status_id`) REFERENCES `order_status` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci";
        TABLE_ORDERLINES_QRY = "CREATE TABLE IF NOT EXISTS `mdhs`.`order_lines` (\n" +
            "   `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "   `product_id` bigint(20) NOT NULL,\n" +
            "   `order_id` bigint(20) NOT NULL,\n" +
            "   `quantity` bigint(20) NOT NULL,\n" +
            "   `cost` double NOT NULL,\n" +
            "   PRIMARY KEY (`id`),\n" +
            "   CONSTRAINT `FK_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),\n" +
            "   CONSTRAINT `FK_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci";
        
        SEED_TABLE_ROLE_QRY =  "INSERT IGNORE INTO mdhs.`role`\n" +
            "(name)\n" +
            "VALUES('customer'), ('admin'), ('staff')";
    }
    
    public boolean createDBtables() {
        // TODO - Check if db & tables already exist
        boolean dbExists = false,
                tblUsersExist = false,
                dbCreate = false;
        String databaseName ="";
        
        try {
            sqlConnection = DriverManager.getConnection(MYSQL_URL, USER_NAME, PASSWORD);
            statement = sqlConnection.createStatement();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return false;
        }
        
        try {
            //create database
            statement.executeUpdate(dbCreateSQL);
            
            if (sqlConnection != null)
                sqlConnection.close(); 
            
            //connection to mdhs database
            dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);                                 
            statement = dbConnection.createStatement();
            //create tables
            statement.executeUpdate(TABLE_ROLE_QRY);
            statement.executeUpdate(TABLE_USERS_QRY);
            statement.executeUpdate(TABLE_PRODUCTS_QRY);
            statement.executeUpdate(TABLE_DELIVERYSCHEDULES_QRY);
            statement.executeUpdate(TABLE_ORDERSTATUS_QRY);
            statement.executeUpdate(TABLE_ORDERS_QRY);
            statement.executeUpdate(TABLE_ORDERLINES_QRY);
            
            //Seed data
            statement.executeUpdate(SEED_TABLE_ROLE_QRY);
            
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * 
     */
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
    
    /**
     * 
     */
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
}
