/*
 * CS335 
 * Summer 2024
 * Midterm - Group 1
 * Name: Sophie Steinberger
 * Created: 06/11/2024
 */
package reading_bubby.src;

import java.io.*;
import java.util.*;

//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

public class User {
    private int userID;
    private String username;
    private ArrayList<CurrentBook> currentlyReading;
    private ArrayList<ReadBook> readBooks;

    public User(int id, String uName){
        userID = id;
        username = uName;
        currentlyReading = new ArrayList<CurrentBook>();
        readBooks = new ArrayList<ReadBook>();
    }

    public int getID(){
        return userID;
    }

    public String getUsername(){
        return username;
    }

    public void initialSetCurrentlyReading(ArrayList<CurrentBook> allCurrentBooks){
        //if username matches, add it to user's current books
        for (int i=0; i < allCurrentBooks.size(); i++) {
            if (allCurrentBooks.get(i).getUsername().equals(username)){
                currentlyReading.add(allCurrentBooks.get(i));
            }
        } 
    }

    public void initialSetReadBooks(ArrayList<ReadBook> allReadBooks){
        //if username matches, add it to user's read books
        for (int i=0; i < allReadBooks.size(); i++) {
            if (allReadBooks.get(i).getUsername().equals(username)){
                readBooks.add(allReadBooks.get(i));
            }
        }
    }

    public ArrayList<CurrentBook> getCurrentlyReading(){
        return currentlyReading;
    }

    public void addToCurrentlyReading(CurrentBook bookToAdd){
        
        //FOR TESTING
        System.out.println("currently reading before add: " + currentlyReading.size());

        currentlyReading.add(bookToAdd);
        addToCurrentlyReadingCsv(bookToAdd);

        //FOR TESTING
        System.out.println("currently reading after add: " + currentlyReading.size());
    }

    public boolean checkIfInCurrentBooks(int idToCheck){
        if(currentlyReading.size() > 0){
            for(int i = 0; i < currentlyReading.size(); i++){
                if(idToCheck == currentlyReading.get(i).getID()){
                    return true;
                }
            }
        }
        return false;
    }

    public void addToCurrentlyReadingCsv(CurrentBook bookToAdd){
        try{
            FileWriter filewriter = new FileWriter("reading_bubby/appdata/currently_reading.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            String[] bookInfo = {Integer.toString(bookToAdd.getID()), bookToAdd.getTitle(), bookToAdd.getAuthor(), 
                username, Integer.toString(0), Integer.toString(-1), Integer.toString(-1), "null"};
            writer.writeNext(bookInfo);
            writer.close();
            System.out.println("\n" + bookToAdd.getTitle() + " has been added to your Currently Reading!");
        }
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public ArrayList<ReadBook> getReadBooks(){
        return readBooks;
    }

    public void addToReadBooks(ReadBook bookToAdd){
        readBooks.add(bookToAdd);
        addToReadBooksCsv(bookToAdd);
    }

    public void addToReadBooksCsv(ReadBook bookToAdd){
        try{
            FileWriter filewriter = new FileWriter("reading_bubby/appdata/read_books.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            String[] bookInfo = {Integer.toString(bookToAdd.getID()), bookToAdd.getTitle(), bookToAdd.getAuthor(), username, Integer.toString(-1)};
            writer.writeNext(bookInfo);
            writer.close();
            System.out.println("\n" +bookToAdd.getTitle() + " has been added to your Read Books");
        }
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public boolean checkIfInReadBooks(int bookIdToCheck){
        if(readBooks.size() > 0){
            for(int i = 0; i < readBooks.size(); i++){
                if(bookIdToCheck == readBooks.get(i).getID()){
                    return true;
                }
            }
        }
        return false;
    }


    //DEAL WITH THIS
    public void removeFromCurrentlyReading(int idx){
       currentlyReading.remove(idx);
    }

}