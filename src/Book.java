/*
 * CS335 
 * Summer 2024
 * Midterm - Group 1
 * Name: Sophie Steinberger
 * Created: 06/11/2024
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