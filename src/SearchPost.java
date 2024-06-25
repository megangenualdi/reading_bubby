package reading_bubby.src;

import java.time.*;

public class SearchPost {
    //poster = posting user's username
    private String poster;
    private int bookID;
    private String title;
    private String author;
    private LocalDate whenPosted;

    public SearchPost(String userName, int bookId, String t, String a, LocalDate datePosted){
        poster = userName;
        bookID = bookId;
        title = t;
        author = a;
        whenPosted = datePosted;
    }

    public SearchPost(String userName, Book bookForGroup){
        poster = userName;
        bookID = bookForGroup.getID();
        title = bookForGroup.getTitle();
        author = bookForGroup.getAuthor();
        whenPosted = LocalDate.now();
    }

    public int getBookID(){
        return bookID;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getPoster(){
        return poster;
    }

    public LocalDate getWhenPosted(){
        return whenPosted;
    }
}
