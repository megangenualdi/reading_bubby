/*
 * CS335 
 * Summer 2024
 * Midterm - Group 1
 * Name: Sophie Steinberger
 * Created: 06/11/2024
 */
package reading_bubby.src;

import java.io.*;
//import java.time.LocalDate;
import java.util.*;

//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

public class User {
    private int userID;
    private String username;
    private ArrayList<CurrentBook> currentlyReading;
    private ArrayList<ReadBook> readBooks;
    private ArrayList<BookGroup> bookGroups;

    public User(int id, String uName){
        userID = id;
        username = uName;
        currentlyReading = new ArrayList<CurrentBook>();
        readBooks = new ArrayList<ReadBook>();
        bookGroups = new ArrayList<BookGroup>();
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

    //MARKER TO INDICATE WORKING HERE (JUST TO CATCH EYE WHEN SCROLLING)
    //MARKER TO INDICATE WORKING HERE (JUST TO CATCH EYE WHEN SCROLLING)
    //MARKER TO INDICATE WORKING HERE (JUST TO CATCH EYE WHEN SCROLLING)


    public void initialSetBookGroups(){
        ArrayList<Integer> groupNums = ReadingBuddy.initialGetBookGroups();
        ArrayList<GroupPost> myGroupPosts = ReadingBuddy.getGroupPostData(groupNums);
        //counter so if all posts have been sorted into groups can stop early
        int counter = 0;
        for(BookGroup i : bookGroups){
            for(GroupPost j : myGroupPosts){
                if(i.getID() == j.getGroupID()){
                    i.addPost(j);
                    counter++;
                }
                if(counter == myGroupPosts.size()){
                    break;
                }
            }
            if(counter == myGroupPosts.size()){
                break;
            }
        }
    }

    //initialGetBookGroups() MOVED TO ReadingBuddy.java

    //MOVED TO ReadingBuddy.java
   /*  public ArrayList<GroupPost> getGroupPostData(ArrayList<Integer> groupNums){
        ArrayList<GroupPost> userGroupPosts = new ArrayList<GroupPost>();
        try { 
            FileReader filereader = new FileReader("reading_bubby/appdata/group_posts.csv"); 
            // create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                    .withSkipLines(1) 
                                    .build(); 
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                //WILL VALUE MATCH? OR NEED TO BE SAME OBJECT?
                if(groupNums.contains(Integer.valueOf(row[2]))){
                    userGroupPosts.add(new GroupPost(row[0], row[1], Integer.parseInt(row[2]), LocalDate.parse(row[3])));
                }
            }
            return userGroupPosts;
        } 
        catch (Exception e) { 
            e.printStackTrace();
            return userGroupPosts;
        } 
    } */

    public void addBookGroup(BookGroup toAdd){
        bookGroups.add(toAdd);
    }
    //IF NEW BOOK GROUP ADD TO CSV IN MAIN FUNCTION

    public ArrayList<CurrentBook> getCurrentlyReading(){
        return currentlyReading;
    }

    //NEW - GET SPECIFIC BOOKGROUP
    //should know book group exists before using function
    public BookGroup getBookGroup(int bookGroupID){
        for(int i = 0; i < bookGroups.size(); i++){
            if(bookGroups.get(i).getID() == bookGroupID){
                return bookGroups.get(i);
            }
        }
        //should never happen
        return new BookGroup(-1, -1, "", "", "", "", -1, -1, false, false);
    }

    public void addToCurrentlyReading(CurrentBook bookToAdd){
        currentlyReading.add(bookToAdd);
        addToCurrentlyReadingCsv(bookToAdd);
    }

    //NEW SPRINT3 - CHANGED TO RETURN THE INDEX NUMBER OF BOOK IN CURRENTLYREADING
    public int checkIfInCurrentBooks(int idToCheck){
        if(currentlyReading.size() > 0){
            for(int i = 0; i < currentlyReading.size(); i++){
                if(idToCheck == currentlyReading.get(i).getID()){
                    return i;
                }
            }
        }
        return -1;
    }

    public void addToCurrentlyReadingCsv(CurrentBook bookToAdd){
        try{
            FileWriter filewriter = new FileWriter("reading_bubby/appdata/currently_reading.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            String[] bookInfo = {Integer.toString(bookToAdd.getID()), bookToAdd.getTitle(), bookToAdd.getAuthor(), 
                username, Integer.toString(0), Integer.toString(-1), Integer.toString(-1), "null", 
                String.valueOf(bookToAdd.getHasOpenSearch()), String.valueOf(bookToAdd.getIsDone())};
            writer.writeNext(bookInfo);
            writer.close();
        }
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public ArrayList<ReadBook> getReadBooks(){
        return readBooks;
    }

    //!!NEW
    public ArrayList<BookGroup> getBookGroups(){
        return bookGroups;
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

    public void removeFromCurrentlyReading(int idx){
       currentlyReading.remove(idx);
    }

    public CurrentBook getSpecificCurrentBook(int bookIDNum){
        for(int i = 0; i < currentlyReading.size(); i++){
            if(currentlyReading.get(i).getID() == bookIDNum){
                return currentlyReading.get(i);
            }
        }
        return new CurrentBook(-1, "", "", "");
    }

    //NEW SPRINT3
    public void removeBookGroup(BookGroup toRemove){
        for(int i = 0; i < bookGroups.size(); i++){
            if(bookGroups.get(i).getID() == toRemove.getID()){
                bookGroups.remove(i);
                break;
            }
        }
    }

}