/*
 * CS335 
 * Summer 2024
 * Group 1
 * Name: Sophie Steinberger
 * Created: 06/11/2024
 * Last Updated: 07/23/2024
 */

package reading_bubby.src;

public class CurrentBook extends Book{

    private String username;
    private int currentPage;
    private int currentChapter;
    private int groupID;
    private String bookBuddy;
    private boolean hasOpenSearch;
    private boolean isDone;
    
    public CurrentBook(int id, String t, String a, String uName){
        super(id, t, a);
        username = uName;
        currentPage = 0;
        currentChapter = -1; //-1 means do not show chapter unless set
        groupID = -1;//-1 indicates no group
        bookBuddy = "null";
        hasOpenSearch = false;
        isDone = false;
    }

    public CurrentBook(int id, String t, String a, String uName, int cpage, int cch, int g, String bb, boolean openSearch, boolean done){
        super(id, t, a);
        username = uName;
        currentPage = cpage;
        currentChapter = cch;
        groupID = g;
        bookBuddy = bb;
        hasOpenSearch = openSearch;
        isDone = done;
    }

    public String getUsername(){
        return username;
    }

    public int getPage(){
        return currentPage;
    }

    public int getCh(){
        return currentChapter;
    }

    public int getGroup(){
        return groupID;
    }

    public String getBB(){
        return bookBuddy;
    }

    public boolean getIsDone(){
        return isDone;
    }

    public void setIsDone(boolean newVal){
        isDone = newVal;
    }

    public void setCurrentPage(int pageNum){
        currentPage = pageNum;
    }

    public void setCurrentChapter(int chNum){
        currentChapter = chNum;
    }

    public void setGroup(int gId){
        groupID = gId;
    }

    public void setBookBuddy(String bbUsername){
        bookBuddy = bbUsername;
    }

    public void setSearchPostOn(){
        hasOpenSearch = true;
    }

    public void setSearchPostOff(){
        hasOpenSearch = false;
    }

    public boolean getHasOpenSearch(){
        return hasOpenSearch;
    }
}
