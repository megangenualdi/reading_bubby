package reading_bubby.src;

public class CurrentBook extends Book{

    private String username;
    private int currentPage;
    private int currentChapter;
    private int groupID;
    private String bookBuddy;
    private boolean hasOpenSearch;
    
    public CurrentBook(int id, String t, String a, String uName){
        super(id, t, a);
        username = uName;
        currentPage = 0;
        currentChapter = -1; //-1 means do not show chapter unless set
        groupID = -1;//-1 indicates no group
        bookBuddy = null;
        hasOpenSearch = false;
    }

    public CurrentBook(int id, String t, String a, String uName, int cpage, int cch, int g, String bb, boolean openSearch){
        super(id, t, a);
        username = uName;
        currentPage = cpage;
        currentChapter = cch;
        groupID = g;
        bookBuddy = bb;
        hasOpenSearch = openSearch;
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
