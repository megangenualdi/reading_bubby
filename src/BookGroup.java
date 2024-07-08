package reading_bubby.src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.opencsv.CSVWriter;

public class BookGroup {

    private int groupID;
    private int bookID;
    private String title;
    private String author;
    //members = usernames of the group members
    private ArrayList<String> members;
    private ArrayList<GroupPost> posts;
    private ArrayList<Integer> currentPages;
    
    public BookGroup(int id, SearchPost wantedPost, String member2){
        groupID = id;
        bookID = wantedPost.getBookID();
        title = wantedPost.getTitle();
        author = wantedPost.getAuthor();
        members = new ArrayList<String>();
        members.add(wantedPost.getPoster());
        members.add(member2);
        posts = new ArrayList<GroupPost>();
        currentPages = new ArrayList<Integer>();
        currentPages.add(Integer.valueOf(0));
        currentPages.add(Integer.valueOf(0));
    }

    public BookGroup(int id, int bID, String t, String a, String member1, String member2, int pagesRead1, int pagesRead2){
        groupID = id;
        bookID = bID;
        title = t;
        author = a;
        members = new ArrayList<String>();
        members.add(member1);
        members.add(member2);
        posts = new ArrayList<GroupPost>();
        currentPages = new ArrayList<Integer>();
        currentPages.add(pagesRead1);
        currentPages.add(pagesRead2);
    }

    public int getID(){
        return groupID;
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

    public ArrayList<String> getMembers(){
        return members;
    }

    public ArrayList<GroupPost> getPosts(){
        return posts;
    }

    public ArrayList<Integer> getCurrentPages() {
        return currentPages;
    }

    public void updateCurrentPage(int newPageNum){
        //can only update own page number when logged in (index in memebers corresponds to position of that user's info in currentPages)
        if(ReadingBuddy.currentUser.getUsername().equals(members.get(0))){
            currentPages.set(0, newPageNum);
        } else if (ReadingBuddy.currentUser.getUsername().equals(members.get(1))){
            currentPages.set(1, newPageNum);
        }
        //ADD UPDATE IN THE GROUP_POSTS.CSV FILE
        //UPDATE PAGE IN CURRENTUSER'S APPROPRIATE CURRENTBOOK (AND IN THE CSV FILE)
    }

    public void addNewPost(){
        GroupPost post = createGroupPost();
        if (post.getGroupID() > 0){
            posts.add(post);
            addNewPostToCsv(post);
        }
    }

    public void addNewPostToCsv(GroupPost post){
        try{
            FileWriter filewriter = new FileWriter("reading_bubby/appdata/group_posts.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            String[] postInfo = {post.getUserName(), post.getContent(), String.valueOf(post.getGroupID()), post.getDayPosted().toString()};
            writer.writeNext(postInfo);
            writer.close();
            System.out.println("\nPosted!\n");
        }
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public void addPost(GroupPost post){
        posts.add(post);
    }

    public GroupPost createGroupPost(){
        GroupPost toAdd;
        String postText;
        //System.out.println("Enter the text of your post below and press return/enter to submit the post:\n");
        postText = ReadingBuddy.checkStrInput("Enter the text of your post below and press return/enter to submit the post:\n");
        int confirmPost = ReadingBuddy.confirmSelection("Do you want to post the text entered?");
        if (confirmPost == 1){
            toAdd = new GroupPost(ReadingBuddy.currentUser.getUsername(), postText, groupID);
        } else {
            toAdd = new GroupPost("", "", -1);
        }
        return toAdd;
    }

    public void showPosts(){
        if(posts.size() > 0){
            System.out.println("-------------------------Group Posts-------------------------");
            for(int x = 0; x < posts.size(); x++){
                System.out.println(posts.get(x).getUserName() + " posted on " + posts.get(x).getDayPosted() + ":");
                System.out.println("\"" + posts.get(x).getContent() + "\"\n");
            }
            System.out.println("-------------------------------------------------------------");
        } else {
            System.out.println("There are no posts in this group yet.");
        }
    }

    public String getBookBuddyName(String ownUsername){
        if(members.get(0).equals(ownUsername)){
            return members.get(1);
        }else{
            return members.get(0);
        }
    }
}
