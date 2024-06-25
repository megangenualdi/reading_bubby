package reading_bubby.src;
import java.time.*;

// will fill in later, just need this for now to reference these objects in other classes
public class GroupPost {
    private String userName;
    private String content;
    private int groupId;
    private LocalDate dayPosted;

    public GroupPost(String uName, String txt, int gID) {
        userName = uName;
        content = txt;
        groupId = gID;
        dayPosted = LocalDate.now();
    }

    public String getUserName(){
        return userName;
    }

    public String getContent(){
        return content;
    }

    public LocalDate getDayPosted(){
        return dayPosted;
    }

    public int getGroupID(){
        return groupId;
    }
}
