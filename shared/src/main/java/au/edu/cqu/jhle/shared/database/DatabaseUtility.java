package au.edu.cqu.jhle.shared.database;

import au.edu.cqu.jhle.shared.models.DeliverySchedule;
import au.edu.cqu.jhle.shared.models.Order;
import au.edu.cqu.jhle.shared.models.OrderLine;
import au.edu.cqu.jhle.shared.models.Product;
import au.edu.cqu.jhle.shared.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                (id, name)
            VALUES(1, 'customer'), (2, 'admin'), (3, 'staff');
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
            """,
            
            //Seed data for order_status
            """
            INSERT INTO mdhs.order_status
                (id, name)
            VALUES
                (1, 'Submitted'),
                (2, 'Paid'),
                (3, 'Received');
            """
    };
    
    //Queries
    private final String SELECT_ALL_PRODUCTS_QUERY = "SELECT id, name, quantity, unit, unit_price, ingredients FROM mdhs.products;";
    private final String SELECT_PRODUCT_BY_ID_QUERY = "SELECT id, name, quantity, unit, unit_price, ingredients FROM mdhs.products WHERE id = ?;";
    private final String SELECT_ALL_DELIVERY_SCHEDULES_QUERY = "SELECT id, postcode, `day`, cost FROM mdhs.delivery_schedule;";
    private final String SELECT_ALL_ORDERS_QUERY = "SELECT id, customer_id, status_id, preferred_delivery_time, total_cost FROM mdhs.orders;";
    private final String SELECT_ORDER_BY_ID_QUERY = "SELECT id, customer_id, status_id, preferred_delivery_time, total_cost FROM mdhs.orders WHERE id = ?;";
    private final String SELECT_ALL_ORDER_LINES_FOR_ORDER_QUERY = "SELECT id, product_id, order_id, quantity, cost FROM mdhs.order_lines WHERE order_id = ?;";

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
     * Gets all users
     */
    public List<User> getUsers() throws Exception {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, username, password, email, mobile, first_name, last_name, address, postcode, role_id FROM users;");

            while (resultSet.next()) {
                users.add(new User(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9),
                    resultSet.getInt(10)
                ));
            }

            return users;

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            throw new Exception("Failed to get users!");
        }
    }

    public User getUserByUsername(String username) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id, username, password, email, mobile, first_name, last_name, address, postcode, role_id FROM mdhs.users WHERE username = ?");
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();
            boolean any = result.next();
            if (!any) return null;

            return new User(
                    result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getString(7),
                    result.getString(8),
                    result.getString(9),
                    result.getInt(10));
        } catch (SQLException ex) {
            System.out.println("Failed to get user!");
            ex.printStackTrace();

            throw new Exception("Failed to get user!");
        }
    }
    
    /**
     * Gets user record by id 
     */
    public User getUserById(int id) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id, username, password, email, mobile, first_name, last_name, address, postcode, role_id FROM mdhs.users WHERE id = ?");
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            boolean any = result.next();
            if (!any) return null;

            return new User(
                    result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getString(7),
                    result.getString(8),
                    result.getString(9),
                    result.getInt(10));
        } catch (SQLException ex) {
            System.out.println("Failed to get user!");
            ex.printStackTrace();

            throw new Exception("Failed to get user!");
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
    public void upsertDeliverySchedule(DeliverySchedule deliverySchedule) throws Exception {
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

            throw new Exception("Failed to upsert delivery schedule!");
        }
    }
    
    /**
     * Gets list of all products 
     */
    public ArrayList<Product> getProducts() throws Exception {
        ArrayList<Product> products = new ArrayList<>();
        
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_PRODUCTS_QUERY);
            
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int quantity = resultSet.getInt(3);
                String unit = resultSet.getString(4);
                Double unitPrice = resultSet.getDouble(5);
                String ingredients = resultSet.getString(6);
                
                products.add(new Product(id, name, quantity, unit, unitPrice, ingredients));
            }

            return products;
            
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            throw new Exception("Failed to get products!");
        }
    }
    
    /**
     * Gets a product record by id
     */
    public Product getProductById(int id) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCT_BY_ID_QUERY);
            statement.setInt(1, id);
            
            ResultSet result = statement.executeQuery();
            
            boolean any = result.next();
            if (!any) return null;
            
            return new Product(
                    result.getInt(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getString(4),
                    result.getDouble(5),
                    result.getString(6)
            );
        } catch (SQLException ex) {
            System.out.println("Failed to get product!");
            ex.printStackTrace();
            
            throw new Exception("Failed to get product!");
        }
    }
    
    /**
     * Gets list of all delivery schedules 
     */
    public ArrayList<DeliverySchedule> getDeliverySchedules() throws Exception {
        ArrayList<DeliverySchedule> deliverySchedules = new ArrayList<>();
        
        try {
            Statement statement = connection.createStatement();
            
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_DELIVERY_SCHEDULES_QUERY);
            
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String postcode = resultSet.getString(2);
                String day = resultSet.getString(3);
                Double cost = resultSet.getDouble(4);
                
                deliverySchedules.add(new DeliverySchedule(id, postcode, day, cost));

            }
            
            return deliverySchedules;
            
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
	        throw new Exception("Failed to get schedules!");
        }
    }
    
    /**
     * Gets list of all orders
     */
    public ArrayList<Order> getOrders()throws Exception {
        ArrayList<Order> orders = new ArrayList<>();
        
        try {
            Statement statement = connection.createStatement();
            
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_ORDERS_QUERY);
            
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int customerId = resultSet.getInt(2);
                int statusId = resultSet.getInt(3);
                String deliveryTime = resultSet.getString(4);
                Double totalCost = resultSet.getDouble(5);
                
                orders.add(new Order(id, customerId, statusId, deliveryTime, totalCost));
            }
            
            return orders;
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
	    throw new Exception("Failed to get orders!");
        }
    }
    
    /**
     * Gets list of all order lines associated with the order
     */
    public ArrayList<OrderLine> getOrderLinesForOrder(int orderIdInput) throws Exception {
        ArrayList<OrderLine> orderLines = new ArrayList<>();
        
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ORDER_LINES_FOR_ORDER_QUERY);
            statement.setInt(1, orderIdInput);
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int productId = resultSet.getInt(2);
                int orderId = resultSet.getInt(3);
                int quantity = resultSet.getInt(4);
                Double cost = resultSet.getDouble(5);
                
                orderLines.add(new OrderLine(id, productId, orderId, quantity, cost));
            }
            
            return orderLines;
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
	    throw new Exception("Failed to get order lines!");
        }
    }
    
    /**
     * Upserts an order
     */
    public int upsertOrder(Order order) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                INSERT INTO orders
                (id, customer_id, status_id, preferred_delivery_time, total_cost)
                VALUES (?, ?, ?, ?, ?) AS insert_values
                ON DUPLICATE KEY UPDATE
                    status_id = insert_values.status_id,
                    preferred_delivery_time = insert_values.preferred_delivery_time,
                    total_cost = insert_values.total_cost;                                                              
            """);
            
            statement.setInt(1, order.getId());
            statement.setInt(2, order.getCustomerId());
            statement.setInt(3, order.getStatusId());
            statement.setString(4, order.getDeliveryTime());
            statement.setDouble(5, order.getTotalCost());
            
            statement.execute();
            
            Integer id = order.getId();
            
            if (order.getId() == 0) {
                //Get last inserted id as new order's id
                Statement idStatement = connection.createStatement();
                ResultSet idResult = idStatement.executeQuery("SELECT LAST_INSERT_ID() FROM orders LIMIT 1;");

                idResult.next();
                id = idResult.getInt(1);
            }
            
            return id;
        } catch (SQLException ex) {
            System.out.println("Failed to upsert order");
            ex.printStackTrace();
            
            throw new Exception("Failed to upsert order!");
        }
    }
    
    /**
     * Upserts an order line
     */
    public void upsertOrderLine(OrderLine orderLine) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                INSERT INTO order_lines
                (id, product_id, order_id, quantity, cost)
                VALUES (?, ?, ?, ?, ?) AS insert_values
                ON DUPLICATE KEY UPDATE
                    product_id = insert_values.product_id,
                    quantity = insert_values.quantity,
                    cost = insert_values.cost;                                               
            """);
            
            statement.setInt(1, orderLine.getId());
            statement.setInt(2, orderLine.getProductId());
            statement.setInt(3, orderLine.getOrderId());
            statement.setInt(4, orderLine.getQuantity());
            statement.setDouble(5, orderLine.getCost());
            
            statement.execute();
            
            //Calculate total cost of all order lines in order
            List<OrderLine> orderLines = getOrderLinesForOrder(orderLine.getOrderId());
            Double totalCost = 0.0;
            
            for (OrderLine line : orderLines) {
                totalCost += line.getCost();
            }
            
            //Update total cost of order
            updateOrderCost(orderLine.getOrderId(), totalCost);

        } catch (SQLException ex) {
            System.out.println("Failed to upsert order line");
            ex.printStackTrace();
            
            throw new Exception("Failed to upsert order line!");
        }
    }
    
    /**
     * Updates an order's cost
     */
    public void updateOrderCost(int orderId, Double cost) throws Exception {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                UPDATE orders
                SET total_cost = ?
                WHERE id = ?;                                                          
            """);
            
            statement.setDouble(1, cost);
            statement.setInt(2, orderId);
            
            statement.execute();
        } catch (SQLException ex) {
            System.out.println("Failed to update order cost");
            ex.printStackTrace();
            
            throw new Exception("Failed to update order cost!");
        }
    }
    
    /**
     * Gets order by id 
     */
    public Order getOrderById(int id) throws Exception {        
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_BY_ID_QUERY);
            statement.setInt(1, id);
            
            ResultSet result = statement.executeQuery();
            
            boolean any = result.next();
            if (!any) return null;
            
            
            return new Order(
                    result.getInt(1),
                    result.getInt(2),
                    result.getInt(3),
                    result.getString(4),
                    result.getDouble(5)
            );
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
	    throw new Exception("Failed to get order!");
        }
    }

    private Connection creationConnection(String dbName) throws SQLException {
        String connectionUrl = DB_CONNECTION_STRING;
        if (dbName != null)
            connectionUrl += "/" + dbName;

        return DriverManager.getConnection(connectionUrl, DB_USERNAME, DB_PASSWORD);
    }
}
