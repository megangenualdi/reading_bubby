/*
 * CS335 
 * Summer 2024
 * Group 1: Megan Genualdi, Jessica Chait, Sophie Steinberger
 * Programmer: Sophie Steinberger
 * Created: 06/11/2024
 * Last Updated: 08/12/2024
 */

package reading_bubby.src;
import java.time.*;

public class GroupPost {
    private String userName;
    private String content;
    private int groupId;
    private LocalDate dayPosted;

    //for creating new GroupPosts
    public GroupPost(String uName, String txt, int gID) {
        userName = uName;
        content = txt;
        groupId = gID;
        dayPosted = LocalDate.now();
    }

    //for creating objects for already existing group posts (from the datafile)
    public GroupPost(String uName, String txt, int gID, LocalDate whenPosted) {
        userName = uName;
        content = txt;
        groupId = gID;
        dayPosted = whenPosted;
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
