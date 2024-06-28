/*
 * CS335 
 * Summer 2024
 * Midterm - Group 1
 * Name: Sophie Steinberger
 * Created: 06/11/2024
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

    //INPUT VALIDATION (and helpers)
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
        System.out.print("1. Yes\n2. No\nSelection: ");
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

    //get+validate username or password - no whitespace allowed 
    //(whatEntered can be username or password (or anything else that fits this syntax))
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

    //FUNCTIONS FOR GETTING DATA AT LOGIN (the ones not belonging to User)
    public static void getNextIds(){
        try { 
            FileReader filereader = new FileReader("reading_bubby/appdata/next_id_nums.csv"); 
            CSVReader csvReader = new CSVReader(filereader); 
            String[] csvLine = csvReader.readNext();
            //ArrayList<Integer> nextIds = new ArrayList<Integer>();
            nextUserBookGroup.add(Integer.valueOf(csvLine[0]));
            nextUserBookGroup.add(Integer.valueOf(csvLine[1]));
            nextUserBookGroup.add(Integer.valueOf(csvLine[2]));
            csvReader.close();
            //return nextIds;
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    public static ArrayList<String[]> getAllUserData(){
        ArrayList<String[]> allUsers = new ArrayList<String[]>();
        try { 
            // Create an object of file reader class with CSV file as a parameter. 
            FileReader filereader = new FileReader("reading_bubby/appdata/users.csv"); 
            // create csvReader object and skip first Line 
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
            // Create an object of file reader class with CSV file as a parameter. 
            FileReader filereader = new FileReader("reading_bubby/appdata/currently_reading.csv"); 
            // create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                      .withSkipLines(1) 
                                      .build(); 
            List<String[]> allData = csvReader.readAll(); 
            CurrentBook tempCurrentBook;
            for (String[] row : allData) {
                tempCurrentBook = new CurrentBook(Integer.parseInt(row[0]), row[1], row[2], row[3], Integer.parseInt(row[4]), Integer.parseInt(row[5]), Integer.parseInt(row[6]), row[7], Boolean.valueOf(row[8]));
                allCurrentBooks.add(tempCurrentBook);
            }
        } 
        catch (Exception e) { 
            e.printStackTrace();
        } 
    }

    public static void getAllReadBooksData(){
        try { 
            // Create an object of file reader class with CSV file as a parameter. 
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

            //ArrayList<Book> tempBookList = new ArrayList<Book>();
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

    //FUNCTIONS FOR UPDATING DATA
    public static void removeCurrentBook(int idxForUserArr, CurrentBook toRemove){
        //remove from data file
        File file = new File("reading_bubby/appdata/currently_reading.csv"); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] {"book_id","title","author","username","current_page","current_chapter","group_id","book_buddy"});
            for(int x = 0; x < allCurrentBooks.size(); x++){
                if((toRemove.getID() == allCurrentBooks.get(x).getID()) && (toRemove.getUsername().equals(allCurrentBooks.get(x).getUsername()))){
                    //when the selected book is found do not write it to the data but remove it from the user's ArrayList and allCurrentBooks Arraylist
                    currentUser.removeFromCurrentlyReading(idxForUserArr);
                    allCurrentBooks.remove(x);
                } else {
                    data.add(new String[] {Integer.toString(allCurrentBooks.get(x).getID()), 
                        allCurrentBooks.get(x).getTitle(), allCurrentBooks.get(x).getAuthor(), 
                        allCurrentBooks.get(x).getUsername(), Integer.toString(allCurrentBooks.get(x).getPage()), 
                        Integer.toString(allCurrentBooks.get(x).getCh()), 
                        Integer.toString(allCurrentBooks.get(x).getGroup()), allCurrentBooks.get(x).getBB()});
                }
            }
            writer.writeAll(data); 
            // closing writer connection 
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
            data.add(new String[] {"book_id","title","author","username","current_page","current_chapter","group_id","book_buddy","has_open_search"});
            for(int x = 0; x < allCurrentBooks.size(); x++){
                data.add(new String[] {Integer.toString(allCurrentBooks.get(x).getID()), allCurrentBooks.get(x).getTitle(), 
                    allCurrentBooks.get(x).getAuthor(), allCurrentBooks.get(x).getUsername(), Integer.toString(allCurrentBooks.get(x).getPage()),
                    Integer.toString(allCurrentBooks.get(x).getCh()), Integer.toString(allCurrentBooks.get(x).getGroup()),
                    allCurrentBooks.get(x).getBB(), Boolean.toString(allCurrentBooks.get(x).getHasOpenSearch())});
            }
            writer.writeAll(data); 
            // closing writer connection 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    }

    public static void updateReadBookRating(){
        //in file (in allReadBooks rating should update because all referring to same object?)
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
            // closing writer connection 
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

    public static void updateBookBuddysEntry(BookGroup groupInfo){
        for(int i = 0; i < allCurrentBooks.size(); i++){
            if(allCurrentBooks.get(i).getUsername().equals(groupInfo.getBookBuddyName(currentUser.getUsername())) &&
                allCurrentBooks.get(i).getID() == groupInfo.getBookID()){
                allCurrentBooks.get(i).setSearchPostOff();
                allCurrentBooks.get(i).setBookBuddy(currentUser.getUsername());
                allCurrentBooks.get(i).setGroup(groupInfo.getID());
            }
        }
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
                    //when the selected book is found do not write it to the data but remove it from the user's ArrayList and allCurrentBooks Arraylist
                    allSearchPosts.remove(x);
                } else {
                    data.add(new String[] {allSearchPosts.get(x).getPoster(), Integer.toString(allSearchPosts.get(x).getBookID()),
                        allSearchPosts.get(x).getTitle(), allSearchPosts.get(x).getAuthor(), 
                        allSearchPosts.get(x).getWhenPosted().toString()});
                }
            }
            writer.writeAll(data); 
            // closing writer connection 
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
        if(currentUser.checkIfInCurrentBooks(wantedPost.getBookID())){
            CurrentBook toUpdate = currentUser.getSpecificCurrentBook(wantedPost.getBookID());
            if(toUpdate.getGroup() > 0){
                System.out.println("You already have a Book Buddy for this book! Check out your book group by selecting Manage Book Groups from the Main Menu.\n");
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
                 false);
            allCurrentBooks.add(toAdd);
            currentUser.addToCurrentlyReading(toAdd);
            createGroup = true;
        }
        if(createGroup){
            updateBookBuddysEntry(newGroup);
            updateCurrentlyReading();
            currentUser.addBookGroup(newGroup);
            removeSearchPost(wantedPost);
            try{
                FileWriter filewriter = new FileWriter("reading_bubby/appdata/book_groups.csv", true);
                CSVWriter writer = new CSVWriter(filewriter);
                String[] groupInfo = {Integer.toString(nextUserBookGroup.get(2)), Integer.toString(wantedPost.getBookID()),
                    wantedPost.getTitle(), wantedPost.getAuthor(), wantedPost.getPoster(), currentUser.getUsername(), 
                    Integer.toString(0), Integer.toString(0)};
                writer.writeNext(groupInfo);
                writer.close();
                incrementNextIdNums(2);
                System.out.println("\nYou are now book buddies with " + wantedPost.getPoster() + " to read " +
                    newGroup.getTitle() + " by " + newGroup.getAuthor() + "!\n\n");
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
            moveForward = false;
        }else if(toCheck.getHasOpenSearch()){
            System.out.println("You alredy have a \"Book Buddy Wanted\" post for this book!\n");
            moveForward = false;
        }else{
            moveForward = true;
        }
        return moveForward;
    }

    public static void createNewSearchPost(Book toAdd){
        //if in currently reading update the attribute indicating if there is a search post for it or not
        //if not in currently reading add it
        CurrentBook toUpdate;
        boolean addPost;
            //CHECK IF ALREADY HAVE A BOOK BUDDY/GROUP FOR THIS BOOK
        if(currentUser.checkIfInCurrentBooks(toAdd.getID())){
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
            System.out.println("""
                    Enter the number corresponding to your menu selection:\n
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
        System.out.println("Main Menu:\n");
        System.out.println("1. Manage My Books\n2. Find a Book Buddy!\n3. Manage Book Buddies & Groups\n4. Exit\n");
        System.out.print("Enter the number that corresponds to your menu selection: ");
        int selectedOption = validateMenuSelection(1, 4);
        return selectedOption;
    }

    public static void manageMyBooksMenu(){
        //While loop to return to this menu unless select to exit
        boolean stayBooksMenu = true;
        while (stayBooksMenu){
            System.out.println("\nMy Books Menu:\n");
            System.out.println("""
                1. View and Manage Currently Reading Books\n
                2. Add a Book to Currently Reading List\n
                3. View Finished Books\n
                4. Return to Main Menu\n""");
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



    //MARKER TO INDICATE WORKING HERE (JUST TO CATCH EYE WHEN SCROLLING)
    //MARKER TO INDICATE WORKING HERE (JUST TO CATCH EYE WHEN SCROLLING)
    //MARKER TO INDICATE WORKING HERE (JUST TO CATCH EYE WHEN SCROLLING)


    //IN PROGRESS
    public static void searchForBookBuddyMenu(){
        boolean returnToMenu = true;
        int menuSelection;
        while (returnToMenu) {
            System.out.print("""
                        Book Buddy Finder Menu\n
                        1. Select a book from your currently reading
                        2. Enter a book (title and author)
                        3. Select a book from the list of books
                        4. View all \"Book Buddy Wanted\" Posts

                        Enter your selection from the menu, or enter 0 to return to the Main Menu:
                        """);
            menuSelection = validateMenuSelection(0, 4);
            if(menuSelection == 1){
                bookBuddyFromCurrentlyReading();
            } else if(menuSelection == 2){
                enterSearchPostByBook();
            }else if(menuSelection == 3){
                chooseFromAllBooks();
            }else if (menuSelection == 4){
                viewAllSearchPosts();
            }else{
                returnToMenu = false;
            }
        }
    }

    //TO MOVE LATER FOR ORGANIZATION
    //TO MOVE LATER FOR ORGANIZATION
    //TO MOVE LATER FOR ORGANIZATION

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
        //!!!IF return val > 0 AND <= passed in ArrayList: that number minus (-) 1 is the index number of selected post
        // to accept in the ArrayList passed in)
        //!!IF return val == ArrayList passed in .size()+1: user selects make a new post for that book
        System.out.println("Posts Searching for a Book Buddy to Read " + posts.get(0).getTitle() + 
        " by " + posts.get(0).getAuthor() + ":\n");
        int x;
        //System.out.println("1. Create your own \"Book Buddy Wanted\" post");
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
        while (stayInMenu) {
            for(int i = 0; i < allBooks.size(); i++){
                System.out.println((i+1) + ". " + allBooks.get(i).getTitle() + " by " + allBooks.get(i).getAuthor());
            }
            System.out.print("\nEnter the corresponding number to select a book to find a book buddy (make a Book Buddy" +
             "Wanted post or answer someone else's), or 0 to return to the menu:");
            menuSelection = validateMenuSelection(0, allBooks.size());
            if(menuSelection > 0){
                //!!!!!MOVE THE CHECKING OF IF OPEN SEARCH TO BEFORE
                selectedBook = allBooks.get(menuSelection-1);
                posts = searchForSearchPosts(selectedBook.getTitle(), selectedBook.getAuthor());
                if(posts.size() > 0){
                    if(currentUser.checkIfInCurrentBooks(selectedBook.getID())){
                        currentSelectedBook = currentUser.getSpecificCurrentBook(selectedBook.getID());
                        System.out.println("\n" + (currentSelectedBook.getGroup() > 0) + "\n");
                        if(currentSelectedBook.getHasOpenSearch()){
                            System.out.println("\nLooks like you already have a post for that book!\n");
                            followUp = true;
                        }else if(currentSelectedBook.getGroup() > 0){
                            System.out.println("\nYou are already in a book group for that book!\n");
                            followUp = true;
                        }else{
                            seePosts = confirmSelection("Would you like to see other users' Book Buddy Wanted posts for this book?");
                            if(seePosts == 1){
                                menu2Selection = displayAndGetSearchPostSelection(posts);
                                if (menu2Selection > 0 && menu2Selection <= posts.size()){
                                    createNewBookGroup(posts.get(menuSelection-1));
                                    stayInMenu = false;
                                }
                            }
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
                if(followUp || menu3Selection == 2){
                    int menu4Selection = confirmSelection("Would you like to select a different book?");
                    if(menu4Selection == 2){
                        stayInMenu = false;
                    }
                }
            }
        }
    }

    public static void viewAllSearchPosts(){
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
            System.out.print("Enter the corresponding number to select a post and become Book Buddies, " +
            "or 0 to return to the menu:");
            int menuSelection = validateMenuSelection(0, postsToShow.size());
            if(menuSelection > 0){
                createNewBookGroup(postsToShow.get(menuSelection-1));
            }
        }else{
            System.out.println("\nThere are no post from other users to show right now. Check back later!\n");
        }
    }



    public static void enterSearchPostByBook(){
        int seePosts = -1;
        String title = checkStrInput("Enter the book's title: ");
        String author = checkStrInput("Enter the name of the book's author: ");
        //check if that book exists in data already
        ArrayList<Book> searchMatches = searchAllBooks(title, author);
        if(searchMatches.size() > 0){
            int menuSelection = -1;
            ArrayList<SearchPost> postMatches = searchForSearchPosts(title, author);
            if(postMatches.size() > 0){
                boolean alreadyHasPost = checkForOwnSearchPost(postMatches, title, author);
                if(alreadyHasPost){
                    System.out.println("\nLooks like you already have a post for that book!\n");
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
            if(postMatches.size() == 0 || menuSelection == postMatches.size()+1 || seePosts == 2){
                int menu2Selection = confirmSelection("Create a \"Book Buddy Wanted\" post for reading " +
                title + " by " + author + "?");
                if(menu2Selection == 1){
                    createNewSearchPost(searchMatches.get(0));
                    System.out.println("Posted!\n\n");
                }
            }
        } else {
            int menu3Selection = confirmSelection("Create a \"Book Buddy Wanted\" post for reading " +
                title + " by " + author + "?");
            if(menu3Selection == 1){
                Book bookToAdd = addNewBook(title, author);
                createNewSearchPost(bookToAdd);
                System.out.println("Posted!\n\n");
            }
        }
    }

    public static void bookBuddyFromCurrentlyReading(){
        int menuSelection;
        int menu2Selection = -1;
        int menu3Selection;
        if (currentUser.getCurrentlyReading().size() < 1){
            System.out.println("You have no books in your Currently Reading.\n");
        } else {
            //use a separate counter since will skip books that already have a book buddy
            int counter = 0;
            //keep books that don't have a buddy in temp separate array to have corresponding number
            ArrayList<CurrentBook> tempCurrentBooks = new ArrayList<CurrentBook>();
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
            System.out.print("To select a book, enter the corresponding number to the book.\nTo return to the previous menu enter 0: ");
            menuSelection = validateMenuSelection(0, counter);
            int seePosts = -1;
            if (menuSelection != 0){
                CurrentBook selectedBook = tempCurrentBooks.get(menuSelection-1);
                ArrayList<SearchPost> matches = searchForSearchPosts(selectedBook.getTitle(), selectedBook.getAuthor());
                if(matches.size() > 0){
                    //SHOULD I REMOVE THIS? this might be redundant because if already has search post it won't go into the temp
                    boolean alreadyHasPost = checkForOwnSearchPost(matches, selectedBook.getTitle(), selectedBook.getAuthor());
                    if(alreadyHasPost){
                        System.out.println("Looks like you already have a post for that book!");
                    }else{
                        seePosts = confirmSelection("Would you like to see other users' Book Buddy Wanted posts for this book?");
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
                    }
                }
            }
        }
    }

    //TO MOVE LATER FOR ORGANIZATION
    //TO MOVE LATER FOR ORGANIZATION^^^^^

    public static void manageCurrentBooks(){
        //menu of currently reading books with action options that loops until exit
        boolean stayCurrentBooks = true;
        int numSelected;
        int i;
        CurrentBook selectedBook;
        while (stayCurrentBooks){
            System.out.println("\n\nCurrently Reading Manager\n");
            //if user has no current books send them back to books menu
            if (currentUser.getCurrentlyReading().size() < 1){
                System.out.println("You have no books in your Currently Reading.\n Returning you to the My Books Menu (there is an option there to add books to your Currently Reading!)");
                stayCurrentBooks = false;
            } else {
                //give user option to return to books menu or to select a book to mark as done or remove
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
                System.out.print("To mark a book as finished or remove a book, enter the corresponding number to the book.\nTo return to the Books Menu enter 0: ");
                numSelected = validateMenuSelection(0, currentUser.getCurrentlyReading().size());
                if(numSelected == 0){
                    //if select exit change LCV
                    stayCurrentBooks = false;
                } else {
                    //give user options for selected book
                    selectedBook = currentUser.getCurrentlyReading().get(numSelected-1);
                    System.out.println("\nYou have selected " + selectedBook.getTitle() + " by " +
                    selectedBook.getAuthor() + ":\n");
                    System.out.println("""
                        1. Mark Book as Read
                        2. Remove book from Currently Reading
                        3. Return to the Currently Reading Menu\n
                        Enter the number that corresponds to your menu selection: 
                        : """);
                    int num2Selected = validateMenuSelection(1, 3);
                    if(num2Selected == 1){
                        //add to read books then remove from current
                        boolean alreadyInRead = currentUser.checkIfInReadBooks(selectedBook.getID());
                        if(!alreadyInRead){
                            ReadBook toAdd = new ReadBook(selectedBook.getID(), selectedBook.getTitle(), selectedBook.getAuthor(), 
                            currentUser.getUsername());
                            currentUser.addToReadBooks(toAdd);
                            allReadBooks.add(toAdd);
                        } else {
                            System.out.println("\n" + selectedBook.getTitle() + " is already in your Read Books\n");
                        }
                        removeCurrentBook((numSelected-1), selectedBook);
                        //later will need to add more to preserve and update group relationship
                    } else if(num2Selected == 2){
                        removeCurrentBook((numSelected-1), selectedBook);
                        System.out.println("\n" + selectedBook.getTitle() + " has been removed from your Currently Reading\n");
                        //!!!later will need to add more to deal with potential group membership
                    }
                    //if 3 loop will just keep going
                }
            }
        }
    }

    public static void manageViewReadBooks(){
        //menu of read books with to select to rate or exit loop/menu
        boolean stayReadBooks = true;
        int numSelected;
        int i;
        ReadBook selectedBook;
        while (stayReadBooks){
            System.out.println("\n\nRead Books Manager\n");
            //if user has no current books send them back to books menu
            if (currentUser.getReadBooks().size() < 1){
                System.out.println("You have no read books yet. Returning to My Books Menu");
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
            System.out.print("""
                \n
                1. Enter a book (title and author)\n
                2. Select a book from our list of books\n
                3. Return to My Books Menu\n
                Enter the number that corresponds to your menu selection:
                """);
            int menuSelection = validateMenuSelection(1, 3);

            Book bookToAdd;
            CurrentBook toAdd;
            int confirmation;
            boolean alreadyInCurrentBooks;
            if(menuSelection == 1){
                String title = checkStrInput("Enter the book's title:");
                String author = checkStrInput("Enter the name of the book's author:\n");
                confirmation = confirmSelection(("\nAdd " + title + " by " + author + " to your Currently Reading?"));
                if(confirmation == 1){
                    ArrayList<Book> searchMatches = searchAllBooks(title, author);
                    if(searchMatches.size()==0){
                        bookToAdd = addNewBook(title, author);
                    } else {
                        bookToAdd = searchMatches.get(0);
                    }//will add other else to manage searches with multiple matches when adding search by only title or author/current else will become "else if(searchMatches.size()==1)"
                    
                    //add to currently reading + check if in currently reading
                    alreadyInCurrentBooks = currentUser.checkIfInCurrentBooks(bookToAdd.getID());
                    if (alreadyInCurrentBooks){
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
                        if (alreadyInCurrentBooks){
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
    //EXPAND FUNCTION TO BE ABLE TO SEARCH BY TITLE OR AUTHOR ONLY (returning array list for easy expansion of function to more matches)
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

    //ADD USER
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

    //LOGIN
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
                    System.out.println("\nIn development--Book Groups coming soon\n\n!");
                    break;
                
                case 4:
                    isLoggedIn = false;
                    break;
            }
        }
        while(isLoggedIn);
    }
}
