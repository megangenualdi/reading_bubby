/*
 * CS335 
 * Summer 2024
 * Group 1: Megan Genualdi, Jessica Chait, Sophie Steinberger
 * Programmer: Sophie Steinberger
 * Created: 06/11/2024
 * Last Updated: 08/12/2024
 */

package reading_bubby.src;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import com.opencsv.*;

public class ReadingBuddy {

    private static Scanner myScanner = new Scanner(System.in);
    //VARIABLES TO HOLD DATA FOR "SESSION"
    //track the next id to be assigned to new users, new books, new groups (that is their order in the array)
    static ArrayList<Integer> nextUserBookGroup = new ArrayList<Integer>();
    static User currentUser = new User(-1, null);
    static ArrayList<Book> allBooks = new ArrayList<Book>();
    static ArrayList<CurrentBook> allCurrentBooks = new ArrayList<CurrentBook>();
    static ArrayList<ReadBook> allReadBooks = new ArrayList<ReadBook>();
    static boolean isLoggedIn = false;
    static ArrayList<SearchPost> allSearchPosts = new ArrayList<SearchPost>();
    static ArrayList<BookGroup> allBookGroups = new ArrayList<BookGroup>();
    static ArrayList<GroupPost> allGroupPosts = new ArrayList<GroupPost>();

    //INPUT VALIDATION FUNCTIONS (and helpers)

    public static int validateMenuSelection(int minVal, int maxVal){
        int selection = -1;
        do {
            while (!myScanner.hasNextInt()) {
                myScanner.next();
                if (myScanner.hasNextLine()){
                    System.out.println("Enter the number that corresponds to your menu selection: ");
                }
            }
            selection = myScanner.nextInt();
            if(selection > maxVal || selection < minVal){
                System.out.println("Enter the number that corresponds to your menu selection: ");
            }
        } while (selection > maxVal || selection < minVal);
        return selection;
    }

    //asks a yes/no question and returns the answer
    public static int confirmSelection(String question){
        System.out.println(question);
        System.out.print("1. Yes\n2. No\nEnter the number that corresponds to your menu selection: ");
        int selection = validateMenuSelection(1, 2);
        return selection;
    }

    //check for strings that are empty or only whitespace
    public static String checkStrInput(String prompt){
        System.out.print("\n" + prompt);
        String userInput = myScanner.nextLine();
        //accounting for scanner sometimes but not always coming off reading an int--will only reprompt user if 
        //USER themself enters nothing or only whitespace
        if(userInput.isBlank()){
            userInput = myScanner.nextLine();
        }
        while(userInput.isBlank()){
            System.out.print(prompt);
            userInput = myScanner.nextLine();
        }
        return userInput;
    }

    //used to get+validate username or password w/ no whitespace allowed, but can be used to validate any string that should not contain white space
    //(whatEntered can be username or password or anything else that fits this syntax--whatEntered being the name of the piece of data the user is entering)
    public static String validateInputNoSpaces(String prompt, String whatEntered){
        boolean isValid = false;
        System.out.print(prompt);
        String userInput = myScanner.nextLine();
        //accounting for scanner sometimes but not always coming off reading an int--will only reprompt user if 
        //USER themself enters nothing or only whitespace
        if(userInput.isBlank()){
            userInput = myScanner.nextLine();
        }
        isValid = checkCharsForSpace(userInput);
        while(userInput.isBlank() || !isValid){
            System.out.println("\nThe " + whatEntered + " entered contains whitespace.");
            System.out.print("Enter a " + whatEntered + " without any spaces: ");
            userInput = myScanner.nextLine();
            isValid = checkCharsForSpace(userInput);
        }
        return userInput;
    }

    //return true if valid username, false if contains whitespace
    public static boolean checkCharsForSpace(String toCheck){
        for(int i = 0; i < toCheck.length(); i++){
            if(Character.isWhitespace(toCheck.charAt(i))){
                return false;
            }
        }
        return true;
    }    

    //FUNCTIONS FOR INITIAL READING IN OF DATA
    public static void getNextIds(){
        try { 
            FileReader filereader = new FileReader("reading_bubby/appdata/next_id_nums.csv"); 
            CSVReader csvReader = new CSVReader(filereader); 
            String[] csvLine = csvReader.readNext();
            nextUserBookGroup.add(Integer.valueOf(csvLine[0]));
            nextUserBookGroup.add(Integer.valueOf(csvLine[1]));
            nextUserBookGroup.add(Integer.valueOf(csvLine[2]));
            csvReader.close();
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    public static ArrayList<String[]> getAllUserData(){
        ArrayList<String[]> allUsers = new ArrayList<String[]>();
        try { 
            FileReader filereader = new FileReader("reading_bubby/appdata/users.csv"); 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                      .withSkipLines(1) 
                                      .build(); 
            List<String[]> allData = csvReader.readAll(); 
            for (String[] row : allData) {
                allUsers.add(row);
            }
            return allUsers;
        } 
        catch (Exception e) { 
            e.printStackTrace();
        } 
        return allUsers;
    }

    //gather all current book file entries
    public static void getAllCurrentBooksData(){
        try { 
            FileReader filereader = new FileReader("reading_bubby/appdata/currently_reading.csv"); 
            // create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                      .withSkipLines(1) 
                                      .build(); 
            List<String[]> allData = csvReader.readAll(); 
            CurrentBook tempCurrentBook;
            for (String[] row : allData) {
                tempCurrentBook = new CurrentBook(Integer.parseInt(row[0]), row[1], row[2], row[3], Integer.parseInt(row[4]), Integer.parseInt(row[5]), Integer.parseInt(row[6]), row[7], Boolean.valueOf(row[8]), Boolean.valueOf(row[9]));
                allCurrentBooks.add(tempCurrentBook);
            }
        } 
        catch (Exception e) { 
            e.printStackTrace();
        } 
    }

    public static void getAllReadBooksData(){
        try {
            FileReader filereader = new FileReader("reading_bubby/appdata/read_books.csv"); 
            // create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                      .withSkipLines(1) 
                                      .build(); 
            List<String[]> allData = csvReader.readAll(); 
            ReadBook tempReadBook;
            for (String[] row : allData) {
                tempReadBook = new ReadBook(Integer.parseInt(row[0]), row[1], row[2], row[3], Integer.parseInt(row[4]));
                allReadBooks.add(tempReadBook);
            }
        } 
        catch (Exception e) { 
            e.printStackTrace();
        } 
    }

    public static void getAllSearchPosts(){
        try { 
            // Create an object of file reader class with CSV file as a parameter. 
            FileReader filereader = new FileReader("reading_bubby/appdata/search_posts.csv"); 
            // create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                      .withSkipLines(1) 
                                      .build(); 
            List<String[]> allData = csvReader.readAll(); 
            SearchPost tempPost;
            for (String[] row : allData) {
                tempPost = new SearchPost(row[0], Integer.parseInt(row[1]), row[2], row[3], LocalDate.parse(row[4]));
                allSearchPosts.add(tempPost);
            }
        } 
        catch (Exception e) { 
            e.printStackTrace();
        } 
    }

    public static void getAllBooks(){
        try {  
            FileReader filereader = new FileReader("reading_bubby/appdata/books.csv"); 
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
            List<String[]> allData = csvReader.readAll(); 
            Book tempBook;
            //if username matches, add it to user's read books
            for (String[] row : allData) {
                tempBook = new Book(Integer.parseInt(row[0]), row[1], row[2]);
                allBooks.add(tempBook);
            }
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        } 
    }

    //adds groups to a static variable and to the user's own attribute
    public static ArrayList<Integer> initialGetBookGroups(){
        ArrayList<Integer> groupNums = new ArrayList<Integer>();
        try { 
            FileReader filereader = new FileReader("reading_bubby/appdata/book_groups.csv"); 
            //create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                    .withSkipLines(1) 
                                    .build(); 
            List<String[]> allData = csvReader.readAll(); 
            BookGroup tempGroup;
            for (String[] row : allData) {
                tempGroup = new BookGroup(Integer.parseInt(row[0]), Integer.parseInt(row[1]), row[2], row[3], row[4],
                 row[5], Integer.parseInt(row[6]), Integer.parseInt(row[7]), Boolean.valueOf(row[8]), Boolean.valueOf(row[9]));
                allBookGroups.add(tempGroup);
                if(row[4].equals(currentUser.getUsername()) || row[5].equals(currentUser.getUsername())){
                    currentUser.addBookGroup(tempGroup);
                    groupNums.add(Integer.valueOf(row[0]));
                }
            }
            return groupNums;
        } 
        catch (Exception e) { 
            e.printStackTrace();
            return groupNums;
        }
    }

    public static ArrayList<GroupPost> getGroupPostData(ArrayList<Integer> groupNums){
        ArrayList<GroupPost> userGroupPosts = new ArrayList<GroupPost>();
        try { 
            FileReader filereader = new FileReader("reading_bubby/appdata/group_posts.csv"); 
            // create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                    .withSkipLines(1) 
                                    .build(); 
            List<String[]> allData = csvReader.readAll();
            GroupPost tempPost;
            for (String[] row : allData) {
                tempPost = new GroupPost(row[0], row[1], Integer.parseInt(row[2]), LocalDate.parse(row[3]));
                allGroupPosts.add(tempPost);
                if(groupNums.contains(Integer.valueOf(row[2]))){
                    userGroupPosts.add(tempPost);
                }
            }
            return userGroupPosts;
        } 
        catch (Exception e) { 
            e.printStackTrace();
            return userGroupPosts;
        } 
    }

    //FUNCTIONS FOR UPDATING, DELETING, AND WRITING NEW DATA (and helpers)

    public static void removeGroupsPosts(BookGroup groupToRemove){
        File file = new File("reading_bubby/appdata/group_posts.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"username","content","group_id","day_posted"});
            for(int x = 0; x < allGroupPosts.size(); x++){
                if(groupToRemove.getID() == allGroupPosts.get(x).getGroupID()){
                    allGroupPosts.remove(x);
                } else {
                    data.add(new String[] {allGroupPosts.get(x).getUserName(), allGroupPosts.get(x).getContent(),
                     Integer.toString(allGroupPosts.get(x).getGroupID()), allGroupPosts.get(x).getDayPosted().toString()});
                }
            }
            writer.writeAll(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    //remove a bookgroup from allBookGroups and grom the book_groups.csv file
    public static void removeBookGroup(BookGroup toRemove){
        File file = new File("reading_bubby/appdata/book_groups.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"group_id","book_id","title","author","member1","member2","pages_read1","pages_read2","is_done1","is_done2"});
            for(int x = 0; x < allBookGroups.size(); x++){
                if(toRemove.getID() == allBookGroups.get(x).getID()){
                    allBookGroups.remove(x);
                } else {
                    data.add(new String[] {Integer.toString(allBookGroups.get(x).getID()), Integer.toString(allBookGroups.get(x).getBookID()),
                        allBookGroups.get(x).getTitle(), allBookGroups.get(x).getAuthor(), allBookGroups.get(x).getMembers().get(0), 
                        allBookGroups.get(x).getMembers().get(1), Integer.toString(allBookGroups.get(x).getCurrentPages().get(0)),
                        Integer.toString(allBookGroups.get(x).getCurrentPages().get(1)), Boolean.toString(allBookGroups.get(x).getIsDone().get(0)),
                        Boolean.toString(allBookGroups.get(x).getIsDone().get(1))});
                }
            }
            writer.writeAll(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public static void removeCurrentBook(CurrentBook toRemove){
        //remove from data file
        File file = new File("reading_bubby/appdata/currently_reading.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"book_id","title","author","username","current_page","current_chapter","group_id","book_buddy","has_open_search","is_done"});
            for(int x = 0; x < allCurrentBooks.size(); x++){
                if((toRemove.getID() == allCurrentBooks.get(x).getID()) && (toRemove.getUsername().equals(allCurrentBooks.get(x).getUsername()))){
                    //when the selected book is found do not write it to the data but remove it from the user's ArrayList and allCurrentBooks Arraylist
                    if(toRemove.getHasOpenSearch()){
                        SearchPost postToDel = searchForSpecificSearchPost(toRemove.getTitle(), toRemove.getAuthor(),
                         toRemove.getUsername());
                        if(postToDel.getBookID() == -1){
                            removeSearchPost(postToDel);
                        }
                    }
                    //remove from currentlyReading if for currentUser's book
                    if(toRemove.getUsername().equals(currentUser.getUsername())){
                        currentUser.removeFromCurrentlyReading(currentUser.checkIfInCurrentBooks(toRemove.getID()));
                    }
                    allCurrentBooks.remove(x);
                } else {
                    data.add(new String[] {Integer.toString(allCurrentBooks.get(x).getID()), 
                        allCurrentBooks.get(x).getTitle(), allCurrentBooks.get(x).getAuthor(), 
                        allCurrentBooks.get(x).getUsername(), Integer.toString(allCurrentBooks.get(x).getPage()), 
                        Integer.toString(allCurrentBooks.get(x).getCh()), 
                        Integer.toString(allCurrentBooks.get(x).getGroup()), allCurrentBooks.get(x).getBB(), 
                        Boolean.toString(allCurrentBooks.get(x).getHasOpenSearch()), Boolean.toString(allCurrentBooks.get(x).getIsDone())});
                }
            }
            writer.writeAll(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public static void updateCurrentlyReading(){
        File file = new File("reading_bubby/appdata/currently_reading.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile); 
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"book_id","title","author","username","current_page","current_chapter","group_id","book_buddy","has_open_search","is_done"});
            for(int x = 0; x < allCurrentBooks.size(); x++){
                data.add(new String[] {Integer.toString(allCurrentBooks.get(x).getID()), allCurrentBooks.get(x).getTitle(), 
                    allCurrentBooks.get(x).getAuthor(), allCurrentBooks.get(x).getUsername(), Integer.toString(allCurrentBooks.get(x).getPage()),
                    Integer.toString(allCurrentBooks.get(x).getCh()), Integer.toString(allCurrentBooks.get(x).getGroup()),
                    allCurrentBooks.get(x).getBB(), Boolean.toString(allCurrentBooks.get(x).getHasOpenSearch()), Boolean.toString(allCurrentBooks.get(x).getIsDone())});
            }
            writer.writeAll(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public static void updateBookGroups(){
        File file = new File("reading_bubby/appdata/book_groups.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile); 
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"group_id","book_id","title","author","member1","member2","pages_read1","pages_read2","is_done1","is_done2"});
            for(int x = 0; x < allBookGroups.size(); x++){
                data.add(new String[] {Integer.toString(allBookGroups.get(x).getID()), Integer.toString(allBookGroups.get(x).getBookID()),
                    allBookGroups.get(x).getTitle(), allBookGroups.get(x).getAuthor(), allBookGroups.get(x).getMembers().get(0),
                    allBookGroups.get(x).getMembers().get(1), Integer.toString(allBookGroups.get(x).getCurrentPages().get(0)),
                    Integer.toString(allBookGroups.get(x).getCurrentPages().get(1)), 
                    Boolean.toString(allBookGroups.get(x).getIsDone().get(0)), Boolean.toString(allBookGroups.get(x).getIsDone().get(1))});
            }
            writer.writeAll(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public static void updateReadBookRating(){
        File file = new File("reading_bubby/appdata/read_books.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile); 
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"book_id","title","author","username","rating"});
            for(int x = 0; x < allReadBooks.size(); x++){
                data.add(new String[] {Integer.toString(allReadBooks.get(x).getID()), 
                    allReadBooks.get(x).getTitle(), allReadBooks.get(x).getAuthor(), 
                    allReadBooks.get(x).getUsername(), Integer.toString(allReadBooks.get(x).getRating())});
            }
            writer.writeAll(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    //takes a number to know which column (0=userIDs, 1=bookIDs, 2=groupIds) and increments it
    public static void incrementNextIdNums(int colNum){
        //adapted from geeksforgeeks
        File file = new File("reading_bubby/appdata/next_id_nums.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile); 
            // add data to csv
            String[] data;
            if (colNum == 0){
                data = new String[] {Integer.toString(nextUserBookGroup.get(0)+1), Integer.toString(nextUserBookGroup.get(1)), Integer.toString(nextUserBookGroup.get(2))};
                nextUserBookGroup.set(colNum, (nextUserBookGroup.get(0)+1));
            } else if (colNum == 1){
                data = new String[] {Integer.toString(nextUserBookGroup.get(0)), Integer.toString(nextUserBookGroup.get(1)+1), Integer.toString(nextUserBookGroup.get(2))};
                nextUserBookGroup.set(colNum, (nextUserBookGroup.get(1)+1));
            } else {
                data = new String[] {Integer.toString(nextUserBookGroup.get(0)), Integer.toString(nextUserBookGroup.get(1)), Integer.toString(nextUserBookGroup.get(2)+1)};
                nextUserBookGroup.set(colNum, (nextUserBookGroup.get(2)+1));
            }
            writer.writeNext(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public static ArrayList<String[]> addNewUserData(String uname, String pw, ArrayList<String[]> allUsers){
        try{
            FileWriter filewriter = new FileWriter("reading_bubby/appdata/users.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            String[] userInfo = {Integer.toString(nextUserBookGroup.get(0)), uname, pw};
            writer.writeNext(userInfo);
            writer.close();
            allUsers.add(userInfo);
            System.out.println("Your account has been added!\n");
            //increment user number
            incrementNextIdNums(0);
            return allUsers;
        }
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return allUsers;
    }

    public static CurrentBook getSpecificCurrentBook(String userName, int bookID){
        for(int i = 0; i < allCurrentBooks.size(); i++){
            if(allCurrentBooks.get(i).getUsername().equals(userName) &&
            allCurrentBooks.get(i).getID() == bookID){
                return allCurrentBooks.get(i);
            }
        }
        return new CurrentBook(-1, "", "", "");
    }

    public static void updateBookBuddysEntry(BookGroup groupInfo){
        CurrentBook toUpdate = getSpecificCurrentBook(groupInfo.getBookBuddyName(currentUser.getUsername()), groupInfo.getBookID());
        toUpdate.setSearchPostOff();
        toUpdate.setBookBuddy(currentUser.getUsername());
        toUpdate.setGroup(groupInfo.getID());
        updateCurrentlyReading();
    }

    public static void removeSearchPost(SearchPost toRemove){
        //remove from data file
        File file = new File("reading_bubby/appdata/search_posts.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile); 
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"poster","book_id","title","author","date_posted"});
            for(int x = 0; x < allSearchPosts.size(); x++){
                if((toRemove.getBookID() == allSearchPosts.get(x).getBookID()) && (toRemove.getPoster().equals(allSearchPosts.get(x).getPoster()))){
                    //when the selected book is found do not write it to the data but remove it from the user's ArrayList and allSearchPosts Arraylist
                    allSearchPosts.remove(x);
                } else {
                    data.add(new String[] {allSearchPosts.get(x).getPoster(), Integer.toString(allSearchPosts.get(x).getBookID()),
                        allSearchPosts.get(x).getTitle(), allSearchPosts.get(x).getAuthor(), 
                        allSearchPosts.get(x).getWhenPosted().toString()});
                }
            }
            writer.writeAll(data); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public static void createNewBookGroup(SearchPost wantedPost){
        boolean createGroup;
        //create group but do not increment the nextUserBookGroup number until/only increment if okayed to make group
        BookGroup newGroup = new BookGroup(nextUserBookGroup.get(2), wantedPost, currentUser.getUsername());
        if(currentUser.checkIfInCurrentBooks(wantedPost.getBookID()) >= 0){
            CurrentBook toUpdate = currentUser.getSpecificCurrentBook(wantedPost.getBookID());
            if(toUpdate.getGroup() > 0){
                System.out.println("You already have a Book Buddy for this book! Check out your book group by selecting Manage Book Groups from the Main Menu.\n");
                System.out.println("____________________________");
                createGroup = false;
            }else{
                createGroup = true;
                toUpdate.setSearchPostOff();
                toUpdate.setGroup(newGroup.getID());
                toUpdate.setBookBuddy(newGroup.getBookBuddyName(currentUser.getUsername()));
            }
        }else{
            CurrentBook toAdd = new CurrentBook(newGroup.getBookID(), newGroup.getTitle(), newGroup.getAuthor(), 
                currentUser.getUsername(), 0, -1, newGroup.getID(), newGroup.getBookBuddyName(currentUser.getUsername()),
                 false, false);
            allCurrentBooks.add(toAdd);
            currentUser.addToCurrentlyReading(toAdd);
            createGroup = true;
        }
        if(createGroup){
            updateCurrentlyReading();
            currentUser.addBookGroup(newGroup);
            allBookGroups.add(newGroup);
            removeSearchPost(wantedPost);
            try{
                FileWriter filewriter = new FileWriter("reading_bubby/appdata/book_groups.csv", true);
                CSVWriter writer = new CSVWriter(filewriter);
                String[] groupInfo = {Integer.toString(nextUserBookGroup.get(2)), Integer.toString(wantedPost.getBookID()),
                    wantedPost.getTitle(), wantedPost.getAuthor(), wantedPost.getPoster(), currentUser.getUsername(), 
                    Integer.toString(0), Integer.toString(0), Boolean.toString(false), Boolean.toString(false)};
                writer.writeNext(groupInfo);
                writer.close();
                incrementNextIdNums(2);
                System.out.println("\nYou are now book buddies with " + wantedPost.getPoster() + " to read " +
                    newGroup.getTitle() + " by " + newGroup.getAuthor() + "!\n\n");
                System.out.println("____________________________");
            }
            catch (IOException e) { 
                e.printStackTrace(); 
            }
        }
    }

    public static boolean checkBeforeCreateSearchPost(CurrentBook toCheck){
        boolean moveForward;
        if(toCheck.getGroup() > -1){
            System.out.println("You already have a Book Buddy for this book! Check out your book group by selecting Manage Book Groups from the Main Menu.\n");
            System.out.println("____________________________");
            moveForward = false;
        }else if(toCheck.getHasOpenSearch()){
            System.out.println("You alredy have a \"Book Buddy Wanted\" post for this book!\n");
            System.out.println("____________________________");
            moveForward = false;
        }else{
            moveForward = true;
        }
        return moveForward;
    }

    public static void createNewSearchPost(Book toAdd){
        CurrentBook toUpdate;
        boolean addPost;
        if(currentUser.checkIfInCurrentBooks(toAdd.getID()) >= 0){
            toUpdate = currentUser.getSpecificCurrentBook(toAdd.getID());
            addPost = checkBeforeCreateSearchPost(toUpdate);
            if(addPost){
                toUpdate.setSearchPostOn();
                updateCurrentlyReading();
            }
        }else{
            addPost = true;
            toUpdate = new CurrentBook(toAdd.getID(), toAdd.getTitle(), toAdd.getAuthor(), currentUser.getUsername());
            toUpdate.setSearchPostOn();
            allCurrentBooks.add(toUpdate);
            currentUser.addToCurrentlyReading(toUpdate);
        }
        if(addPost){
            SearchPost newPost = new SearchPost(currentUser.getUsername(), toAdd);
            allSearchPosts.add(newPost);
            try{
                FileWriter filewriter = new FileWriter("reading_bubby/appdata/search_posts.csv", true);
                CSVWriter writer = new CSVWriter(filewriter);
                String[] postInfo = {currentUser.getUsername(), Integer.toString(newPost.getBookID()), newPost.getTitle(), newPost.getAuthor(), newPost.getWhenPosted().toString()};
                writer.writeNext(postInfo);
                writer.close();
            }
            catch (IOException e) { 
                e.printStackTrace(); 
            } 
        }
    }

    //overload method to be able to take in a Book or CurrentBook
    public static void createNewSearchPost(CurrentBook toAdd){
        boolean makePost = checkBeforeCreateSearchPost(toAdd);
        if(makePost){
            toAdd.setSearchPostOn();
            updateCurrentlyReading();
            SearchPost newPost = new SearchPost(currentUser.getUsername(), toAdd);
            allSearchPosts.add(newPost);
            try{
                FileWriter filewriter = new FileWriter("reading_bubby/appdata/search_posts.csv", true);
                CSVWriter writer = new CSVWriter(filewriter);
                String[] postInfo = {currentUser.getUsername(), Integer.toString(newPost.getBookID()), newPost.getTitle(), newPost.getAuthor(), newPost.getWhenPosted().toString()};
                writer.writeNext(postInfo);
                writer.close();
            }
            catch (IOException e) { 
                e.printStackTrace(); 
            } 
        }
    }

    //MENU FUNCTIONS

    public static void manageEntry(){
        //arraylist below will hold arrays with userID, username, password (all would not be pulled into program in using actual DB)
        ArrayList<String[]> allUsers = getAllUserData();

        while (currentUser.getID() == -1 || !isLoggedIn){
            System.out.println("\nWelcome to Book Buddy!");
            System.out.println("""
                    \nEnter the number corresponding to your menu selection:\n
                    1. Login
                    2. Create an Account
                    """);
            int menuSelection = validateMenuSelection(1, 2);
            if(menuSelection == 1){
                login(allUsers);
            } else {
                allUsers = createNewAccount(allUsers);
            }
        }
    }

    public static int mainMenu(){
        System.out.println("\n____________________________\n");
        System.out.println("Main Menu:\n");
        System.out.println("1. Manage My Books\n2. Find a Book Buddy!\n3. Manage Book Buddies & Groups\n4. Exit\n");
        System.out.print("Enter the number that corresponds to your menu selection: ");
        int selectedOption = validateMenuSelection(1, 4);
        return selectedOption;
    }

    public static void manageMyBooksMenu(){
        boolean stayBooksMenu = true;
        while (stayBooksMenu){
            System.out.println("____________________________");
            System.out.println("\nMy Books Menu:\n");
            System.out.println("""
                1. View and Manage Currently Reading Books\n2. Add a Book to Currently Reading List\n3. View Finished Books\n4. Return to Main Menu\n""");
            System.out.print("Enter the number that corresponds to your menu selection: ");
            int menuSelection = validateMenuSelection(1, 4);
            switch(menuSelection){
                case 1:
                    //view and update currently reading
                    manageCurrentBooks();
                    break;
                case 2:
                    //Add a book to currently reading
                    addCurrentBookMenu();
                    break;
                case 3:
                    //view finished books (view list, rate/re-rate books)
                    manageViewReadBooks();
                    break;
                case 4:
                    //return to main menu
                    stayBooksMenu = false;
                    break;
            }
        }
    }

    public static void searchForBookBuddyMenu(){
        boolean returnToMenu = true;
        int menuSelection;
        while (returnToMenu) {
            System.out.println("\n____________________________\n");
            System.out.print("""
                        Book Buddy Finder Menu\n
                        1. Select a book from your currently reading
                        2. Enter a book (title and author)
                        3. Select a book from our list of books
                        4. View all \"Book Buddy Wanted\" Posts
                        5. Manage your \"Book Buddy Wanted\" posts

                        Choose the number that corresponds your selection \nEnter 0 to return to the Main Menu \nEnter the number that corresponds to your menu selection:""");
            menuSelection = validateMenuSelection(0, 5);
            if(menuSelection == 1){
                bookBuddyFromCurrentlyReading();
            } else if(menuSelection == 2){
                enterSearchPostByBook();
            }else if(menuSelection == 3){
                chooseFromAllBooks();
            }else if (menuSelection == 4){
                viewAllSearchPosts();
            }else if(menuSelection == 5){
                viewAndManageOwnSearchPosts();
            }else{
                returnToMenu = false;
            }
        }
    }

    public static void viewAndManageOwnSearchPosts(){
        //get all search posts by user
        ArrayList<SearchPost> usersPosts = new ArrayList<SearchPost>();
        int menuSelection;
        int menu2Selection;
        for(int i = 0; i < allSearchPosts.size(); i++){
            if(currentUser.getUsername().equals(allSearchPosts.get(i).getPoster())){
                usersPosts.add(allSearchPosts.get(i));
            }
        }
        //display search posts
        boolean stayIn = true;
        while (stayIn) {
            if(usersPosts.size() == 0){
                System.out.println("____________________________");
                System.out.println("\nYou currently have no \"Book Buddy Wanted\" posts.\n\n");
                stayIn = false;
            }else{
                System.out.println("____________________________\n");
                System.out.println("Your Book Buddy Wanted Posts:\n");
                for(int x = 1; x <= usersPosts.size(); x++){
                    System.out.println(x + ". " + usersPosts.get(x-1).getTitle() + " by " + usersPosts.get(x-1).getAuthor());
                    System.out.println("   Posted on " + usersPosts.get(x-1).getWhenPosted() + "\n");
                }
                System.out.print("""
                        \nEnter the number corresponding to a Book Buddy Wanted post to delete it,
                        Enter 0 to return to the \"Find a Book Buddy\" menu:  """);
                menuSelection = validateMenuSelection(0, usersPosts.size());
                if(menuSelection == 0){
                    stayIn = false;
                }else{
                    menu2Selection = confirmSelection("\nAre you sure you want to delete the search post for "
                     + usersPosts.get(menuSelection-1).getTitle() + " by " + usersPosts.get(menuSelection-1).getAuthor()
                      + "?\n");
                    if(menu2Selection == 1){
                        CurrentBook toUpdate = currentUser.getSpecificCurrentBook(usersPosts.get(menuSelection-1).getBookID());
                        toUpdate.setSearchPostOff();
                        updateCurrentlyReading();
                        removeSearchPost(usersPosts.get(menuSelection-1));
                        usersPosts.remove(menuSelection-1);
                    }
                }
            }
        }
    }

    public static boolean checkForOwnSearchPost(ArrayList<SearchPost> toCheck, String title, String author){
        for(int i = 0; i < toCheck.size(); i++){
            if(toCheck.get(i).getPoster().equals(currentUser.getUsername()) && toCheck.get(i).getTitle().equals(title) &&
            toCheck.get(i).getAuthor().equals(author)){
                return true;
            }
        }
        return false;
    }

    public static int displayAndGetSearchPostSelection(ArrayList<SearchPost> posts){
        //returns an int corresponding to a menu selection-> 0=back to menu
        //!!!IF return val > 0 AND <= passed in ArrayList size: that number minus (-) 1 is the index number of selected post
        // to accept in the ArrayList passed in)
        //!!IF return val == ArrayList passed in .size()+1: user selects make a new post for that book
        System.out.println("____________________________");
        System.out.println("Posts Searching for a Book Buddy to Read " + posts.get(0).getTitle() + 
        " by " + posts.get(0).getAuthor() + ":\n");
        int x;
        for(x = 0; x < posts.size(); x++){
            System.out.println((x+1) + ". Read with " + posts.get(x).getPoster() + "\n");
        }
        System.out.println((posts.size()+1) + ". Create your own \"Book Buddy Wanted\" post");
        System.out.print("Enter the corresponding number to select a post and become Book Buddies, " +
            "or enter " + (posts.size()+1) + " to create your own post, or 0 to return to the menu:");
        int menuSelection = validateMenuSelection(0, posts.size()+1);
        return menuSelection;
    }

    public static void chooseFromAllBooks(){
        boolean stayInMenu = true;
        boolean followUp = false;
        int menuSelection;
        Book selectedBook;
        CurrentBook currentSelectedBook;
        ArrayList<SearchPost> posts;
        int seePosts = -1;
        int menu2Selection = -1;
        int menu3Selection = -1;
        System.out.println("\n");
        System.out.println("____________________________");
        while (stayInMenu) {
            for(int i = 0; i < allBooks.size(); i++){
                System.out.println((i+1) + ". " + allBooks.get(i).getTitle() + " by " + allBooks.get(i).getAuthor());
            }
            System.out.print("\nEnter the corresponding number to select a book to find a book buddy for (make a Book Buddy" +
             "Wanted post or answer someone else's), or 0 to return to the menu:");
            menuSelection = validateMenuSelection(0, allBooks.size());
            if(menuSelection > 0){
                selectedBook = allBooks.get(menuSelection-1);
                if(currentUser.checkIfInCurrentBooks(selectedBook.getID()) >= 0){
                    currentSelectedBook = currentUser.getSpecificCurrentBook(selectedBook.getID());
                    if(currentSelectedBook.getHasOpenSearch()){
                        System.out.println("\nLooks like you already have a post for that book!\n");
                        followUp = true;
                    }else if(currentSelectedBook.getGroup() > 0){
                        System.out.println("\nYou are already in a book group for that book!\n");
                        followUp = true;
                    }
                }
                if(followUp == false){
                    posts = searchForSearchPosts(selectedBook.getTitle(), selectedBook.getAuthor());
                    if(posts.size() > 0){
                        seePosts = confirmSelection("Would you like to see other users' Book Buddy Wanted posts for this book?");
                        if(seePosts == 1){
                            menu2Selection = displayAndGetSearchPostSelection(posts);
                            if (menu2Selection > 0 && menu2Selection <= posts.size()){
                                createNewBookGroup(posts.get(menuSelection-1));
                                stayInMenu = false;
                            }
                        }
                    }
                    if(posts.size() == 0 || seePosts == 2 || menu2Selection == (posts.size()+1)){
                        menu3Selection = confirmSelection("Create a \"Book Buddy Wanted\" post for reading " +
                            selectedBook.getTitle() + " by " + selectedBook.getAuthor() + "?");
                        if(menu3Selection == 1){
                            createNewSearchPost(selectedBook);
                            stayInMenu = false;
                            followUp = false;
                            System.out.println("Posted!\n\n");
                        }
                    }
                }
                if(followUp || menu3Selection == 2){
                    int menu4Selection = confirmSelection("Would you like to select a different book?");
                    if(menu4Selection == 2){
                        stayInMenu = false;
                    }
                }
            }else{
                stayInMenu = false;
            }
        }
    }

    public static void viewAllSearchPosts(){
        System.out.println("____________________________");
        System.out.println("All \"Book Buddy Wanted\" Posts:\n\n");
        int i;
        ArrayList<SearchPost> postsToShow = new ArrayList<SearchPost>();
        
        for(int x = 0; x < allSearchPosts.size(); x++){
            if(!allSearchPosts.get(x).getPoster().equals(currentUser.getUsername())){
                postsToShow.add(allSearchPosts.get(x));
            }
        }
        System.out.println("number of posts to show: " + postsToShow.size());
        if(postsToShow.size() > 0){
            for(i = 0; i < postsToShow.size(); i++){
                System.out.println((i+1) + ". Read " + postsToShow.get(i).getTitle() + " by " +
                postsToShow.get(i).getAuthor() + " with " + postsToShow.get(i).getPoster() + "\n");
            }
            System.out.print("Enter the corresponding number to select a post and become Book Buddies " +
            "or 0 to return to the menu:");
            int menuSelection = validateMenuSelection(0, postsToShow.size());
            if(menuSelection > 0){
                createNewBookGroup(postsToShow.get(menuSelection-1));
            }
        }else{
            System.out.println("\nThere are no post from other users to show right now. Check back later or create your own!\n");
        }
    }

    public static void enterSearchPostByBook(){
        int seePosts = -1;
        boolean endNow = false;
        System.out.println("____________________________");
        String title = checkStrInput("Enter the book's title: ");
        String author = checkStrInput("Enter the name of the book's author: ");
        //This array should only have one item (if any) because search by title and author
        ArrayList<Book> searchMatches = searchAllBooks(title, author);
        if(searchMatches.size() > 0){
            //!!FIX implemented 08/12/2024 -- moved below line from line 902
            int bookIndex = currentUser.checkIfInCurrentBooks(searchMatches.get(0).getID());
            int menuSelection = -1;
            if(bookIndex >= 0){
                if(currentUser.getCurrentlyReading().get(bookIndex).getGroup() > 0){
                    System.out.println("You already have a Book Buddy for this book! Check out your book group by selecting Manage Book Groups from the Main Menu.\n");
                    endNow = true;
                }
            }
            ArrayList<SearchPost> postMatches = searchForSearchPosts(title, author);
            if(postMatches.size() > 0 && endNow == false){
                boolean alreadyHasPost = checkForOwnSearchPost(postMatches, title, author);
                if(alreadyHasPost){
                    System.out.println("\nLooks like you already have a post for that book!\n");
                    System.out.println("____________________________");
                    endNow = true;
                }else{
                    seePosts = confirmSelection("Would you like to see other users' Book Buddy Wanted posts for this book?");
                    if(seePosts == 1){
                        menuSelection = displayAndGetSearchPostSelection(postMatches);
                        if (menuSelection > 0 && menuSelection <= postMatches.size()){
                            createNewBookGroup(postMatches.get(menuSelection-1));
                        }
                    }
                }
            }
            if((postMatches.size() == 0 || menuSelection == postMatches.size()+1 || seePosts == 2) && endNow == false){
                int menu2Selection = confirmSelection("Create a \"Book Buddy Wanted\" post for reading " +
                title + " by " + author + "?");
                if(menu2Selection == 1){
                    createNewSearchPost(searchMatches.get(0));
                    System.out.println("Posted!\n\n");
                    System.out.println("____________________________");
                }
            }
        } else {
            int menu3Selection = confirmSelection("Create a \"Book Buddy Wanted\" post for reading " +
                title + " by " + author + "?");
            if(menu3Selection == 1){
                Book bookToAdd = addNewBook(title, author);
                createNewSearchPost(bookToAdd);
                System.out.println("Posted!\n\n");
                System.out.println("____________________________");
            }else{
                System.out.println("Returning you to menu\n\n");
                System.out.println("____________________________");
            }
        }
    }

    public static void bookBuddyFromCurrentlyReading(){
        int menuSelection;
        int menu2Selection = -1;
        int menu3Selection;
        if (currentUser.getCurrentlyReading().size() < 1){
            System.out.println("You have no books in your Currently Reading.\n");
            System.out.println("____________________________");
        } else {
            //use a separate counter since will skip books that already have a book buddy
            int counter = 0;
            //keep books that don't have a buddy in temp separate array to have corresponding number
            ArrayList<CurrentBook> tempCurrentBooks = new ArrayList<CurrentBook>();
            System.out.println("\n____________________________\n");
            for(int i = 1; i <= currentUser.getCurrentlyReading().size(); i++){
                if(currentUser.getCurrentlyReading().get(i-1).getGroup() == -1 &&
                 !currentUser.getCurrentlyReading().get(i-1).getHasOpenSearch()){
                    counter++;
                    System.out.println(counter + ". " + currentUser.getCurrentlyReading().get(i-1).getTitle() + " by "
                    + currentUser.getCurrentlyReading().get(i-1).getAuthor());
                    System.out.println("\tCurrent Page: " + currentUser.getCurrentlyReading().get(i-1).getPage()
                    + " Current Chapter: " + (currentUser.getCurrentlyReading().get(i-1).getCh() != -1 ? currentUser.getCurrentlyReading().get(i-1).getCh() : "-"));
                    System.out.println((""));
                    tempCurrentBooks.add(currentUser.getCurrentlyReading().get(i-1));
                }
            }
            System.out.print("To select a book, enter the corresponding number to the book.To return to the previous menu enter 0. \nEnter the number that corresponds to your menu selection:: ");
            menuSelection = validateMenuSelection(0, counter);
            int seePosts = -1;
            if (menuSelection != 0){
                CurrentBook selectedBook = tempCurrentBooks.get(menuSelection-1);
                ArrayList<SearchPost> matches = searchForSearchPosts(selectedBook.getTitle(), selectedBook.getAuthor());
                if(matches.size() > 0){
                    //SHOULD I REMOVE THIS? this might be redundant because if already has search post it won't go into the temp
                    boolean alreadyHasPost = checkForOwnSearchPost(matches, selectedBook.getTitle(), selectedBook.getAuthor());
                    if(alreadyHasPost){
                        System.out.println("\nLooks like you already have a post for that book!");
                        System.out.println("____________________________");
                    }else{
                        seePosts = confirmSelection("\nWould you like to see other users' Book Buddy Wanted posts for this book?");
                        if(seePosts == 1){
                            menu2Selection = displayAndGetSearchPostSelection(matches);
                            if(menu2Selection > 0 && menu2Selection <= matches.size()){
                                createNewBookGroup(matches.get(menu2Selection-1));
                            }
                        }
                    }
                }
                if(matches.size() == 0 || menu2Selection == matches.size()+1 || seePosts == 2){
                    menu3Selection = confirmSelection("Create a \"Book Buddy Wanted\" post for reading " +
                    selectedBook.getTitle() + " by " + selectedBook.getAuthor() + "?");
                    if(menu3Selection == 1){
                        createNewSearchPost(selectedBook);
                        System.out.println("Posted!\n\n");
                        System.out.println("____________________________");
                    }
                }
            }
        }
    }

    public static void manageCurrentBooks(){
        boolean stayCurrentBooks = true;
        int numSelected;
        int i;
        CurrentBook selectedBook;
        while (stayCurrentBooks){
            System.out.println("____________________________");
            System.out.println("\n\nCurrently Reading Manager\n\n");
            if (currentUser.getCurrentlyReading().size() < 1){
                System.out.println("You have no books in your Currently Reading.\n Returning you to the My Books Menu (From here, there is an option there to add books to your Currently Reading!)");
                stayCurrentBooks = false;
            } else {
                for(i = 1; i <= currentUser.getCurrentlyReading().size(); i++){
                    System.out.println(i + ". " + currentUser.getCurrentlyReading().get(i-1).getTitle() + " by "
                     + currentUser.getCurrentlyReading().get(i-1).getAuthor());
                    System.out.println("\tCurrent Page: " + currentUser.getCurrentlyReading().get(i-1).getPage()
                     + " Current Chapter: " + (currentUser.getCurrentlyReading().get(i-1).getCh() != -1 ? currentUser.getCurrentlyReading().get(i-1).getCh() : "-"));
                    if (currentUser.getCurrentlyReading().get(i-1).getGroup() != -1){
                        System.out.println("\tBook Buddy: " + currentUser.getCurrentlyReading().get(i-1).getBB());
                    }
                    System.out.println((""));
                }
                System.out.print("\nTo update your current page, mark a book as finished, or remove a book, enter the corresponding number to the book.\nTo return to the Books Menu enter 0.\n\nEnter your menu selection:  ");
                numSelected = validateMenuSelection(0, currentUser.getCurrentlyReading().size());
                if(numSelected == 0){
                    stayCurrentBooks = false;
                } else {
                    //give user options for selected book
                    selectedBook = currentUser.getCurrentlyReading().get(numSelected-1);
                    System.out.println("____________________________");
                    System.out.println("\nYou have selected " + selectedBook.getTitle() + " by " +
                    selectedBook.getAuthor() + ":\n");
                    System.out.print("""
                        1. Update Current Page
                        2. Mark Book as Read
                        3. Remove book from Currently Reading
                        4. Return to the Currently Reading Menu\n
                        Enter the number that corresponds to your menu selection: """);
                    int num2Selected = validateMenuSelection(1, 4);
                    if(num2Selected == 1){
                        updateCurrentPage(selectedBook);
                    }else if(num2Selected == 2){
                        //add to read books then remove from current
                        boolean alreadyInRead = currentUser.checkIfInReadBooks(selectedBook.getID());
                        if(!alreadyInRead){
                            ReadBook toAdd = new ReadBook(selectedBook.getID(), selectedBook.getTitle(), selectedBook.getAuthor(), 
                            currentUser.getUsername());
                            currentUser.addToReadBooks(toAdd);
                            allReadBooks.add(toAdd);
                        }
                        //NEW SPRINT3: only remove book if NOT in book group
                        if(selectedBook.getGroup() < 0){
                            removeCurrentBook(selectedBook);
                        }else{
                            //if in a book group just set+update isDone attribute
                            selectedBook.setIsDone(true);
                            updateCurrentlyReading();
                            //!!!FIX: update tracking of doneness in book group
                            BookGroup groupToUpdate = currentUser.getBookGroup(selectedBook.getGroup());
                            groupToUpdate.setUserIsDone(currentUser.getUsername());
                            updateBookGroups();
                        }
                    } else if(num2Selected == 3){
                        int confirmation1 = -1;
                        if(selectedBook.getGroup() > 0){
                            confirmation1 = confirmSelection("You are in a book group for this book. Removing this book will end the book group. Do you still want to remove the book?");
                            if(confirmation1 == 1){
                                removeCurrentBook(selectedBook);
                                //remove group
                                BookGroup toRemove = currentUser.getBookGroup(selectedBook.getGroup());
                                if(toRemove.getID() > 0){
                                    endBookGroup(toRemove);
                                }
                            }
                        }else{
                            System.out.println("\n" + selectedBook.getTitle() + " has been removed from your Currently Reading\n");
                            removeCurrentBook(selectedBook);
                        }   
                    }
                    //if 4 loop will just keep going
                }
            }
        }
    }

    public static void updateCurrentPage(CurrentBook toUpdate){
        //get new page num
        int newPageNum = getNewPageNum();
        int doTheUpdate = confirmSelection(("\nUpdate the current page to " + newPageNum + "?"));
        if(doTheUpdate == 1){
            if(toUpdate.getGroup() > 0){
                BookGroup groupToUpdate = currentUser.getBookGroup(toUpdate.getGroup());
                groupToUpdate.updateCurrentPage(newPageNum);
                updateBookGroups();
            }
            toUpdate.setCurrentPage(newPageNum);
            updateCurrentlyReading();
        }
    }

    //gets a page number from user for updating current page number
    public static int getNewPageNum(){
        int newPageNum = -1;
        System.out.print("\nEnter the page number you are currently on: ");
        do {
            while (!myScanner.hasNextInt()) {
                myScanner.next();
                System.out.print("\nEnter the page number you are currently on: ");
            }
            newPageNum = myScanner.nextInt();
        } while (newPageNum < 0);
        return newPageNum;
    }

    public static void manageViewReadBooks(){
        boolean stayReadBooks = true;
        int numSelected;
        int i;
        ReadBook selectedBook;
        while (stayReadBooks){
            System.out.println("____________________________");
            System.out.println("\n\nFinished Books Manager\n");
            //if user has no current books send them back to books menu
            if (currentUser.getReadBooks().size() < 1){
                System.out.println("You have no finished books yet. Returning to My Books Menu");
                stayReadBooks = false;
            } else {
                //give user option to return to books menu or to select a book to mark as done or remove
                for(i = 1; i <= currentUser.getReadBooks().size(); i++){
                    System.out.println(i + ". " + currentUser.getReadBooks().get(i-1).getTitle() + " by "
                      + currentUser.getReadBooks().get(i-1).getAuthor());
                    System.out.println("\tRating: " + ((currentUser.getReadBooks().get(i-1).getRating() != -1) ? currentUser.getReadBooks().get(i-1).getRating() + "/10" : "-"));
                    System.out.println((""));
                }
                System.out.print("\nTo rate a book, enter the corresponding number or enter 0 to return to the My Books Menu: ");
                numSelected = validateMenuSelection(0, currentUser.getReadBooks().size());
                System.out.println("");
                if (numSelected == 0){
                    stayReadBooks = false;
                } else {
                    selectedBook = currentUser.getReadBooks().get(numSelected-1);
                    selectedBook.rateBook(myScanner);
                    updateReadBookRating();
                }
            }
        }
    }

    public static void addCurrentBookMenu(){
        boolean stayInMenu = true;
        while(stayInMenu){
            //CAN ADD SEARCH BOOKS (by title and/or author) LATER
            System.out.println("\n____________________________\n");
            System.out.println("Add a book to currently reading:");
            System.out.print("""
                \n
                1. Enter a book (title and author)
                2. Select a book from our list of books
                3. Return to My Books Menu \n
                Enter the number that corresponds to your menu selection:
                """);
            int menuSelection = validateMenuSelection(1, 3);

            Book bookToAdd;
            CurrentBook toAdd;
            int confirmation;
            int alreadyInCurrentBooks;
            if(menuSelection == 1){
                System.out.println("____________________________");
                String title = checkStrInput("Enter the book's title:");
                String author = checkStrInput("Enter the name of the book's author:\n");
                confirmation = confirmSelection(("\nAdd " + title + " by " + author + " to your Currently Reading?"));
                if(confirmation == 1){
                    ArrayList<Book> searchMatches = searchAllBooks(title, author);
                    if(searchMatches.size()==0){
                        bookToAdd = addNewBook(title, author);
                    } else {
                        bookToAdd = searchMatches.get(0);
                    }
                    //add to currently reading + check if in currently reading
                    alreadyInCurrentBooks = currentUser.checkIfInCurrentBooks(bookToAdd.getID());
                    if (alreadyInCurrentBooks >= 0){
                        System.out.println("\n" + bookToAdd.getTitle() + " by " + bookToAdd.getAuthor() + 
                            "is already in your Currently Reading\n");
                    } else {
                        //otherwise add it to currently reading in book obj AND currentlyReading arraylist
                        toAdd = new CurrentBook(bookToAdd.getID(), bookToAdd.getTitle(), bookToAdd.getAuthor(), currentUser.getUsername());
                        allCurrentBooks.add(toAdd);
                        currentUser.addToCurrentlyReading(toAdd);
                        System.out.println("\n" + bookToAdd.getTitle() + " has been added to your Currently Reading!");
                    }
                }
            } else if (menuSelection == 2){
                System.out.println("\n\nSelect a book from the following menu, or enter 0 to go back:\n");
                for(int x = 0; x < allBooks.size(); x++){
                    System.out.println((x+1) + ". " + allBooks.get(x).getTitle() + " by " + 
                    allBooks.get(x).getAuthor());
                }
                System.out.println("");
                int menu2Selection = validateMenuSelection(0, allBooks.size());
                if(menu2Selection != 0){
                    bookToAdd = allBooks.get(menu2Selection-1);
                    confirmation = confirmSelection(("\nAdd " + bookToAdd.getTitle() + " by " + 
                        bookToAdd.getAuthor() + " to your Currently Reading?"));
                    if(confirmation == 1){
                        alreadyInCurrentBooks = currentUser.checkIfInCurrentBooks(bookToAdd.getID());
                        if (alreadyInCurrentBooks >= 0){
                            System.out.println("\n" + bookToAdd.getTitle() + " by " + bookToAdd.getAuthor() + 
                            "is already in your Currently Reading");
                        } else {
                            toAdd = new CurrentBook(bookToAdd.getID(), bookToAdd.getTitle(), bookToAdd.getAuthor(), currentUser.getUsername());
                            allCurrentBooks.add(toAdd);
                            currentUser.addToCurrentlyReading(toAdd);
                            System.out.println("\n" + bookToAdd.getTitle() + " has been added to your Currently Reading!");
                        }
                    }
                }
            } else {
                stayInMenu = false;
            }
        }
    }

    //search for a specific book in all books
    public static ArrayList<Book> searchAllBooks(String title, String author){
        ArrayList<Book> matches = new ArrayList<Book>();
        for(int i = 0; i < allBooks.size(); i++){
            if(allBooks.get(i).getTitle().equals(title) && allBooks.get(i).getAuthor().equals(author)){
                matches.add(allBooks.get(i));
            }
        }
        return matches;
    }

    public static ArrayList<SearchPost> searchForSearchPosts(String title, String author){
        ArrayList<SearchPost> matches = new ArrayList<SearchPost>();
        for(int i = 0; i < allSearchPosts.size(); i++){
            if(allSearchPosts.get(i).getTitle().equals(title) && allSearchPosts.get(i).getAuthor().equals(author)){
                matches.add(allSearchPosts.get(i));
            }
        }
        return matches;
    }

    //find a specific searchpost (connected to a specific user)
    public static SearchPost searchForSpecificSearchPost(String title, String author, String username){
        
        for(int i = 0; i < allSearchPosts.size(); i++){
            if(allSearchPosts.get(i).getTitle().equals(title) && allSearchPosts.get(i).getAuthor().equals(author)
             && allSearchPosts.get(i).getPoster().equals(username)){
                return allSearchPosts.get(i);
            }
        }
        return new SearchPost("", new Book(-1, "", ""));
    }

    //add book to all books arraylist and to books.csv, return book (with intention of adding to currently read)
    public static Book addNewBook(String title, String author){
        Book bookToAdd = new Book(Integer.valueOf(nextUserBookGroup.get(1)), title, author);
        //increment the next book id number
        incrementNextIdNums(1);
        allBooks.add(bookToAdd);
        try{
            FileWriter filewriter = new FileWriter("reading_bubby/appdata/books.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            String[] bookInfo = {Integer.toString(bookToAdd.getID()), bookToAdd.getTitle(), bookToAdd.getAuthor()};
            writer.writeNext(bookInfo);
            writer.close();
        }
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return bookToAdd;
    }

    //return false if match, return true if available
    public static boolean checkUsernameAvailability(String userName, ArrayList<String[]> usersList){
        for(int i = 0; i < usersList.size(); i++){
            if(usersList.get(i)[1].equals(userName.toLowerCase())){
                System.out.println("The username \"" + userName + "\" is already taken");
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String[]> createNewAccount(ArrayList<String[]> usersList){
        String userName = "";
        boolean isAvailable = false;
        while (!isAvailable) {
            userName = validateInputNoSpaces("Enter the username you would like (usernames cannot contain spaces): ", "username");
            isAvailable = checkUsernameAvailability(userName, usersList);
        }
        //once valid username enter a password
        String pw = validateInputNoSpaces("Enter a password (passwords cannot contain spaces): ", "password");
        String confirmationQuestion = "Create account with username \"" + userName + "\"?";
        int createAcct = confirmSelection(confirmationQuestion);
        if(createAcct == 1){
            usersList = addNewUserData(userName, pw, usersList);
        }
        return usersList;
    }

    public static void login(ArrayList<String[]> usersList){
        boolean keepTrying = true;
        String username;
        String password;
        int userIdNum;
        while (keepTrying) {
            //because an integer was the last thing entered via scanner must skip to next line
            myScanner.nextLine();
            System.out.print("\nUsername: ");
            username = myScanner.nextLine();
            System.out.print("\nPassword: ");
            password = myScanner.nextLine();
            userIdNum = checkLoginInfo(username, password, usersList);
            if(userIdNum > 0){
                currentUser = new User(userIdNum, username);
                isLoggedIn = true;
                keepTrying = false;
            } else {
                System.out.println("Incorrect username and/or password");
                System.out.println("____________________________");
                int yesOrNo = confirmSelection("Would you like to try logging in again?");
                if (yesOrNo == 2) {
                    keepTrying = false;
                }
            }
        }
    }

    public static int checkLoginInfo(String userName, String pw, ArrayList<String[]> allUsers){
        for(int i = 0; i < allUsers.size(); i++){
            if(allUsers.get(i)[1].equals(userName) && allUsers.get(i)[2].equals(pw)){
                return Integer.parseInt(allUsers.get(i)[0]);
            }
        }
        return -1;
    }

    //menu of all a user's book groups
    public static void bookGroupsManager(){
        boolean stayInMenu = true;
        if(currentUser.getBookGroups().size() > 0){
            int menuSelection;
            while (stayInMenu) {
                System.out.println("____________________________");
                System.out.println("\n\nYour Book Groups: \n");
                for(int i = 0; i < currentUser.getBookGroups().size(); i++){
                    System.out.println((i+1) + ".  Book Buddy: " + 
                        currentUser.getBookGroups().get(i).getBookBuddyName(currentUser.getUsername()) + " \n    Reading: " +
                         currentUser.getBookGroups().get(i).getTitle() + " by "
                        + currentUser.getBookGroups().get(i).getAuthor() + "\n");
                }
                System.out.print("""
                        \nEnter the corresponding number to select a group and view its full information, view posts made by
                        you and your Book Buddy, and be able write a new post in the group. Or, to return to the Main Menu 
                        enter 0: """);
                menuSelection = validateMenuSelection(0, currentUser.getBookGroups().size());
                if(menuSelection == 0){
                    stayInMenu = false;
                }else{
                    viewBookGroup(currentUser.getBookGroups().get(menuSelection-1));
                }
            }
        }else{
            System.out.println("____________________________");
            System.out.println("""
                You are not currently in any book groups--search Book Buddy Wanted posts to start a group right away
                 or create your own Book Buddy Wanted post!
                """);
        }
    }

    //displays book group info to user, options to create a post, read posts, update current page
    public static void viewBookGroup(BookGroup currentGroup){
        boolean stayIn = true;
        int menuSelection;
        int menu2Selection;
        while (stayIn) {
            String bookBuddyName = currentGroup.getBookBuddyName(currentUser.getUsername());
            System.out.println("\n\n\nBook: " + currentGroup.getTitle() + " by " + currentGroup.getAuthor());
            System.out.println("Book Buddy: " + bookBuddyName);
            //NEW SPRINT3: ternary operator to display page number or "Done" depending on if user done or not (left old commented out until tested)
            System.out.println("Your current page: " + (currentGroup.getUserIsDone(currentUser.getUsername())? "Done" : currentGroup.getUserPageNum(currentUser.getUsername())));
            System.out.println(bookBuddyName + "'s current page: " + (currentGroup.getUserIsDone(bookBuddyName) ? "Done" : currentGroup.getUserPageNum(bookBuddyName)));
            System.out.println("Posts in Group: " + currentGroup.getPosts().size());
            //NEW SPRINT3: adding option to end a book group
            System.out.print("""
                 \n
                1. Create a Post
                2. View All Posts in Group
                3. Update Your Current Page
                4. End Book Group
                5. Return to Book Groups menu:""");
            menuSelection = validateMenuSelection(1, 5);
            if(menuSelection == 5){
                stayIn = false;
            }else if(menuSelection == 1){
                currentGroup.addNewPost();
            }else if (menuSelection == 2){
                currentGroup.showPosts();
                System.out.println("\nEnter 1 to write a new post or enter 0 to return to the Book Groups menu: ");
                menu2Selection = validateMenuSelection(0, 1);
                if (menu2Selection == 1) {
                    currentGroup.addNewPost();
                }else{
                    stayIn = false;
                }
            }else if(menuSelection == 3){
                int newPageNum = getNewPageNum();
                int doTheUpdate = confirmSelection(("\nUpdate the current page to " + newPageNum + "?"));
                if(doTheUpdate == 1){
                    currentGroup.updateCurrentPage(newPageNum);
                    updateBookGroups();
                    CurrentBook toUpdate = currentUser.getSpecificCurrentBook(currentGroup.getBookID());
                    toUpdate.setCurrentPage(newPageNum);
                    updateCurrentlyReading();
                }
            }else if(menuSelection == 4){
                //NEW SPRINT3
                //confirm end group --> CAN ADD MORE LATER (TAILOR RESPONSES TO IF BOTH GROUP MEMBERS ARE DONE OR NOT ETC)
                System.out.println("""
                    You can continue a book group for as long as you would like, regardless of if one or both of you 
                    has finished the book.""");
                int endGroupConfirmation = confirmSelection("Are you sure you want to end this book group with " + bookBuddyName + "?");
                if(endGroupConfirmation == 1){
                    //adding extra level of confirmation that can be removed later
                    int confirmation2 = confirmSelection("End book group?");
                    if(confirmation2 == 1){
                        handleEndBookGroupViaManager(currentGroup);
                        stayIn = false;
                    }
                }
            }
        }
    }

    public static void handleEndBookGroupViaManager(BookGroup toEnd){
        int selection1;
        int selection2;
        CurrentBook usersCurrentBook = getSpecificCurrentBook(currentUser.getUsername(), toEnd.getBookID());
        if(toEnd.getUserIsDone(currentUser.getUsername())){
            removeCurrentBook(usersCurrentBook);
        }else{
            selection1 = confirmSelection("Did you finish the book? ");
            if(selection1 == 1){
                //add to read books (if not there)
                boolean alreadyInRead = currentUser.checkIfInReadBooks(usersCurrentBook.getID());
                if(!alreadyInRead){
                    ReadBook toAdd = new ReadBook(usersCurrentBook.getID(), usersCurrentBook.getTitle(), usersCurrentBook.getAuthor(), 
                    currentUser.getUsername());
                    currentUser.addToReadBooks(toAdd);
                    allReadBooks.add(toAdd);
                }
                removeCurrentBook(usersCurrentBook);
            }else{
                selection2 = confirmSelection("Would you like to keep " + toEnd.getTitle() + " by " + toEnd.getAuthor() + " in your Currently Reading Books?");
                if (selection2 == 1) {
                    //update currentbook to remove book buddy and book number
                    usersCurrentBook.setBookBuddy("null");
                    usersCurrentBook.setGroup(-1);
                    //ask if they would like to create a post to find a new book buddy
                    int selection3 = confirmSelection("Would you like to create a new Book Buddy Wanted post for this book? ");
                    if(selection3 == 1){
                        //calls function to update currently_reading.csv
                        createNewSearchPost(usersCurrentBook);
                    }else{
                        //if didn't make search post need to call to update the csv
                        updateCurrentlyReading();
                    }

                } else {
                    removeCurrentBook(usersCurrentBook);
                }
            }
        }
        endBookGroup(toEnd);
    }

    public static void endBookGroup(BookGroup toEnd){
        String bookBuddyName = toEnd.getBookBuddyName(currentUser.getUsername());
        CurrentBook buddysCurrentBook = getSpecificCurrentBook(bookBuddyName, toEnd.getBookID());
        if(buddysCurrentBook.getIsDone()){
            removeCurrentBook(buddysCurrentBook);
        }else{
            buddysCurrentBook.setGroup(-1);
            buddysCurrentBook.setBookBuddy("null");
            updateCurrentlyReading();
        }
        removeBookGroup(toEnd);
        removeGroupsPosts(toEnd);
        currentUser.removeBookGroup(toEnd);
    }

    public static void main(String[] args){
        //data gathering to happen before login
        getNextIds();
        getAllBooks();

        //this runs a loop that will prevent anything below from running until user logged in
        manageEntry();

        //once user logged in pull in all the relevant info for user
        getAllReadBooksData();
        getAllCurrentBooksData();
        getAllSearchPosts();

        //post-login code that only runs once (and thus not iin the main menu loop)
        if (currentUser.getID() > 0 && isLoggedIn){
            System.out.println("\n\n\nHello " + currentUser.getUsername() + "!\n");
            currentUser.initialSetCurrentlyReading(allCurrentBooks);
            currentUser.initialSetReadBooks(allReadBooks);
            currentUser.initialSetBookGroups();
        }

        int mainMenuSelection;//to hold number representing which menu choice user has made
        //Loop that constitutes being logged in and will keep returning user to main menu unless log out
        do{
            mainMenuSelection = mainMenu();
            switch (mainMenuSelection) {
                case 1:
                    manageMyBooksMenu();
                    break;
            
                case 2:
                    searchForBookBuddyMenu();
                    break;

                case 3:
                    bookGroupsManager();
                    break;
                case 4:
                    isLoggedIn = false;
                    break;
            }
        }
        while(isLoggedIn);
    }
}
