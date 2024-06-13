package reading_bubby.src;

public class CurrentBook extends Book{

    private String username;
    private int currentPage;
    private int currentChapter;
    private int groupID;
    private String bookBuddy;
    
    public CurrentBook(int id, String t, String a, String uName){
        super(id, t, a);
        username = uName;
        currentPage = 0;
        currentChapter = -1; //-1 means do not show chapter unless set
        groupID = -1;//-1 indicates no group
        bookBuddy = null;
    }

    public CurrentBook(int id, String t, String a, String uName, int cpage, int cch, int g, String bb){
        super(id, t, a);
        username = uName;
        currentPage = cpage;
        currentChapter = cch;
        groupID = g;
        bookBuddy = bb;
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
        if (bookBuddy == null){
            bookBuddy = bbUsername;
        }
    }
}