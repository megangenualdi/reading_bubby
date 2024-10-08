/*
 * CS335 
 * Summer 2024
 * Group 1: Megan Genualdi, Jessica Chait, Sophie Steinberger
 * Programmer: Sophie Steinberger
 * Created: 06/11/2024
 * Last Updated: 08/12/2024
 */

package reading_bubby.src;

public class Book {

    private int bookID;
    private String title;
    private String author;
    
    public Book(int id, String t, String a){
        bookID = id;
        title = t;
        author = a;
    }

    public Book(String t, String a){
        bookID = -1;
        title = t;
        author = a;
    }

    public int getID(){
        return bookID;
    }

    //ID can be set but not re-set
    public void setID(int bID){
        if (bookID < 0){
            bookID = bID;
        }
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }
}