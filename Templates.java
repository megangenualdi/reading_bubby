/*
 *THIS WILL BE DELETED LATER
 *THIS FILE IS TO HOLD GENERA/TEMPLATE FUNCTIONS THAT WILL BE ADAPTED FOR VARIOUS FUNCTIONS
 */
package reading_bubby;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class Templates {

    //WRITE SINGLE LINE TO CSV FILE
    //from https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/
    public static void writeDataLineByLine(String filePath) { 
        // first create file object for file placed at location 
        // specified by filepath 
        File file = new File(filePath); 
        try { 
            // create FileWriter object with file as parameter 
            FileWriter outputfile = new FileWriter(file); 
    
            // create CSVWriter object filewriter object as parameter 
            CSVWriter writer = new CSVWriter(outputfile); 
    
            // adding header to csv 
            String[] header = { "bookID", "title", "author" }; 
            writer.writeNext(header); 
    
            // add data to csv 
            String[] data1 = { "1", "Harry Potter and The Sorcerer's Stone", "J.K. Rowling" }; 
            writer.writeNext(data1); 
            String[] data2 = { "2", "Bossypants", "Tina Fey" }; 
            writer.writeNext(data2); 
    
            // closing writer connection 
            writer.close(); 
            System.out.println("done!");
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
}
