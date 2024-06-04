package au.edu.cqu.jhle.shared.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataFile {
    
    public static void main(String[] args) {
        
        DatabaseUtility utility = new DatabaseUtility();
        
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
                String[] values = line.split(",");  // Assuming comma as the delimiter
//                for (String value : values) {
//                    System.out.print(value.trim() + "\t");
//                }
//                System.out.println();
                System.out.println(values[0] + " " + values[1] + " " + values[2] + " " + values[3] + " " + values[4]);
                
                /*Comes from csv file. Value 0 is name, value 1 is unit, value 2 is quantity, value 3 is price, value 4 is ingredients */
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
