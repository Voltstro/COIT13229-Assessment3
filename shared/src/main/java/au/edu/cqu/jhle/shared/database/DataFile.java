package au.edu.cqu.jhle.shared.database;

import au.edu.cqu.jhle.shared.models.Product;
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Integer.parseInt;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataFile {
    
    public DataFile() {
        
    }
    
    public void readFromFile(DatabaseUtility utility) {
        //Read proucts from csv
        String csvFilePath = "A3-products.csv";
        File csvFile = new File(csvFilePath);
        
        //error debugging
        if (!csvFile.exists()) {
            System.err.println("File not found: " + csvFile.getAbsolutePath());
            return;
        }

        try (Scanner scanner = new Scanner(csvFile)) {
            
            // Skip the first line (it is the headers)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                
                /*Comes from csv file. Value 0 is name, value 1 is unit, value 2 is quantity, value 3 is price, value 4 is ingredients */
                Product product = new Product(values[0], parseInt(values[2]), values[1], Double.valueOf(values[3]), values[4]);
                utility.upsertProduct(product);
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(DataFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
